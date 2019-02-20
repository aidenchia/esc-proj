from flask import Flask
from flask import flash, redirect, render_template, url_for, request, session, abort
import os


app = Flask(__name__)
app.config.from_object(os.environ['APP_SETTINGS'])
print("[INFO] DATABASE_URI:", app.config["SQLALCHEMY_DATABASE_URI"])

from models import db
with app.app_context():
  db.init_app(app)
  db.create_all()
 
@app.route('/')
def home():
  if not session.get('logged_in'): # if not logged in
    return render_template('login.html')
  else: # if logged in already
    return "Logged In"
 
@app.route('/login', methods=['GET','POST'])
def do_admin_login():
  if request.form['password'] == 'password' and request.form['username'] == 'admin':
    session['logged_in'] = True
    return render_template('index.html')
  else:
    flash('wrong password!')
    return home()

@app.route("/logout")
def logout():
  session['logged_in'] = False
  return home()

@app.route("/database", methods=['GET','POST'])
def display():
  from models import Subjects
  esc = Subjects(50.006, 5)
  db.session.add(esc)
  db.session.commit()
  return db.session.query(Subjects).filter_by(subjectCode=50.006).first()


 
if __name__ == "__main__":
  app.run(host='0.0.0.0', port=5000)