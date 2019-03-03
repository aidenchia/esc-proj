# https://alembic.sqlalchemy.org/en/latest/tutorial.html 

import os
from flask_script import Manager
from flask_migrate import Migrate, MigrateCommand

from app import app, db

app.config.from_object(os.environ['APP_SETTINGS'])
migrate = Migrate(app, db)
manager = Manager(app)

manager.add_command('db', MigrateCommand)
# allows us to run commands in terminal such as 
# python3 manage.py db init, 
# python3 manage.py db migrate
# python3 manage.py db upgrade
# If migrating to remote server on heroku,
# prepend 'heroku run' before the above commands

if __name__ == '__main__':
    manager.run()