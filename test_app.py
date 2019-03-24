import os
import pytest

from app import app
from models import db, Users, Subjects
from config import TestingConfig

@pytest.fixture
def client():
  app.config.from_object(TestingConfig)
  print('[INFO] DATABASE_URL:', app.config["SQLALCHEMY_DATABASE_URI"])
  client = app.test_client()
  yield client

def login(client, username, password):
  return client.post('/login', data=dict(username=username, password=password), follow_redirects=True)

def test_app(client):
  with app.test_request_context('login'):
    response = client.get('login')
  assert response.status_code == 200 # http status code 200 = OK

def test_db(client, numSubjects):
  with app.app_context():
    subjects = Subjects.query.all()
  assert len(subjects) == int(numSubjects)

'''
def test_login(client):
  rv = login(client, 'felicePeck', 'password')
  assert b'You were logged in' in rv.data
'''

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
    Users.insert('aidenchia', 'Aiden Chia', 'aiden_chia@mymail.sutd.edu.sg', 'password', 'student')
    query = Users.query.filter_by(username='aidenchia').all()
  assert len(query) == 1

def test_remove_user(client):
  with app.app_context():
    Users.remove('aidenchia')
    query = Users.query.filter_by(username='aidenchia').all()
  assert len(query) == 0













