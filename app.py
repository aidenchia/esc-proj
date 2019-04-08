from flask import Flask
from flask import flash, g, redirect, render_template, url_for, request, session, abort
from flask import current_app
from flask_login import login_required, current_user, login_user,logout_user
from forms import LoginForm, RegisterForm
from models import Users, Subjects
from werkzeug.security import generate_password_hash
import functools
import os
import sys


app = Flask(__name__)
app.config.from_object(os.environ['APP_SETTINGS'])
print("[INFO] DATABASE_URL:", app.config["SQLALCHEMY_DATABASE_URI"])

from models import db, login_manager
with app.app_context():
  db.init_app(app)
  db.create_all()
  login_manager.init_app(app)
  login_manager.login_view = "login"


######################################## Wrapper for roles required #################
def Roles(included=True, *role):
    """
    if included is true, means any roles that is typed must match the user's user_group.
    
    if included is false, means any roles that is typed must NOT match the user's user_group.
    """
    def decorater(view):
        @functools.wraps(view)
        def wrapped_view(*args,**kwargs):
            #flash(str(current_user.user_group))
            #flash(str(role))
            if current_user.is_authenticated:
                if included:
                    if current_user.user_group not in role:
                        return redirect(url_for('login'))
                else:
                    if current_user.user_group in role:
                        return redirect(url_for('login'))
            else:
                return redirect(url_for('login'))
            return view(*args,**kwargs)
        return wrapped_view
    return decorater

 
########################################## ALL USERS ##########################
@app.route('/', methods=['GET', 'POST'])
def login():
  if current_user.is_authenticated:
    return redirect_user(current_user)

  form = LoginForm()
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
    if user is not None and user.check_password(form.password.data):
        login_user(user)
        return redirect_user(user)

    flash('Invalid username / password')    
    #return redirect(url_for('register'))
  return render_template('login.html', title="Sign In", form=form)

def redirect_user(user):
  if user.user_group == 'student':
    return redirect(url_for('viewSchedule'))

  elif user.user_group == 'admin':
    return redirect(url_for('register'))

  elif user.user_group == 'pillar_head' or user.user_group == 'subject_lead':
    return redirect(url_for('courseInput'))


@app.route("/logout")
def logout():
  logout_user()
  session.clear()
  return redirect(url_for('login'))

########################################## COURSE LEAD ##########################
@app.route('/courseInput', methods=['GET','POST'])
@login_required
#@Roles("student")
def courseInput():
    #flash(str(current_user.user_group))
    return render_template('index.html')

@app.route("/database", methods=['GET','POST'])
@login_required
def subjectsTable():
  try:
    inserted = Subjects.insert( 
      request.form['subjectCode'],
      request.form['term'], 
      request.form['subjectType'], 
      request.form['subjectName'])
  except:
    print("Empty fields")
    
  result = db.session.query(Subjects).order_by(Subjects.subjectName).all()
  return render_template("database.html", result = result)

########################################## ADMIN ##########################
@app.route('/register', methods=['GET', 'POST'])
@login_required
@Roles(True,"admin")
def register():
  form = RegisterForm()
  flash(str(current_user.user_group))
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
    flash(str(user))
    if user is None:
        Users.insert(form.username.data,form.fullname.data,form.email.data, generate_password_hash(form.password.data), dict(form.user_choices).get(form.user_group.data))
        return redirect(url_for('courseInput'))
    flash('Invalid Parameters')
  return render_template('register.html',form=form)

#  if form.validate_on_submit():
#    Users.insert(request.form['username'],
#                 request.form['fullname'],
#                 request.form['email'],
#                 request.form['password'],
#                 request.form['user_group'])
#
#    return redirect(url_for('displayUsers'))  
#  
#  return render_template('register.html')


@app.route("/usersTable", methods=['GET', 'POST'])
def usersTable():
  Users.insert(request.form['username'],
               request.form['fullname'],
               request.form['email'],
               request.form['password'],
               request.form['user_group'])

  allUsers = db.session.query(Users).order_by(Users.fullname).all()
  return render_template("usersTable.html", allUsers = allUsers)

@app.route("/editUsers", methods=['GET', 'POST'])
def editUsers():
  #Users.edit(request.form['pillar']
  #           request.form['term']
  #           request.form['student_id']
  #           request.form['student_group'])

  return render_template("register.html")



@app.route("/home", methods=['GET','POST'])
@login_required
def index():
    return render_template("base.html",)



######################################## STUDENTS #################
@login_required
def viewSchedule():
  return render_template("base.html") # for now

######################################## Scheduling algorithm #################
def genSchedule():
  import subprocess
  wd = './javaFiles'
  subprocess.call(['javac', 'Scheduler.java'], cwd=wd)
  subprocess.call(['java', 'Scheduler'], cwd=wd)

  
if __name__ == "__main__":
  app.run(host='0.0.0.0', port=5000)