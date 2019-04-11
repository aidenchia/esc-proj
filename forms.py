from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField, SelectField, IntegerField, FloatField
from wtforms.form import Form
from wtforms.fields import FieldList, FormField
from wtforms.validators import DataRequired, EqualTo, InputRequired, ValidationError, Optional, NumberRange

def check_user_group_validator(form,field):
    if field.data == -1:
        raise ValidationError("Please select a user group")

class LoginForm(FlaskForm):
  username = StringField('Username', validators=[DataRequired()])
  password = PasswordField('Password', validators=[DataRequired()])
  #remember_me = BooleanField('Remember Me', default=True)
  submit = SubmitField('Sign In')

class RegisterForm(FlaskForm):
  user_choices = [('-1','Please select a user group'),('1','admin'),('2','pillar_head'),('3','course_lead'),('4','professor'),('5','student')]
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

class EditForm(FlaskForm):
  user_choices = [('-1','Please select a user group'),('1','admin'),('2','pillar_head'),('3','course_lead'),('4','professor'),('5','student')]
  username = StringField('Username', validators=[DataRequired()])

  password = PasswordField('Password', validators=[Optional()])
  fullname = StringField('Full Name', validators=[Optional()])
  email = StringField('Email', validators=[Optional()])
  user_group = SelectField('User Group',choices=user_choices, validators=[Optional()])
  
  pillar = StringField('Pillar', validators=[Optional()])
  term = StringField('Term', validators=[Optional()])
  student_id = IntegerField('Student ID', validators=[Optional()])

  delete = BooleanField('Remove User', default=False, validators=[Optional()])
  
class componentForm(Form):
    #duration_length = [('-1','Please select a duration'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5'),('0.5','0.5')]
    sessionTypes = [('-1','Please select a Session Type'), ('0','Cohort Based Learning'),('1','Lecture'),('2','Lab')]
    duration = FloatField('Duration(in 0.5 increments)', validators=[DataRequired(),NumberRange(min=0.5,max=11,message="Your Class must be within 0.5 to 11 hrs long inclusive")])
    session = SelectField('Session type',choices=sessionTypes,validators=[DataRequired()])
    classroom = SelectField('Room',coerce=int,validators=[DataRequired()])

class SubjectForm(FlaskForm):
    terms = [('-1','Please select a term'),('1','1'),('2','2'),('3','3'),('4','4'),('5','5'),('6','6'),('7','7'),('8','8')]
    pillar_choices = [('-1','Please select a pillar'),('0','HASS'),('1','EPD'),('2','ASD'),('3','ESD'),('4','ISTD')]
    subject_name = StringField('Subject Name', validators=[DataRequired()])
    subject_id = IntegerField('Subject id(without the decimal point)', validators=[DataRequired()])
    term_no = SelectField('Term number',choices=terms,validators=[DataRequired()])
    component = FieldList(FormField(componentForm),min_entries=1,validators=[DataRequired()])
    pillar = SelectField('Pillar',choices=pillar_choices,validators=[DataRequired()])
    total
    
