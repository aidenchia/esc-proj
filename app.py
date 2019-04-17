from flask import Flask
from flask import flash, g, redirect, render_template, url_for, request, session, abort
from flask import current_app
from flask_login import login_required, current_user, login_user,logout_user
from forms import LoginForm, RegisterForm, EditForm, StudentGroupForm, SubjectForm, RoomForm
from models import Users, Subjects, Timetable, Rooms, studentGroup
import models
from werkzeug.security import generate_password_hash
from flask_wtf.csrf import CSRFProtect
import functools
import os
import sys
import json

#scsrf = CSRFProtect()
app = Flask(__name__)
app.config.from_object(os.environ['APP_SETTINGS'])
print("[INFO] DATABASE_URL:", app.config["SQLALCHEMY_DATABASE_URI"])

from models import db, login_manager
with app.app_context():
  db.init_app(app)
  db.create_all()
  login_manager.init_app(app)
  login_manager.login_view = "login"


######################################## Wrapper for roles required #################
def Roles(included=True, *role):
    """
    if included is true, means any roles that is typed must match the user's user_group.
    
    if included is false, means any roles that is typed must NOT match the user's user_group.
    """
    def decorater(view):
        @functools.wraps(view)
        def wrapped_view(*args,**kwargs):
            #flash(str(current_user.user_group))
            #flash(str(role))
            url = request.referrer
            if current_user.is_authenticated:
                if included:
                    if current_user.user_group not in role:
                        if url is not None:
                            return redirect(url)
                        else:
                            return redirect(url_for('login'))
                else:
                    if current_user.user_group in role:
                        if url is not None:
                            return redirect(url)
                        else:
                            return redirect(url_for('login'))
            else:
                if url is not None:
                    return redirect(url)
                else:
                    return redirect(url_for('login'))
            return view(*args,**kwargs)
        return wrapped_view
    return decorater

 
########################################## ALL USERS ##########################
@app.route('/', methods=['GET', 'POST'])
def login():
  if current_user.is_authenticated:
    return redirect(url_for('home'))

  form = LoginForm()
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
    if user is not None and user.check_password(form.password.data):
        login_user(user)
        return redirect(url_for('home'))

    flash('Invalid username / password')    
    #return redirect(url_for('register'))
  return render_template('login.html', title="Sign In", form=form)

@app.route("/logout")
def logout():
  logout_user()
  session.clear()
  return redirect(url_for('login'))

@login_required
@app.route("/home")
def home():
  return render_template('home.html')


########################################## COURSE LEAD ##########################
@app.route('/courseInput', methods=['GET','POST'])
@login_required
#@Roles(True,"student")
def courseInput():
    #flash(str(current_user.user_group))
    return render_template('index.html')

@app.route("/subjectsTable", methods=['GET','POST'])
@login_required
def subjectsTable():
  try:
    inserted = Subjects.insert( 
      request.form['subjectCode'],
      request.form['term'], 
      request.form['subjectType'], 
      request.form['subjectName'])
  except:
    print("Empty fields")
    
  allSubjects = Subjects.select(all=True)
  return render_template("subjectsTable.html", allSubjects=allSubjects)

########################################## ADMIN ##########################
@app.route('/register', methods=['GET', 'POST'])
#@login_required
#@Roles(True,"admin")
def register():
  print("came herer from register")
  available_subjects = Subjects.query.all()
  print(available_subjects)
  subject_list = [(-1,'Please choose a subject')]
  for subject in available_subjects:
      subject_list.append((str(int(subject.subjectCode)),subject.subjectName))
  print(subject_list)
  form = RegisterForm()

  if form.add_more_component.data:
    form.class1.append_entry(u'default value')

  for entry in form.class1.entries:
      entry.classes.choices = subject_list
  print("came herer from register")
  print(form.class1.entries[0].classes.choices)
  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()

    if user is None:
        if form.user_group.data == -1:
            flash("Please choose a user group.")
        else:
            temp_course_table = None
            if form.user_group.data in ['2','3','4']:
                temp_course_table = {}
                for each_entry in form.class1.entries:
                    if each_entry.data['classes'] == -1:
                        print('Please select a class')
                        return render_template('register.html',form=form)
                    else:
                        temp_course_table[each_entry.data['classes']] = str(each_entry.data['cohorts'].split())
            Users.insert(form.username.data,form.password.data,
              form.fullname.data,form.email.data,dict(form.user_choices).get(form.user_group.data),
              dict(form.pillar_choices).get(form.pillar.data), form.term.data, 
              form.student_id.data,form.professor_id.data,
              str(temp_course_table))
            return redirect(url_for('usersTable'))
    else:
        if form.user_group.data == -1:
            flash("Please choose a user group.")
        else:
            temp_course_table = None
            if form.user_group.data in ['2','3','4']:
                temp_course_table = {}
                for each_entry in form.class1.entries:
                    if each_entry.data['classes'] == -1:
                        print('Please select a class')
                        return render_template('register.html',form=form)
                    else:
                        temp_course_table[each_entry.data['classes']] = str(each_entry.data['cohorts'].split())
            user.edit(form.username.data,form.password.data,
              form.fullname.data,form.email.data,dict(form.user_choices).get(form.user_group.data),
              dict(form.pillar_choices).get(form.pillar.data), form.term.data, 
              form.student_id.data,form.professor_id.data,
              str(temp_course_table),False)
            return redirect(url_for('usersTable'))
    flash('Invalid Parameters')
  print("came herer from register")
  return render_template('register.html',form=form)
  #return render_template('register.html',form=form)

@app.route("/subjects", methods=['GET', 'POST'])
@Roles(True,"admin", "course_lead", "pillar_head")
def subjects():
    available_rooms = Rooms.query.all()
    room_list = [(-1,'No Preference')]
    for room in available_rooms:
        room_list.append((room.room_id, room.location))
    form = SubjectForm()
    
    if form.add_more_component.data:
        print("came here instead")
        form.component.append_entry(u'default value')
    for entry in form.component.entries:
        entry.classroom.choices = room_list

    if form.validate_on_submit():
        print("came here")
        if form.term_no.data == '-1' or form.pillar.data == '-1' or form.subject_type.data == '-1':
            print("Please choose an option for term, pillar and subject type")
        else:
            subjectname = form.subject_name.data
            subjectid = form.subject_id.data
            termno = dict(form.terms).get(form.term_no.data)
            subjecttype = dict(form.subject_types).get(form.subject_type.data)
            pillar = form.pillar.data
            cohort_num = form.cohort_num.data
            total_enrollment = form.total_enrollment.data
            session_nums = len(form.component.entries)
            components = []
            for each_entry in form.component.entries:
                temp  = {"duration":each_entry.data['duration'],"sessionType": int(each_entry.data['session']),"classroom":each_entry.data['classroom'], 'cohorts':[]}
                if int(each_entry.data['session']) == -1:
                    print("Please choose an option for session type")
                    return render_template('subjects.html',form=form)
                if int(each_entry.data['session']) == 1:
                    for i in range(cohort_num):
                        temp['cohorts'].append(i)
                components.append(temp)
                print(temp)
            Subjects.insertSubject(subjectid,termno,subjecttype,subjectname, str(components), pillar, cohort_num, total_enrollment, session_nums)
            
        
    return render_template('subjects.html',form=form)

@app.route("/usersTable", methods=['GET', 'POST'])
@Roles(True,"admin", "course_lead", "pillar_head")
def usersTable():
  allUsers = db.session.query(Users).order_by(Users.fullname).all()
  return render_template("usersTable.html", allUsers = allUsers)

@app.route("/editUsers", methods=['GET', 'POST'])
def editUsers():
  form = EditForm()

  if form.add_more_component.data:
    form.component.append_entry(u'default value')

  if form.validate_on_submit():
    user = Users.query.filter_by(username=form.username.data).first()
 
    if user is None:
      flash('No such user')
      return render_template('editUsers.html', form=form)

    status = user.edit(username=form.username.data,
              password=form.password.data,
              fullname=form.fullname.data,
              email = form.email.data,
              user_group= dict(form.user_choices).get(form.user_group.data),
              pillar=form.pillar.data, 
              term=form.term.data, 
              student_id=form.student_id.data,
              professor_id=form.professor_id.data,
              coursetable = form.coursetable.data,
              delete=form.delete.data)

    flash(status)
    
    return redirect(url_for('usersTable'))

  return render_template("editUsers.html", form=form)
  

@login_required
@Roles(True,"admin")
def deleteUser(username):
  Users.remove(username)
  return redirect(url_for('usersTable'))

@app.route("/editStudentGroups", methods=['GET', 'POST'])
def editStudentGroups():
  print("came here in student group")
  subject_choices = [('-1','Choose the subject')]
  for subject in Subjects.query.all():
      subject_choices.append((str(int(subject.subjectCode)), subject.subjectName))
  form = StudentGroupForm()
  for entry in form.subjectFieldList.entries:
      entry.subject_choice.choices = subject_choices

  if form.validate_on_submit():
    subjects = []
    for entry in form.subjectFieldList.entries:
        if entry.data['subject_choice'] == '-1':
            flash('All choices must be filled')
            render_template('editStudentGroups.html',form=form)
        subjects.append(entry.data['subject_choice'])
    
    studentGroup.insert(pillar=form.pillar.data,
                        size=form.size.data,
                        name=form.name.data,
                        subjects=subjects,
                        term=form.term.data)

    return redirect(url_for('studentGroupTable'))

  return render_template('editStudentGroups.html',form=form)

@app.route("/studentGroupTable", methods=['GET', 'POST'])
def studentGroupTable():
  studentGroupTable = studentGroup.query.all()
  return render_template('studentGroupTable.html', studentGroupTable=studentGroupTable)

@app.route("/addRooms", methods=['GET','POST'])
def addRooms():
    form = RoomForm()
    if form.validate_on_submit():
        if form.room_type.data == '-1':
            flash("Please choose a room type.")
        else:
            print(form.room_id.data)
            Rooms.insert(str(form.room_id.data), str(form.room_name.data), int(form.room_type.data), int(form.capacity.data))
            return redirect(url_for('viewRooms'))
    
    return render_template('addRooms.html',form=form)

@app.route("/editRooms", methods=['GET','POST'])
def editRooms():
    form = RoomForm()
    if form.validate_on_submit():
        if form.room_type.data == '-1':
            flash("Please choose a room type.")
        else:
            room = Rooms.query.filter_by(location=form.room_id.data).first()
            if room is None:
                flash("Room not found. Please try again")
            else:
                room.edit(form.room_id.data, form.room_name.data, int(form.room_type.data), form.capacity.data)
                return redirect(url_for('viewRooms'))
            
    
    return render_template('addRooms.html',form=form)

@app.route('/viewRooms', methods=['GET','POST'])
def viewRooms():
    rooms = Rooms.query.all()
    return render_template('viewRooms.html',rooms=rooms)
    

######################################## STUDENTS ###############################
@login_required
def viewStudentSchedule():
    """
    A schedule contains the following information per specific class:
        subject id
        subject name
        type of session(cohort based learning, lecture, lab)
        start time to end time
        location of session(room id)
        professors teaching
    """
    Session = {2:'Lab',0:'Cohort Based Learning',1:'Lecture'}
    student_schedule = [['Monday','Tuesday','Wedneday','Thursday','Friday'],
                        ['08:30-09:00',None,None,None,None,None],
                        ['09:00-09:30',None,None,None,None,None],
                        ['09:30-10:00',None,None,None,None,None],
                        ['10:00-10:30',None,None,None,None,None],
                        ['10:30-11:00',None,None,None,None,None],
                        ['11:00-11:30',None,None,None,None,None],
                        ['11:30-12:00',None,None,None,None,None],
                        ['12:00-12:30',None,None,None,None,None],
                        ['12:30-13:00',None,None,None,None,None],
                        ['13:00-13:30',None,None,None,None,None],
                        ['13:30-14:00',None,None,None,None,None],
                        ['14:00-14:30',None,None,None,None,None],
                        ['14:30-15:00',None,None,None,None,None],
                        ['15:00-15:30',None,None,None,None,None],
                        ['15:30-16:00',None,None,None,None,None],
                        ['16:00-16:30',None,None,None,None,None],
                        ['16:30-17:00',None,None,None,None,None],
                        ['17:00-17:30',None,None,None,None,None],
                        ['17:30-18:00',None,None,None,None,None],
                        ['18:00-18:30',None,None,None,None,None],
                        ['18:30-19:00',None,None,None,None,None]]
    
    if current_user.user_group == 'student':
        user_student_group_pillar = Users.query(Users.student_group, Users.pillar).filter_by(current_user.username).all()._asdict()
        user_subjects_cohort = studentGroup.query(studentGroup.subjects, studentGroup.cohort)\
                                .filter(studentGroup.name == user_student_group_pillar['student_group'],
                                        studentGroup.pillar == user_student_group_pillar['pillar']).all()._asdict()
        subject_cohort_dict = {}
        subjects = user_subjects_cohort['subjects']
        for subject in subjects:
            subject_cohort_dict[str(subject)] = str(user_subjects_cohort['cohort'])
        user_timetable = Timetable.find_Timetable(subject_cohort_dict)
        all_professors = Users.getAllProfessors(for_scheduler=False)
        subject_ids = Subjects.select()
        for specific_class in user_timetable[user_timetable]:
            subject_id = ''
            subject_name = specific_class['subject']
            session_type = 'Lecture' if len(specific_class['session']) > 1 else 'Cohort Based Learning'
            start_to_end = student_schedule[int(specific_class['startTime'])+1][0]
            location = ''
            professors_teaching = ''
            #for subject in subject_ids:
            #    if subject.subjectName
            #class_information = subject_ids
        
    return render_template("base.html") # for now

######################################## Scheduling algorithm #################
@app.route("/genSchedule", methods=['GET', 'POST'])
def genSchedule():
  """
  Update the input.json file in algorithm folder from the database.
  runScheduler
  then, update the database with the new data.
  
  input_dict = {'professor':[],'subject':[],'classroom':[],'studentGroup':[]}
  prof_format = {'name':'','id':0,'coursetable':{}}
  subject_format = {'component':[],'pillar':0,'sessionNumber':0,'name':'','term':1,'cohortNumber':1,'totalEnrollNumber':10,'type':0,'courseId':''}
  class_format = {'name':'','location':'','id':1,'roomType':0,'capacity':10}
  studentGroup_format = {'pillar': 0, 'size': 0, 'subjects': [], 'name': '', 'cohort': 0, 'term': 1}
  
  for professor in Users.getAllProfessors():
      input_dict['professor'].append({'name':professor.fullnamell,'id':0,'coursetable':{}})
  input_dict['subject'] = Subjects.getAllSubjects()
  input_dict['classroom'] = Rooms.geAllRooms()
  input_dict['studentGroup'] = studentGroup.getAllGroups()
  
  from pathlib import Path
  data_folder = Path("algorithm/")
  file_to_open = data_folder / 'input.json'
  with open(file_to_open,'w+') as input_file:
      json.dump(input_dict, input_file)
  """
  runScheduler()
  '''
  timetablePath = os.path.join(os.getcwd(), "algorithm/timetable.json")
  with open(timetablePath, 'r') as data_file:    
    data = json.load(data_file)
  Timetable.replace_all(data)
  '''
  return redirect(url_for('viewMasterSchedule'))

@app.route("/viewMasterSchedule", methods=['GET', 'POST'])
def viewMasterSchedule():
  timetablePath = os.path.join(os.getcwd(), "algorithm/timetable.json")
  try:
    f = open(timetablePath, 'r')
    return f.read()
    
  except FileNotFoundError:
    flash('Schedule has not been generated yet')

def runScheduler():
  import subprocess

  os.chdir('./algorithm')
  subprocess.call(['javac', '-cp', 'json-20180813.jar', 
                    'Calendar.java', 'JsonUtils.java','Scheduler.java', 'Professor.java', 
                    'StudentGroup.java', 'Classroom.java', 'Subject.java'])
  subprocess.call(['java', '-cp', 'json-20180813.jar:.', 'Scheduler'])
  os.chdir('..')

def outputToDatabase():
    return
  
if __name__ == "__main__":
    app.jinja_env.cache = {}
    port = int(os.environ.get('PORT',5000))
    #csrf.init_app(app)
    app.run(host='0.0.0.0',port=port)