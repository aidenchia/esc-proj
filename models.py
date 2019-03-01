from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager
from werkzeug.security import generate_password_hash, check_password_hash


db = SQLAlchemy()
login_manager = LoginManager()
login_manager.login_view = 'login'

class Subjects(db.Model):
  subjectCode = db.Column(db.Float, primary_key=True) # primary keys are unique identifiers
  term = db.Column(db.Integer, nullable=True)
  subjectType = db.Column(db.Text, nullable=True)
  subjectName = db.Column(db.Text, nullable=True)

  def __init__(self, subjectCode, term, subjectType, subjectName):
    self.subjectCode = subjectCode
    self.term = term
    self.subjectType = subjectType
    self.subjectName = subjectName

  def __repr__(self):
    return '{}: {}'.format(self.subjectCode, self.subjectName)

  @staticmethod
  def select(all=False):
    if all:
      query = db.session.query(Subjects).order_by(Subjects.subjectName).all()
      return query

    else:
      return "TODO"

  # only works on localhost, because on heroku each dyno has its own ephermeral filesystem, where any files written will be discarded
  @staticmethod
  def export(app):
    import psycopg2
    conn = psycopg2.connect(app.config['SQLALCHEMY_DATABASE_URI'], sslmode='require')
    cur = conn.cursor()
    excel = open('course_details.csv', 'w+')
    cur.copy_to(excel, table="Subjects", sep=",")
    excel.close()

    return "Data written to 'course_details.csv'"


  @staticmethod
  def insert(subjectCode, term, subjectType, subjectName):
    if None in (subjectCode, term, subjectType, subjectName):
      return "Please fill in all required fields"

    subject = db.session.query(Subjects).filter_by(subjectCode=subjectCode).first()
    if subject is not None:
      result = "{}: {} already in database".format(str(subject.subjectCode), str(subject.subjectName))
      return result
    else:
      subject = Subjects(subjectCode, term, subjectType, subjectName)
      db.session.add(subject)
      db.session.commit()
      result = "Added {}: {} to database".format(str(subject.subjectCode), str(subject.subjectName))
      return result

class Users(db.Model):
  username = db.Column(db.String, primary_key=True)
  fullname = db.Column(db.String)
  email = db.Column(db.String)
  password = db.Column(db.String)
  department = db.Column(db.String)
  authenticated = db.Column(db.Boolean, default=False)
  password_hash = db.Column(db.String(128))

  def __init__(self, username, fullname, email, password, department, authenticated):
    self.username = username
    self.fullname = fullname
    self.email = email
    self.password = password
    self.department = department
    self.authenticated = authenticated
    self.password_hash = generate_password_hash(password)

  def __repr__(self):
    return self.fullname

  def is_active(self):
    return True

  def get_id(self):
    return self.username

  def is_authenticated(self):
    return self.authenticated

  def is_anonymous(self):
    return False

  @login_manager.user_loader
  def user_loader(username):
    return Users.query.get(username)

  def check_password(self, password):
    return check_password_hash(self.password_hash, password)










