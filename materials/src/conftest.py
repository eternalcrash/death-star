import pytest
from app import app as flask_app

# Creates a fixture whose name is "app"
# and returns our flask server instance
@pytest.fixture
def app():
    return flask_app
