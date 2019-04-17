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

def test_numSubjects(client, numSubjects):
  with app.app_context():
    subjects = Subjects.query.all()
  assert len(subjects) == int(numSubjects)

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

@pytest.fixture(scope="session")
def driver_init():
  driver = webdriver.Chrome()
  driver.get("https://sutd-scheduler.herokuapp.com")
  yield driver

  driver.close()
















