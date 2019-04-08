from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField, SelectField, IntegerField
from wtforms.validators import DataRequired, EqualTo, InputRequired, ValidationError

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
  #user_group = StringField('User Group' ,validators=[DataRequired()])

  pillar = StringField('Pillar')
  term = StringField('Term')
  student_id = IntegerField('Student ID')
