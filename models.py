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
  authenticated = db.Column(db.Boolean, default=False)
  password_hash = db.Column(db.String(128))
  user_group = db.Column(db.String(128))

  # Student - specific fields
  pillar = db.Column(db.String, nullable=True)
  term = db.Column(db.Integer, nullable=True)
  student_id = db.Column(db.Integer, nullable=True)
  student_group = db.Column(db.String, nullable=True)

  # Prof - specific fields


  def __init__(self, username, fullname, email, password, user_group, authenticated):
    self.username = username
    self.fullname = fullname
    self.email = email
    self.password = password
    self.user_group = user_group
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
  def load_user(username):
    return Users.query.get(username)

  def check_password(self, password):
    return check_password_hash(self.password_hash, password)

  @staticmethod
  def insert(username, fullname, email, password, user_group):
    query = Users.query.filter_by(username=username).first()
    if query is None:
      user = Users(username, fullname, email, password, user_group, False)
      db.session.add(user)
      db.session.commit()
    return None

  @staticmethod
  def remove(username):
    user = Users.query.filter_by(username=username).first()
    if user is not None:
      db.session.delete(user)
      db.session.commit()
    return None

  @staticmethod
  def edit(username=None, pillar="", term="", student_id=""):
    user = Users.query.filter_by(username=username).first()
    if user is None:
      return None

    user.pillar = pillar
    user.term = term
    user.student_id = student_id
    return None

class Timetable(db.Model):
  subject = db.Column(db.String)
  session = db.Column(db.Integer)
  weekday = db.Column(db.Integer)
  cohort = db.Column(db.String)
  startTime = db.Column(db.Integer)
  classroom = db.Column(db.String)
  
  def __init__(self, subject, session, weekday, cohort, startTime, classroom):
    self.subject = subject
    self.session = session
    self.weekday = weekday
    self.cohort = cohort
    self.startTime = startTime
    self.classroom = classroom

  def __repr__(self):
    return '{}, {}, {}, [}'.format(self.subject, self.session, self.weekday, self.cohort)

  @staticmethod
  def insert(subject, session, weekday, cohort, startTime, classroom):
      query = db.session.query(Timetable).filter_by(Timetable.subject).filter_by(Timetable.session) \
      .filter_by(Timetable.weekday).filter_by(Timetable.cohort).filter_by(Timetable.startTime).filter_by(Timetable.classroom)
      if query is None:
          specific_class = Timetable(subject, session, weekday, cohort, startTime, classroom)
          db.session.add(specific_class)
          db.session.commit()
      return None
  @staticmethod
  def replaceall(all_classes):
      db.session.query(Timetable).delete()
      db.session.commit()
  

