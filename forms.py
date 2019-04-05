from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField
from wtforms.validators import DataRequired, EqualTo, InputRequired

class LoginForm(FlaskForm):
  username = StringField('Username', validators=[DataRequired()])
  password = PasswordField('Password', validators=[DataRequired()])
  remember_me = BooleanField('Remember Me', default=True)
  submit = SubmitField('Sign In')

class RegisterForm(FlaskForm):
  username = StringField('Username', validators=[DataRequired()])
  password = PasswordField('Password', validators=[InputRequired(), DataRequired(), EqualTo('confirmPassword',message='Passwords must match')])
  confirmPassword = PasswordField('Confirm Password',validators=[DataRequired()])
  fullname = StringField('Full Name', validators=[DataRequired()])
  email = StringField('Email', validators=[DataRequired()])
  user_group = StringField('User Group', validators=[DataRequired()])