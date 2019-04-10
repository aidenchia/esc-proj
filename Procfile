web: gunicorn app:app --preload
release: python3 manage.py db migrate
release: python3 manage.py db upgrade
