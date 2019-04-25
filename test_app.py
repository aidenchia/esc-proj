import os
import pytest

from app import app
from models import db, Users, Subjects
from config import TestingConfig
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
import random
import string

@pytest.fixture
def client():
  app.config.from_object(TestingConfig)
  #print('[INFO] DATABASE_URL:', app.config["SQLALCHEMY_DATABASE_URI"])
  client = app.test_client()
  yield client

########################################## USERS ##########################

def test_init_user(client): # check if all fields are initialized properly
  #_init__(self, username, fullname, email, password, user_group, authenticated):
  from werkzeug.security import generate_password_hash, check_password_hash
  username = 'aidenchia'
  fullname = 'Aiden Chia'
  email = 'aiden_chia@mymail.sutd.edu.sg'
  password = 'password'
  user_group = 'student'
  user = Users(username, fullname, email, password, user_group, False)

  print("\n[INFO] Initializing new user with following fields:")
  print("[INFO] Username: {}".format(user.username))
  print("[INFO] Email: {}".format(user.email))
  print("[INFO] Password: {}".format(user.password))
  print("[INFO] User Group: {}".format(user.user_group))
  print("[INFO] Checking hash({}) == {}".format(user.password, generate_password_hash(password)))

  assert user.username == username
  assert user.fullname == fullname
  assert user.email == email
  assert user.password == password
  assert user.user_group == user_group
  assert user.authenticated == False
  assert user.check_password(password)

def test_insert_user(client): 
  with app.app_context():
    #insert(username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable)
    numUsers = len(Users.query.all())
    print("\n[INFO] Inserting user, database should contain user & no. users should ++1")
    print("[INFO] No. of users before insert: {}".format(numUsers))

    username = 'aidenchia'
    fullname = 'Aiden Chia'
    email = 'aiden_chia@mymail.sutd.edu.sg'
    password = 'password'
    user_group = 'student'

    Users.insert(username=username, password=password, fullname=fullname, 
                 email=email, user_group=user_group, 
                 pillar=None, term=None, student_id=None, student_group=None, professor_id=None, coursetable=None)
    new_numUsers = len(Users.query.all())
    print("[INFO] No. of users after insert: {}".format(new_numUsers))
    query = Users.query.filter_by(username='aidenchia').all()
    assert len(query) == 1
    assert numUsers + 1 == new_numUsers


def test_edit_user_fullname(client):
  with app.app_context():
    #   def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, delete):
    user = Users.query.filter_by(username='aidenchia').first()
    current_fullname = user.fullname
    new_fullname = randomString()
    print("\n[INFO] Current fullname: {}".format(current_fullname))
    print("[INFO] Trying to change fullname to: {}".format(new_fullname))
    user.edit(username='aidenchia', password="", fullname=new_fullname, email="", user_group="", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] New fullname: {}".format(new_fullname))
    user = Users.query.filter_by(username='aidenchia').first()
    assert user.fullname == new_fullname


def test_edit_user_fullname_with_badname(client):
  import copy
  with app.app_context():
    user = Users.query.filter_by(username='aidenchia').first()
    current_fullname = user.fullname 
    new_fullname = copy.deepcopy(current_fullname) + "!"
    print("\n[INFO] Current fullname: {}".format(current_fullname))

    print("[INFO] Trying to change fullname to: {}".format(new_fullname))
    user.edit(username='aidenchia', password="", fullname=new_fullname, email="", user_group="", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    
    print("[INFO] Fullname after attempted insert: {}".format(user.fullname))
    user = Users.query.filter_by(username='aidenchia').first()

    assert user.fullname != new_fullname


def test_edit_user_email(client):
  with app.app_context():
    #   def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, delete):
    user = Users.query.filter_by(username='aidenchia').first()
    current_email = user.email
    print("\n[INFO] Current email: {}".format(current_email))

    new_email = "TEST@mymail.sutd.edu.sg"
    print("[INFO] Trying to change email to: {}".format(new_email))

    user.edit(username='aidenchia', password="", fullname="", email=new_email, user_group="", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] New email: {}".format(user.email))
    assert current_email != user.email
    assert user.email == new_email

def test_edit_user_email_with_bad_address(client):
  with app.app_context():
    user = Users.query.filter_by(username='aidenchia').first()

    current_email = user.email
    print("\n[INFO] Current email: {}".format(current_email))

    extension = "mymail.sutd.edu.sg"
    bad_email = "TEST" + "@" + randomBitFlip(extension)
    print("[INFO] Trying to edit email to: {}".format(bad_email))

    user.edit(username='aidenchia', password="", fullname="", email=bad_email, user_group="", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] Email after attempted insert: {}".format(user.email))
    assert user.email != bad_email
    assert user.email == current_email

    bad_email_2 = "TEST!" + "@" + extension
    print("[INFO] Trying to edit email to: {}".format(bad_email_2))
    user.edit(username='aidenchia', password="", fullname="", email=bad_email_2, user_group="", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] Email after attempted insert: {}".format(user.email))
    assert user.email != bad_email_2
    assert user.email == current_email


def randomBitFlip(hexstring):
  b = bytearray(hexstring.encode())
  index = random.randint(0, len(b) - 1)
  b[index] = b[index] ^ 4
  return b.decode()

def test_edit_user_user_group(client):
  with app.app_context():
    #   def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, delete):
    user = Users.query.filter_by(username='aidenchia').first()
    print("\n[INFO] Current user group: {}".format(user.user_group))

    new_user_group = "professor"
    print("[INFO] Trying to change to: {}".format(new_user_group))

    user.edit(username='aidenchia', password="", fullname="", email="", user_group="professor", pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] New user group: {}".format(user.user_group))
    user = Users.query.filter_by(username='aidenchia').first()
    assert user.user_group == "professor"

def test_edit_user_bad_user_group(client):
  with app.app_context():
    user = Users.query.filter_by(username='aidenchia').first()
    print("\n[INFO] Current user group: {}".format(user.user_group))

    new_user_group = randomBitFlip("professor")
    print("[INFO] Trying to change to: {}".format(new_user_group))

    user.edit(username='aidenchia', password="", fullname="", email="", user_group=new_user_group, pillar="", term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] User group: {}".format(user.user_group))
    assert user.user_group != new_user_group

def test_edit_user_pillar(client):
  with app.app_context():
    #   def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, student_group, professor_id, coursetable, delete):
    user = Users.query.filter_by(username='aidenchia').first()
    print("\n[INFO] Current pillar: {}".format(user.pillar))

    new_pillar = "ESD"
    user.edit(username='aidenchia', password="", fullname="", email="", user_group="", pillar=new_pillar, term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("[INFO] Trying to change to: {}".format("ESD"))
    print("[INFO] New pillar: {}".format(user.pillar))

    assert user.pillar == new_pillar

def test_edit_user_pillar_with_number(client):
  with app.app_context():
    user = Users.query.filter_by(username='aidenchia').first()
    rand_num = random.randint(1,10)
    user.edit(username='aidenchia', password="", fullname="", email="", user_group="", pillar=rand_num, term="", student_id="", student_group="", professor_id="", coursetable="", delete=False)
    print("\n[INFO] Trying to insert integer into pillar field: {}".format(rand_num))
    print("[INFO] Current pillar: {}".format(user.pillar))
    assert user.pillar != str(100)
'''
def test_edit_user_term(client):
  with app.app_context():
    user = Users.query.filter_by(username='aidenchia').first()
    rand_num = random.randint(10,21)
'''



def test_remove_user(client):
  with app.app_context():
    numUsers = len(Users.query.all())
    print("\n[INFO] Current no. of users: {}".format(numUsers))
    Users.remove('aidenchia')
    new_numUsers = len(Users.query.all())
    print("[INFO] New no. of users: {}".format(new_numUsers))
    query = Users.query.filter_by(username='aidenchia').all()
    assert len(query) == 0
    assert new_numUsers == numUsers - 1


def randomString(stringLength=10):
  letters = string.ascii_lowercase
  return ''.join(random.choice(letters) for i in range(stringLength))


def test_query_random_username(client):
  with app.app_context():
    rand_username = randomString(10)
    print("\n[INFO] Trying to query username: {}".format(rand_username))
    user = Users.query.filter_by(username=rand_username).all()
    print("[INFO] No. of users returned: {}".format(len(user)))
    assert len(user) == 0


########################################## SUBJECTS ##########################

'''
  subjectCode = db.Column(db.Float, primary_key=True) # primary keys are unique identifiers
  term = db.Column(db.Integer, nullable=True)
  subjectType = db.Column(db.Text, nullable=True)
  subjectName = db.Column(db.Text, nullable=True)
  components = db.Column(db.String)
  pillar = db.Column(db.Integer)
  cohortnum = db.Column(db.Integer)
  totalenrollment = db.Column(db.Integer)
  sessionnum = db.Column(db.Integer)
'''

def test_numSubjects(client, numSubjects):
  with app.app_context():
    subjects = Subjects.query.all()
    print("\n[INFO] Current no. of Subjects: {}".format(len(subjects)))
    print("[INFO] Known no. of Subjects: {}".format(numSubjects))
    assert len(subjects) == int(numSubjects)


def test_insert_subject(client, numSubjects):
  with app.app_context():
    # insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
    numSubjects = len(Subjects.query.all())

    subjectCode = 50.999
    term = 5
    subjectType = "Core"
    subjectName = "Test Subject"
    components = "[]"
    pillar = 1
    cohortnum = 1
    totalenrollment = 1
    sessionnum = 1

    Subjects.insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum)

    print("\n[INFO] Inserting new subject with following fields:")
    print("[INFO] Subject Code: {}".format(subjectCode))
    print("[INFO] Term: {}".format(term))
    print("[INFO] Subject Type: {}".format(subjectType))
    print("[INFO] Components: {}".format(components))
    print("[INFO] Pillar: {}".format(pillar))
    print("[INFO] Cohort No.: {}".format(cohortnum))
    print("[INFO] Total Enrollment: {}".format(totalenrollment))
    print("[INFO] Session No.: {}".format(sessionnum))

    new_numSubjects = len(Subjects.query.all())
    print("[INFO] Current no. of Subjects: {}".format(numSubjects))
    print("[INFO] No. of Subjects after insert: {}".format(new_numSubjects))
    subjects = Subjects.query.all()
    assert len(subjects) == int(numSubjects) + 1


def test_edit_subject(client):
  with app.app_context():
    new_subjectType = randomString()
    subject = Subjects.query.filter_by(subjectCode=50.999).first()
    subject.edit(term=5, subjectType=new_subjectType, subjectName="Test Subject", components="[]", pillar=1, cohortnum=1, totalenrollment=1, sessionnum=1)
    assert subject.subjectType != new_subjectType


def test_remove_subject(client, numSubjects):
    with app.app_context():
    # insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
      Subjects.remove(50.999)
      subjects = Subjects.query.all()
      assert len(subjects) == int(numSubjects)














