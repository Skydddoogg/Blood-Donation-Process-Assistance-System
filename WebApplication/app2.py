
import os
from flask import session, redirect, request, Flask, render_template, url_for
import json
import hashlib
from firebase import firebase
from flask_cors import CORS
from datetime import datetime
from myconfig import database_url, donation_key, donor_profile_key

app = Flask(__name__)
CORS(app)
app.secret_key = "bloodDonorRecorder"
firebase = firebase.FirebaseApplication(database_url, None)

@app.route('/searchResult', methods=['POST'])
def searchForDonor():

        required_id_number = request.form['required_id_number']

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
                "hospital": "Chonburi Hospital",
                "ordinal": ordinal
        }

        # Post the record into the database
        post_result = firebase.post(donation_key + '/' + id_number, new_record)

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
    return render_template('index.html', result_search=result_search, id_number=id_number)

if __name__ == "__main__":
    app.run(debug=True)

