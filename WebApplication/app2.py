
import os
from flask import session, redirect, request, Flask, render_template, url_for
import json
import hashlib
from firebase import firebase
from flask_cors import CORS
from datetime import datetime

app = Flask(__name__)
CORS(app)
app.secret_key = "bloodDonorRecorder"
firebase = firebase.FirebaseApplication('https://testweb-e5763.firebaseio.com/', None)

@app.route('/recordBloodDonation/<id_number>', methods=['GET', 'POST'])
def recordBloodDonation(id_number):
        records = firebase.get('/donors/blood_donation/' + id_number, None)
        if records is None:
                ordinal = 1
        else:
                current_ordinal = len(records)
                ordinal = current_ordinal + 1

        now = datetime.now()
        current_date = "{:02d}/{:02d}/{:d}".format(now.day, now.month, now.year+543)

        new_record = {
                "checker": "Chonburi Hospital",
                "date": current_date,
                "hospital": "Chonburi Hospital",
                "ordinal": ordinal
        }

        post_result = firebase.post('/donors/blood_donation/' + id_number, new_record)

        return redirect('/donorProfile/' + id_number)

@app.route('/donorProfile/<id_number>')
def donorProfilePage(id_number):

    # Get all blood donation records of a specified donor
    records = firebase.get('/donors/blood_donation/' + id_number, None)

    # Get the personal inforamtion
    personal_infor = firebase.get('/donors/personal_infor/' + id_number, None)

    return render_template('profile.html', records=records, personal_infor=personal_infor, id_number=id_number)

@app.route('/')
def index():
    donors = firebase.get('/donors/personal_infor', None)
    return render_template('index.html', donors=donors)

@app.route('/login')
def loginPage():
    return render_template('login.html')

if __name__ == "__main__":
    app.run(debug=True)

