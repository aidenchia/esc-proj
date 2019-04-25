from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager
from werkzeug.security import generate_password_hash, check_password_hash
import time
import ast
import re

db = SQLAlchemy()
login_manager = LoginManager()
login_manager.login_view = 'login'

class Subjects(db.Model):
  subjectCode = db.Column(db.Float, primary_key=True) # primary keys are unique identifiers
  term = db.Column(db.Integer, nullable=True)
  subjectType = db.Column(db.Text, nullable=True)
  subjectName = db.Column(db.Text, nullable=True)
  components = db.Column(db.String)
  pillar = db.Column(db.Integer)
  cohortnum = db.Column(db.Integer)
  totalenrollment = db.Column(db.Integer)
  sessionnum = db.Column(db.Integer)

  def __init__(self, subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
    self.subjectCode = subjectCode
    self.term = term
    self.subjectType = subjectType
    self.subjectName = subjectName
    self.components = components
    self.pillar = pillar
    self.cohortnum = cohortnum
    self.totalenrollment = totalenrollment
    self.sessionnum = sessionnum

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

  @staticmethod
  def insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
      subject = db.session.query(Subjects).filter_by(subjectCode=subjectCode).first()
      if subject is not None:
          subject.subjectCode = subjectCode
          subject.term = term
          subject.subjectType = subjectType
          subject.subjectName = subjectName
          subject.components = components
          subject.pillar = pillar
          subject.cohortnum = cohortnum
          subject.totalenrollment = totalenrollment
          subject.sessionnum = sessionnum
      else:
          subject = Subjects(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum)
          db.session.add(subject)
      db.session.commit()
      return None

  def edit(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
    if Subjects.checkSubjectType(subjectType):
      self.subjectType = subjectType
      db.session.commit()  


  @staticmethod
  def checkSubjectType(subjectType):
    if str(subjectType).lower() not in ['core', 'elective']:
      return False
    else:
      return True
    
  @staticmethod
  def getAllSubjects():
      query = db.session.query(Subjects).all()
      #subject_format = {'component':[],'pillar':0,'sessionNumber':0,'name':'','term':1,'cohortNumber':1,'totalEnrollNumber':10,'type':0,'courseId':''}
      all_subjects = []
      subject_type = {'Core':0,'Elective':1}
      for subject in query:
          if subject.pillar == 0:
              continue
          all_subjects.append({'component':ast.literal_eval(subject.components),
                               'pillar':subject.pillar,
                               'sessionNumber':subject.sessionnum,
                               'name':subject.subjectName,
                               'term':subject.term,
                               'cohortNumber':subject.cohortnum,
                               'totalEnrollNumber':subject.totalenrollment,
                               'type':subject_type[subject.subjectType],
                               'courseId':str(int(subject.subjectCode))})
      #all_subjects = [subject._asdict() for subject in query]
      return all_subjects

  @staticmethod
  def remove(subjectCode):
    subject = Subjects.query.filter_by(subjectCode=subjectCode).first()
    if subject is not None:
      db.session.delete(subject)
      db.session.commit()
    return None

      

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
  #subject = db.Column(db.String, nullable=True)
  
  # Prof - specific fields
  professor_id = db.Column(db.Integer, nullable=True)
  coursetable = db.Column(db.String, nullable=True) # "['50.004': [0, 1]]"



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
  def insert(username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable):
    query = Users.query.filter_by(username=username).first()
    if query is None:
      user = Users(username, fullname, email, password, user_group, False)
      db.session.add(user)
      db.session.commit()
      user.edit(username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, False)
    else:
      user = query
      user.edit(username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, False)
    return None

  @staticmethod
  def remove(username):
    user = Users.query.filter_by(username=username).first()
    if user is not None:
      db.session.delete(user)
      db.session.commit()
    return None

  def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, delete):
    if delete:
      Users.remove(username)
      return username + " removed"

    if Users.checkPillar(pillar):
      self.pillar = pillar.upper()

    if Users.checkEmail(email):
      self.email = email

    if Users.checkUserGroup(user_group):
      self.user_group = user_group

    if Users.checkFullName(fullname):
      self.fullname = fullname

    if password != "": self.password_hash = generate_password_hash(password) 
    if term != "": self.term = term 
    if student_id != "": self.student_id = student_id
    if student_group != "": self.student_group = student_group
    if professor_id != "": self.professor_id = professor_id
    if coursetable != "": self.coursetable = coursetable
    db.session.commit()

  @staticmethod
  def checkPillar(pillar):
    if str(pillar).upper() not in ['ISTD', 'ESD', 'HASS', 'ASD', 'EPD']:
      return False
    else:
      return True

  @staticmethod
  def checkUserGroup(user_group):
    if user_group.lower() not in ['student', 'professor', 'pillar_head', 'admin', 'subject_lead']:
      return False
    else:
      return True

  @staticmethod
  def checkEmail(email):
    if "@mymail.sutd.edu.sg" not in email or not bool(re.match(r'[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+', email)):
      return False
    else:
      return True

  @staticmethod
  def checkFullName(fullname):
    return fullname.isalpha()

  @staticmethod
  def getAllProfessors(for_scheduler=True):
      if for_scheduler:
          query = Users.query.with_entities(Users.fullname, Users.professor_id, Users.coursetable).filter_by(user_group="professor").all()
      else:
          query = Users.query.filter_by(user_group="professor").all()
      #all_professors = [professor._asdict() for professor in query]
      return query

class Timetable(db.Model):
  timetable_id = db.Column(db.Integer, primary_key=True)
  subject = db.Column(db.String)
  session = db.Column(db.Integer)
  weekday = db.Column(db.Integer)
  cohort = db.Column(db.String)
  startTime = db.Column(db.Integer)
  classroom = db.Column(db.String)
  duration = db.Column(db.Float)
  professor = db.Column(db.String)
  studentgroup = db.Column(db.String)
  
  def __init__(self, subject, session, weekday, cohort, startTime, classroom,duration,professor,studentgroup):
    self.subject = subject
    self.session = session
    self.weekday = weekday
    self.cohort = cohort
    self.startTime = startTime
    self.classroom = classroom
    self.duration = duration
    self.professor = professor
    self.studentgroup = studentgroup

  def __repr__(self):
    return '{}, {}, {}, {}}'.format(self.subject, self.session, self.weekday, self.cohort)
  
  @staticmethod
  def row2dict(row):
    d = {}
    for column in row.__table__.columns:
        d[column.name] = str(getattr(row, column.name))

    return d

  @staticmethod
  def insert(subject, session, weekday, cohort, startTime, classroom,duration,professor,studentgroup):
      query = Timetable.query.filter_by(subject).filter_by(session) \
      .filter_by(weekday).filter_by(cohort).filter_by(startTime)\
      .filter_by(classroom).filter_by(duration).filter_by(professor).filter_by(studentgroup).first()
      if query is None:
          specific_class = Timetable(subject, session, weekday, cohort, startTime, classroom,duration,professor,studentgroup)
          db.session.add(specific_class)
          db.session.commit()
      return None

  @staticmethod
  def replace_all(all_classes): 
      db.session.query(Timetable).delete()
      db.session.commit()

      for specific_class in list(all_classes.values())[0]:
          sc_subject = specific_class['subject']
          sc_session = specific_class['session']
          sc_weekday = specific_class['weekday']
          sc_cohort = str(specific_class['cohort'])
          sc_startTime = specific_class['startTime']
          sc_classroom = specific_class['classroom']
          sc_duration = specific_class['duration']
          sc_professor = str(specific_class['professor'])
          sc_studentgroup = str(specific_class['studentgroup'])
          specific_class = Timetable(sc_subject,sc_session,sc_weekday,sc_cohort,sc_startTime,sc_classroom,sc_duration,sc_professor,sc_studentgroup)
          db.session.add(specific_class)
          db.session.commit()
      return None

  @staticmethod
  def find_Timetable(subject_cohort_dict):
      user_timetable = {"user_timetable":[]}
      for subject,cohort in subject_cohort_dict.items():
          subject_classes = Timetable.query.filter_by(subject).all()
          for each_class in subject_classes:
              if cohort in list(each_class.cohort):
                  cohortdict = Timetable.row2dict(each_class)
                  user_timetable.get("user_timetable").append(cohortdict)
      return user_timetable
  
class Rooms(db.Model):
  room_id = db.Column(db.Integer, primary_key=True)
  location = db.Column(db.String)
  name = db.Column(db.String)
  roomType = db.Column(db.Integer)
  capacity = db.Column(db.Integer)
  
  def __init__(self, location, name, roomType, capacity):
    self.location = location
    self.name = name
    self.roomType = roomType
    self.capacity = capacity

  def __repr__(self):
      return '{}, {}, {}, {}'.format(self.location, self.name, self.roomType, self.capacity)
  
  def edit(self, location, name, roomType, capacity):
      
    if name != "": self.name = name
    if location != "": self.location = location 
    if roomType != 'Please select a room type': self.roomType = roomType
    if capacity != "": self.capacity = capacity
    db.session.commit()

  @staticmethod
  def insert(location, name, roomType, capacity):
      print(location)
      query = Rooms.query.filter_by(location=location).first()
      print(query)
      if query is None:
          room = Rooms(location, name, roomType, capacity)
          print(room)
          db.session.add(room)
          db.session.commit()
      return None
  
  @staticmethod
  def delete(Specific_location):
      query = Rooms.query.filter_by(location=Specific_location).first()
      db.session.delete(query)
      db.session.commit()
  
  @staticmethod
  def getroom(Specific_location):
      query = Rooms.query.filter_by(location=Specific_location).first()
      if query is not None:
          return query.all()._asdict()
      return None

  @staticmethod
  def geAllRooms():
      query = Rooms.query.all()
      all_rooms = []
      #class_format = {'name':'','location':'','id':1,'roomType':0,'capacity':10}
      for room in query:
          all_rooms.append({'name':room.name,
                            'location':room.location,
                            'id':room.room_id,
                            'roomType':room.roomType,
                            'capacity':room.capacity})
      #all_rooms = [room.__dict__ for room in query]
      return all_rooms

class studentGroup(db.Model):
    sg_id = db.Column(db.Integer, primary_key=True)
    pillar = db.Column(db.Integer)
    size = db.Column(db.Integer)
    subjects = db.Column(db.String) #a string representation of a list e.g. [50003, 50005, 50034]
    name = db.Column(db.String)
    cohort = db.Column(db.Integer)
    term = db.Column(db.Integer)

    def __init__(self, pillar, size, subjects, name, cohort, term):
        self.pillar = pillar
        self.size = size
        self.subjects = subjects
        self.name = name
        self.cohort = cohort
        self.term = term
    
    def __repr__(self):
      return '{}, {}, {}, {}, {}, {}'.format(self.pillar, self.size, self.subjects, self.name, self.cohort)

    @staticmethod
    def insert(pillar, size, subjects, name, cohort, term):
        query = studentGroup.query.filter_by(pillar=pillar).all()
        #query = studentGroup.query.filter(studentGroup.pillar==pillar, studentGroup.name==name)
        if query is None:
            newgroup = studentGroup(pillar, size, subjects, name, cohort, term)
            db.session.add(newgroup)
            db.session.commit()
        else:
          for q in query:
            if q.name == name:
              return None

          newgroup = studentGroup(pillar, size, subjects, name, cohort, term)
          db.session.add(newgroup)
          db.session.commit()

        return None
    
    @staticmethod
    def delete(pillar, name):
        query = studentGroup.query.filter_by(pillar).filter_by(name)
        if query is not None:
            db.session.delete(query)
            db.session.commit()
    
    @staticmethod
    def getGroup(pillar, name):
        query = studentGroup.query.filter(studentGroup.name==name,studentGroup.pillar==pillar)
        if query is not None:
            return query.all()._asdict()
    
    @staticmethod
    def getAllGroups():
        query = studentGroup.query.all()
        studentGroup_format = {'pillar': 0, 'size': 0, 'subjects': [], 'name': '', 'cohort': 0, 'term': 1, 'id':0}
        all_groups = []
        for group in query:
            all_groups.append({'pillar': group.pillar,
                               'size': group.size,
                               'subjects': ast.literal_eval(group.subjects),
                               'name': group.name,
                               'cohort': group.cohort,
                               'term': group.term,
                               'id': group.sg_id})
        #all_groups = [group._asdict() for group in query]
        return all_groups
        

class Requests(db.Model):
  request_id = db.Column(db.Integer, primary_key=True)
  requestee = db.Column(db.String)
  room = db.Column(db.String)
  day = db.Column(db.String)
  time = db.Column(db.String)
  date_created = db.Column(db.DateTime)
  approved = db.Column(db.Boolean, default=False)

  def __init__(self, requestee, room, day, time, date_created):
    self.room = room
    self.requestee = requestee
    self.day = day 
    self.time = time 
    self.date_created = date_created

  @staticmethod
  def insert(requestee, room, day, time):
    import datetime
    date_created = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
    newrequest = Requests(requestee, room, day, time, date_created)
    db.session.add(newrequest)
    db.session.commit()
    return None 

  @staticmethod
  def edit(request_id):
    request = Requests.query.filter_by(request_id=request_id).first()
    request.approved = True
    db.session.commit()















