from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField, SelectField, IntegerField, FloatField
from wtforms.form import Form
from wtforms.fields import FieldList, FormField
from wtforms.validators import DataRequired, EqualTo, InputRequired, ValidationError, Optional, NumberRange

def check_user_group_validator(form,field):
    if field.data == -1:
        raise ValidationError("Please select a user group")


user_choices = [('-1','Please select a user group'),('1','admin'),('2','pillar_head'),('3','course_lead'),('4','professor'),('5','student')]
pillar_choices = [('-1','Please select a pillar'),('0','HASS'),('1','ASD'),('2','EPD'),('3','ESD'),('4','ISTD'),('5','Freshmore')]


class LoginForm(FlaskForm):
  username = StringField('Username', validators=[DataRequired()])
  password = PasswordField('Password', validators=[DataRequired()])
  #remember_me = BooleanField('Remember Me', default=True)
  submit = SubmitField('Sign In')

class classForm(Form):
  classchoices = [('1', '50.003'),('2', '50.004'), ('3', '50.034')]
  classes = SelectField('Enrolled in',coerce=int,validators=[Optional()])
  cohorts = StringField('Cohorts teaching(space seperated)',validators=[Optional()])

class RegisterForm(FlaskForm):
  #user_choices = [('-1','Please select a user group'),('1','admin'),('2','pillar_head'),('3','course_lead'),('4','professor'),('5','student')]
  username = StringField('Username', validators=[DataRequired()])
  password = PasswordField('Password', validators=[InputRequired(), DataRequired(), EqualTo('confirmPassword',message='Passwords must match')])
  confirmPassword = PasswordField('Confirm Password',validators=[DataRequired()])
  fullname = StringField('Full Name', validators=[DataRequired()])
  email = StringField('Email', validators=[DataRequired()])
  user_group = SelectField('User Group',choices=user_choices,validators=[DataRequired(),check_user_group_validator])
  
  # Student - related, must allow it to be blank
  pillar = StringField('Pillar', validators=[Optional()])
  term = StringField('Term', validators=[Optional()])
  student_id = IntegerField('Student ID', validators=[Optional()])
  #student_subjects = 

  # Professor - related, must allow it to be blank
  professor_id = IntegerField('Professor ID', validators=[Optional()])
  class1 = FieldList(FormField(classForm), min_entries=1,validators=[Optional()])
  add_more_component = SubmitField("Add another Subject")

class EditForm(FlaskForm):
  #user_choices = [('-1','Please select a user group'),('1','admin'),('2','pillar_head'),('3','course_lead'),('4','professor'),('5','student')]
  username = StringField('Username', validators=[DataRequired()])

  password = PasswordField('Password', validators=[Optional()])
  fullname = StringField('Full Name', validators=[Optional()])
  email = StringField('Email', validators=[Optional()])
  user_group = SelectField('User Group',choices=user_choices, validators=[Optional()])
  
  # Student - related, must allow it to be blank
  pillar = StringField('Pillar', validators=[Optional()])
  term = StringField('Term', validators=[Optional()])
  student_id = IntegerField('Student ID', validators=[Optional()])

  # Professor - related, must allow it to be blank
  professor_id = IntegerField('Professor ID', validators=[Optional()])
  coursetable = StringField('Courses Taught', validators=[Optional()])

  add_more_component = SubmitField("Add another Subject")

  delete = BooleanField('Remove User', default=False, validators=[Optional()])
  
class componentForm(Form):
    #duration_length = [('-1','Please select a duration'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5')]
    sessionTypes = [('-1','Please select a Session Type'), ('0','Cohort Based Learning'),('1','Lecture'),('2','Lab')]
    duration = FloatField('Duration(in 0.5 increments)', validators=[DataRequired(),NumberRange(min=0.5,max=10,message="Your Class must be within 0.5 to 10 hrs long inclusive")])
    session = SelectField('Session type',choices=sessionTypes,validators=[DataRequired()])
    classroom = SelectField('Room',coerce=int,validators=[DataRequired()])

class SubjectForm(FlaskForm):
    terms = [('-1','Please select a term'),('1','1'),('2','2'),('3','3'),('4','4'),('5','5'),('6','6'),('7','7'),('8','8')]
    subject_types = [('-1','Please select a Subject Type'),('0','Core'),('1','Elective')]
    
    subject_name = StringField('Subject Name', validators=[DataRequired()])
    subject_id = IntegerField('Subject id(without the decimal point)', validators=[DataRequired()])
    term_no = SelectField('Term number',choices=terms,validators=[DataRequired()])
    component = FieldList(FormField(componentForm),min_entries=1,validators=[DataRequired()])
    add_more_component = SubmitField("Add another component")   
    pillar = SelectField('Pillar',choices=pillar_choices,validators=[DataRequired()])
    subject_type = SelectField('Subject Type',choices=subject_types,validators=[DataRequired()])
    cohort_num = IntegerField('Number of Cohorts',validators=[DataRequired(),NumberRange(min=1, max=10, message='Number of cohorts must be between 1 and 10 inclusive')])
    total_enrollment = IntegerField('Total Enrollment',validators=[DataRequired(),NumberRange(min=10, max=500, message='Total Enrollments must be between 10 and 500 inclusive')])
    
class RoomForm(FlaskForm):
    roomtypes = [('-1','Please select a room type'),('0','Cohort Classroom/Think Tank'),('1','Lecture Theatre'),('2','Lab')]
    room_name = StringField("Room Name",validators=[DataRequired()])
    room_id = StringField('Room Id',validators=[DataRequired()])
    room_type = SelectField("Room Type",choices=roomtypes,validators=[DataRequired()])
    capacity = IntegerField("Room Capacity",validators=[DataRequired()])

class subjectSelectForm(Form):
    subject_choice = SelectField('Subject',coerce=int,validators=[DataRequired()])

class StudentGroupForm(FlaskForm):
  pillar = SelectField('Pillar',choices=pillar_choices,validators=[DataRequired()])
  size = IntegerField('Size', validators=[DataRequired(), NumberRange(min=5, max=50, message="Student group size must be between 5 and 50 inclusive")])
  cohort = IntegerField('Cohort', validators=[DataRequired()])
  term = IntegerField('Term', validators=[DataRequired()])
  name = StringField('Name', validators=[DataRequired()])
  subjectFieldList = FieldList(FormField(subjectSelectForm),min_entries=4,validators=[DataRequired()])


