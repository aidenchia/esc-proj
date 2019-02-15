from flask import Flask
from flask import Flask, flash, redirect, render_template, request, session, abort
import os

app = Flask(__name__)
 
@app.route('/')
def home():
  if not session.get('logged_in'):
    return render_template('login.html')
  else:
    return "Logged In!"
 
@app.route('/login', methods=['POST'])
def do_admin_login():
  if request.form['password'] == 'password' and request.form['username'] == 'admin':
    session['logged_in'] = True
    return "Logged In!"
  else:
    flash('wrong password!')
    return home()

@app.route("/logout")
def logout():
  session['logged_in'] = False
  return home()

@app.route('/course-details', methods=['GET','POST'])
def do_prof_input():
  return render_template('courseDetails.html')

 
if __name__ == "__main__":
  app.secret_key = os.urandom(12)
  app.config['SESSION_TYPE'] = 'filesystem'
  app.run(debug=True,host='0.0.0.0', port=5000)