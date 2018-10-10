
import os
from flask import session, redirect, request, Flask, render_template, url_for
import json
import uuid 
import hashlib
from firebase import firebase
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
app.secret_key = "bloodDonorRecorder"
firebase = firebase.FirebaseApplication('https://testweb-e5763.firebaseio.com/', None)

@app.route('/recordBloodDonation', methods=['GET', 'POST'])
def recordBloodDonation():
    if request.method == 'POST':
        data = request.get_json()
        print("data", data)
        result = firebase.get('/donors/blood_donation/1350100453843', None)
        print("result", result)
        return redirect('/hello')

@app.route('/testJsonAction', methods=['GET', 'POST'])
def testJson():
    if request.method == 'POST':
        data = request.get_json()
        return redirect('/hello')

@app.route('/donorProfile')
def donorProfilePage():
    # Get all blood donation records of a specified donor
    records = firebase.get('/donors/blood_donation/1350100453843', None)
    return render_template('profile.html', records=records)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/login')
def loginPage():
    return render_template('login.html')

if __name__ == "__main__":
    app.run(debug=True)

