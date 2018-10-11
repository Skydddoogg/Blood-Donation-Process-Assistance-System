import os
import hashlib
import json
from models.Officer import Officer
from flask import Flask, render_template, request, abort, redirect, url_for, jsonify, session
from flask_login import LoginManager, login_required, login_user, logout_user, current_user
from flask_cors import CORS
from flask_session import Session
from firebase import firebase

app = Flask(__name__)
CORS(app)
app.secret_key = os.urandom(24)
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = "login"
firebase = firebase.FirebaseApplication('https://fit-sanctum-169618.firebaseio.com/', None)


@app.route('/')
def index():
    if current_user.is_authenticated:
        return render_template('index.html')
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
            print(True)
            return json.dumps(True)
        else:
            print(False)
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
            return "Adding complete"
        else:
            return "Duplicate ID"


# @app.route('/home')
# @login_required
# def home():
#     return render_template('home.html')


# simple method for create officer account
# @app.route('/create_officer', methods=['GET', 'POST'])
# def create_officer():
#     if request.method == 'POST':
#         req = request.json
#         username = req['username']
#         password = hashlib.sha1(req['password'].encode('utf-8')).hexdigest()
#         req.pop('username', None)
#         req.pop('password', None)
#         firebase.put('/officer/authen', username, {'password': password})
#         firebase.put('officer/data', username, req)
#         return "Officer account created"


# @app.route('/profile')
# # @login_required
# def get_profile():
#     id_card = request.args.get('id_card', type=str)
#     query = firebase.get('donor/data', id_card)
#     if query is None:
#         return abort(404)
#     # TEST: Showing query result
#     return jsonify(query)


@login_manager.user_loader
def user_loader(username):
    query = firebase.get('/officer/authen', username)
    if query is None:
        return redirect('/')
    query_userdata = firebase.get('/officer/data', username)
    session['username'] = query_userdata['hospital_name_th']
    officer = Officer()
    officer.id = username
    return officer


# @login_manager.request_loader
# def request_loader(request):
#     req = request.json
#     if req is None:
#         return
#     username = req['username']
#     password = hashlib.sha1(req['password'].encode('utf-8')).hexdigest()
#     query = firebase.get('/officer/authen', username)
#     if query is None:
#         return
#     officer = Officer()
#     officer.id = username
#     officer.is_authenticated = password == query['password']


@login_manager.unauthorized_handler
def unauthorized_handler():
    return 'Unauthorized'
    # return render_template('index.html')


if __name__ == '__main__':
    app.run(debug=True)
