from flask import Flask
from flask import flash, redirect, render_template, url_for, request, session, abort
from flask_login import login_required, current_user, login_user,logout_user
from forms import LoginForm
import os
from models import Users


app = Flask(__name__)
app.config.from_object(os.environ['APP_SETTINGS'])
print("[INFO] DATABASE_URL:", app.config["SQLALCHEMY_DATABASE_URI"])

from models import db, login_manager
with app.app_context():
  db.init_app(app)
  db.create_all()
  login_manager.init_app(app)
  #user = Users('felicePeck', 'Felice Peck', 'felice_peck@mymail.sutd.edu.sg', 'password', 'staff', False)
  #db.session.add(user)
  #db.session.commit()
 
@app.route('/', methods=['GET', 'POST'])
@app.route('/login', methods=['GET','POST'])
def login():
  if current_user.is_authenticated:
    return redirect(url_for('courseInput'))

  form = LoginForm()
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
    if user is None or not user.check_password(form.password.data):
      flash('Invalid username of password')
      return redirect(url_for('login'))
    login_user(user, remember=form.remember_me.data)
    return redirect(url_for('courseInput'))
  return render_template('login.html', title="Sign In", form=form)
 
@app.route('/courseInput', methods=['GET','POST'])
@login_required
def courseInput():
    return render_template('index.html')

@app.route("/logout")
def logout():
  logout_user()
  return redirect(url_for('login'))

@app.route("/database", methods=['GET','POST'])
@login_required
def display():
  from models import Subjects
  try:
    inserted = Subjects.insert( 
      request.form['subjectCode'],
      request.form['term'], 
      request.form['subjectType'], 
      request.form['subjectName'])
  except:
    print("Empty fields")
    
  result = Subjects.select(all=True)
  return render_template("database.html", result = result)

@app.route("/export", methods=['GET', 'POST'])
@login_required
def getTable():
  from models import Subjects
  result = Subjects.export(app)
  return result
  
if __name__ == "__main__":
  app.run(host='0.0.0.0', port=5000)