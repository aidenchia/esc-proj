## Event Scheduling
---
Our project for SUTD's 50.003 Elements of Software Construction, where we had to design a web app that could take in a set of course requirements and generate a schedule that fulfilled the requirements.

### Prerequisites
---
- Python 3.7
- OSX
- Heroku account
- Heroku CLI
- Postgres.app
- PostgreSQL CLI

### Installation
---  
Please follow the steps below.  

__Install all dependencies:__
```bash
$ pip3 install virtualenv
$ virtualenv venv
$ source venv/bin/activate
$ pip3 install -r requirements.txt
```

__Set environmet variables:__
```bash
source set_env_vars.sh
```

__Check environment variables:__
```bash
$ source check_env_vars.sh
```
You should see the output:
```bash
$ declare -x APP_SETTINGS="config.ProductionConfig"
$ declare -x DATABASE_URL="postgres://..."
```
where `DATABASE_URL` should be the URL of the provisioned Heroku database.

__Check heroku configuration vars:__
```bash
$ heroku config
```

### Database

Follow Heroku documetation to install the necessary tools to use Postgres with Heroku. Then, you can inspect the database as follows.
---
__Inspect database:__
```bash
$ heroku pg:psql
```
You'll connect to the remote database from your command line and you can then view the tables and their content via PostgreSQL commands. 

__Pull remote database:__  
```bash
source pullDatabase.sh
```
A local database called 'mylocaldb' will be created. If an error is thrown, you most likely already have another local database called 'mylocaldb'. You need to delete that before running this script. 

__Update database:__  

If you wish to make changes to the remote database, you should do by modifying the classes in the `models.py` script. We are using the `Flask_Migrate` library to help automatically generate migration scripts.

Once you're happy with the changes to you've made to the `models.py` script, run the following:
```bash
$ source db_update.sh
```

### Running Tests
---
We used the __Pytest__ framework in order to run automated testing of our web app. To run the full suite of tests, type in the following in terminal:
```bash
pytest --numSubjects=x
```
where x can be any value you want. To get a successful test result, you should pass x equal to the number of subjects in the database.


