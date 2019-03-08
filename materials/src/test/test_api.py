"""
Little test to check if we could obtain Titanium and Plasma values
"""
from app import models
import pytest


@pytest.mark.filterwarnings("ignore:DeprecationWarning")
def test_get_Titanium(client):
    response = client.get("/materials/Titanium")
    assert response.status_code == 200
    assert response.json['name'] == "Titanium"
    assert type(response.json['value']) == int


@pytest.mark.filterwarnings("ignore:SADeprecationWarning")
def test_get_Plasma(client):
    response = client.get("/materials/Plasma")
    assert response.status_code == 200
    assert response.json['name'] == "Plasma"
    assert type(response.json['value']) == int
