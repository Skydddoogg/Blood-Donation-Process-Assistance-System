
import os
from flask import session, redirect, request, Flask, render_template, url_for
from flask_login import LoginManager, login_required, login_user, logout_user, current_user
from flask_session import Session
import json
import hashlib
from firebase import firebase
from flask_cors import CORS
from datetime import datetime

from models.Officer import Officer
from myconfig import database_url, donation_key, donor_profile_key, officer_key, form_key, form_request_checker

app = Flask(__name__)
CORS(app)
app.secret_key = "bloodDonorRecorder"
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = "login"
firebase = firebase.FirebaseApplication(database_url, None)

@app.route('/confirmation/<request_id>', methods=['GET', 'POST'])
def confirmation(request_id):

    # Get specified request
    confirmation = firebase.get(form_key + '/' + request_id, None)

    return render_template('confirmation.html', confirmation=confirmation)

@app.route('/requestInformation', methods=['GET', 'POST'])
def requestInformation():
    
    # Get all donation requests and their donors
    hospital = session['id']
    donation_requests = firebase.get(form_key + '/' + hospital, None)

    donors = {}
    for donor in donation_requests:
        donors[donor] = firebase.get(donor_profile_key + '/' + donor, None)

    return render_template('request_information.html', donation_requests=donation_requests, donors=donors)

@app.route('/searchResult', methods=['POST'])
def searchForDonor():

    if request.method == 'POST':
        required_id_number = request.form['required_id_number']

        if required_id_number == '':
            return redirect('/')
        else:
            result = firebase.get(donor_profile_key + '/' + required_id_number, None)
            return index(result, required_id_number)


@app.route('/recordBloodDonation/<id_number>', methods=['GET', 'POST'])
def recordBloodDonation(id_number):

        # Get records for ordinal definition
        records = firebase.get(donation_key + '/' + id_number, None)

        # Calculate the ordinal of donation
        if records is None:
                ordinal = 1
        else:
                current_ordinal = len(records)
                ordinal = current_ordinal + 1

        # Get the objection of timestamp
        now = datetime.now()

        # Set current date
        current_date = "{:02d}/{:02d}/{:d}".format(now.day, now.month, now.year+543)

        # Prepare the record information as JSON object
        new_record = {
                "checker": request.form['checker'],
                "date": current_date,
                "hospital": session['username'],
                "ordinal": ordinal
        }

        # Post the record into the database
        post_result = firebase.post(donation_key + '/' + id_number, new_record)

        return redirect('/donorProfile/' + id_number)

@app.route('/sendPreDonationForm/<id_number>', methods=['GET', 'POST'])
def sendPreDonationForm(id_number):

    # Switch request status for specified donor (False -> True)
    firebase.put(form_request_checker, id_number, {"request": True})

    return redirect('/donorProfile/' + id_number)

@app.route('/donorProfile/<id_number>')
def donorProfilePage(id_number):

    # Get all blood donation records of a specified donor
    records = firebase.get(donation_key + '/' + id_number, None)

    # Get the personal inforamtion
    personal_infor = firebase.get(donor_profile_key + '/' + id_number, None)

    return render_template('profile.html', records=records, personal_infor=personal_infor, id_number=id_number)

@app.route('/')
def index(result_search=None, id_number=None):
    if current_user.is_authenticated:
        return render_template('index.html', result_search=result_search, id_number=id_number)
    else:
        return render_template('login.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        req = request.form
        username = req['username']
        password = hashlib.sha1(req['password'].encode('utf-8')).hexdigest()
        query = firebase.get('/officer/authen', username)
        if query is None:
            return redirect('/')
        elif query['password'] != password:
            return redirect('/')
        officer = Officer()
        officer.id = username
        login_user(officer)
        return redirect('/')
    else:
        return redirect('/')


@app.route('/logout')
@login_required
def logout():
    session.pop('username', None)
    logout_user()
    return redirect('/')


@app.route('/signup')
@login_required
def signup():
    return render_template('signup.html')


@app.route('/validateAvailableId', methods=['GET', 'POST'])
@login_required
def validateAvailableId():
    if request.method == 'POST':
        req = request.json
        print("here", req)
        if firebase.get('/donor/authen', req['idcard']) is None:
            return json.dumps(True)
        else:
            return json.dumps(False)


@app.route('/register', methods=['GET', 'POST'])
@login_required
def register():
    if request.method == 'POST':
        req = request.form
        json_req = {**req}
        idcard = json_req.pop('idcard')[0]
        if firebase.get('/donor/authen', idcard) is None:
            password = hashlib.sha1(json_req.pop('password')[0].encode('utf-8')).hexdigest()
            firebase.put('donor/authen', idcard, {"password": password})
            firebase.put('donor/profile', idcard, json_req)
            return redirect('/')
        else:
            return redirect('/')


@login_manager.user_loader
def user_loader(username):
    query = firebase.get('/officer/authen', username)
    if query is None:
        return redirect('/')
    query_userdata = firebase.get('/officer/data', username)
    session['username'] = query_userdata['hospital_name_th']
    session['id'] = username
    officer = Officer()
    officer.id = username
    return officer


@login_manager.unauthorized_handler
def unauthorized_handler():
    return 'Unauthorized'

if __name__ == "__main__":
    app.run(debug=True)

