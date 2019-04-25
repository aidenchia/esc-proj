import os
import pytest

from app import app
from models import db, Users, Subjects
from config import TestingConfig
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys

@pytest.fixture
def client():
  app.config.from_object(TestingConfig)
  #print('[INFO] DATABASE_URL:', app.config["SQLALCHEMY_DATABASE_URI"])
  client = app.test_client()
  yield client

########################################## USERS ##########################

def test_init_user(client): # check if all fields are initialized properly
  from werkzeug.security import generate_password_hash, check_password_hash
  user = Users('aidenchia', 'Aiden Chia', 'aiden_chia@mymail.sutd.edu.sg', 'password', 'student', False)
  assert user.username == 'aidenchia'
  assert user.fullname == 'Aiden Chia'
  assert user.email == 'aiden_chia@mymail.sutd.edu.sg'
  assert user.password == 'password'
  assert user.user_group == 'student'
  assert user.authenticated == False
  assert user.check_password('password')

def test_insert_user(client): 
  with app.app_context():
    #insert(username, password, fullname, email, user_group, pillar, term, student_id, professor_id, coursetable)
    Users.insert(username='aidenchia', password='password', fullname='Aiden Chia', 
                 email='aiden_chia@mymail.sutd.edu.sg', user_group=None, 
                 pillar=None, term=None, student_id=None, professor_id=None, coursetable=None)
    query = Users.query.filter_by(username='aidenchia').all()
  assert len(query) == 1


def test_edit_user(client):
  with app.app_context():
    #   def edit(self, username, password, fullname, email, user_group, pillar, term, student_id, professor_id, coursetable, delete):
    user = Users.query.filter_by(username='aidenchia').first()
    user.edit(username='testing', password="", fullname="", email="", user_group="", pillar="", term="", student_id="", professor_id="", coursetable="", delete)

def test_remove_user(client):
  with app.app_context():
    Users.remove('aidenchia')
    query = Users.query.filter_by(username='aidenchia').all()
  assert len(query) == 0



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
  assert len(subjects) == int(numSubjects)


def test_insert_subject(client, numSubjects):
  with app.app_context():
    # insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
    Subjects.insertSubject(10.000, 5, "Core", "Test Subject", "[]", 1, 1, 1, 1)
    subjects = Subjects.query.all()
    assert len(subjects) == int(numSubjects) + 1


def test_remove_subject(client, numSubjects):
    with app.app_context():
    # insertSubject(subjectCode, term, subjectType, subjectName, components, pillar, cohortnum, totalenrollment, sessionnum):
      Subjects.remove(10.000)
      subjects = Subjects.query.all()
      assert len(subjects) == int(numSubjects)


















