export DATABASE_URL=$(heroku config:get DATABASE_URL -a evening-forest-28744)
export APP_SETTINGS="config.ProductionConfig"
export PYTEST_ADDOPTS="-v --disable-warnings"
export DATABASE_NAME="postgresql-polished-71084"