export APP_SETTINGS=config.ProductionConfig
export DATABASE_URL=$(heroku config:get DATABASE_URL -a evening-forest-28744)
