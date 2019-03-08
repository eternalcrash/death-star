import os
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from .service import blueprint
from .models import db

# flask app shared object
app = Flask(__name__)


# Some configuration

project_dir = os.path.dirname(os.path.abspath(__file__))
# I choose SQLite to avoid more heavy dependencies like MariaDB
# But using SQLAlchemy would be easy to switch.
database_file = "sqlite:///{}".format(
    os.path.join(project_dir, "materials.db"))

app.config["SQLALCHEMY_DATABASE_URI"] = database_file
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# initialize the db object
db.init_app(app)

# register our material api blueprint
app.register_blueprint(blueprint)
app.app_context().push()
