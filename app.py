from flask import Flask
from flask import flash, g, redirect, render_template, url_for, request, session, abort
from flask_login import login_required, current_user, login_user,logout_user
from forms import LoginForm, RegisterForm
import os
from models import Users, Subjects


app = Flask(__name__)
app.config.from_object(os.environ['APP_SETTINGS'])
print("[INFO] DATABASE_URL:", app.config["SQLALCHEMY_DATABASE_URI"])

from models import db, login_manager
with app.app_context():
  db.init_app(app)
  db.create_all()
  login_manager.init_app(app)
 
@app.route('/', methods=['GET', 'POST'])
@app.route('/login', methods=['GET','POST'])
def login():
  if current_user.is_authenticated:
    return redirect(url_for('courseInput'))

  form = LoginForm()
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
    if user is None or not user.check_password(form.password.data):
      flash('Invalid username / password')
      return redirect(url_for('login'))
    login_user(user, remember=form.remember_me.data)
    #return redirect(url_for('courseInput'))
    return redirect(url_for('register'))
  return render_template('login.html', title="Sign In", form=form)
 
@app.route('/courseInput', methods=['GET','POST'])
@login_required
def courseInput():
    return render_template('index.html')

@app.route('/register', methods=['GET', 'POST'])
def register():
  form = RegisterForm()
  if form.validate_on_submit():
    return render_template(url_for('displayUsers'))
  return render_template('register.html')

@app.route("/logout")
def logout():
  logout_user()
  return redirect(url_for('login'))

@app.route("/usersTable")
def displayUsers():
  allUsers = db.session.query(Users).order_by(Users.fullname).all()
  return render_template("usersTable.html", allUsers = allUsers)

@app.route("/database", methods=['GET','POST'])
@login_required
def displaySubjects():
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


  
if __name__ == "__main__":
  app.run(host='0.0.0.0', port=5000)