from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager
from werkzeug.security import generate_password_hash, check_password_hash
import time


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

  def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, delete):
    if delete:
      Users.remove(username)
      return username + " removed"

    if password != "": self.password_hash = generate_password_hash(password) 
    if fullname != "": self.fullname = fullname 
    if email != "": self.email = email
    if user_group != 'Please select a user group': self.user_group = user_group 
    if pillar != "": self.pillar = pillar 
    if term != "": self.term = term 
    if student_id != "": self.student_id = student_id 
    db.session.commit()

class Timetable(db.Model):
  subject = db.Column(db.String, primary_key=True)
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
  def row2dict(row):
    d = {}
    for column in row.__table__.columns:
        d[column.name] = str(getattr(row, column.name))

    return d

  @staticmethod
  def insert(subject, session, weekday, cohort, startTime, classroom):
      query = Timetable.query.filter_by(subject).filter_by(session) \
      .filter_by(weekday).filter_by(cohort).filter_by(startTime).filter_by(classroom).first()
      if query is None:
          specific_class = Timetable(subject, session, weekday, cohort, startTime, classroom)
          db.session.add(specific_class)
          db.session.commit()
      return None

  @staticmethod
  def delete(row): # row either a timetable object or the string "all"
    if row == "all":
      db.session.query(Timetable).delete()
      db.session.commit()
<<<<<<< HEAD

    else:
      db.session.query(Timetable).delete(row)
      db.session.commit()
=======
      for specific_class in all_classes.values():
          sc_subject = specific_class['subject']
          sc_session = specific_class['session']
          sc_weekday = specific_class['weekday']
          sc_cohort = str(specific_class['cohort'])
          sc_startTime = specific_class['startTime']
          sc_classroom = specific_class['classroom']
          Timetable.insert(sc_subject,sc_session,sc_weekday,sc_cohort,sc_startTime,sc_classroom)
  @staticmethod
  def find_Timetable(subject_cohort_list):
      user_timetable = {"user_timetable":[]}
      for subject,cohort in subject_cohort_list.items():
          subject_classes = db.session.query(Timetable).filter_by(subject).all()
          for each_class in subject_classes:
              if cohort in list(each_class.cohort):
                  cohortdict = Timetable.row2dict(each_class)
                  user_timetable.get("user_timetable").append(cohortdict)
      return user_timetable
                  
              
      
>>>>>>> 4f5cb0a340f894448cb1783dc9fe8c52b5e826d1
  


