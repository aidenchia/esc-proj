import os
import tempfile
import pytest

from app import app
from models import db, Users, Subjects
from config import TestingConfig

@pytest.fixture
def client():
  db_fd, app.config['DATABASE'] = tempfile.mkstemp()
  app.config['TESTING'] = True
  app.config.from_object(TestingConfig)
  client = app.test_client()
  with app.app_context():
    db.init_app(app)
    db.create_all()

  yield client

  os.close(db_fd)
  os.unlink(app.config['DATABASE'])

def login(client, username, password):
  return client.post('/login', data=dict(username=username, password=password), follow_redirects=True)

def test_db(client, numSubjects):
  with app.app_context():
    subjects = Subjects.query.all()
  assert len(subjects) == int(numSubjects)






