import pytest

def pytest_addoption(parser):
  parser.addoption("--numSubjects", action="store", default=7, help="Number of Subjects")

def pytest_generate_tests(metafunc):
  if 'numSubjects' in metafunc.fixturenames:
    metafunc.parametrize("numSubjects", metafunc.config.getoption('numSubjects'))
 