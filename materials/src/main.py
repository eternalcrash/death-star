from app import app, db
from app import models


if __name__ == '__main__':
    db.create_all()
    # set some initial values for the deathstar (if not created)
    DAO = models.MaterialDAO()
    if DAO.get("Plasma") is None :
        DAO.create("Plasma", 10)
    if DAO.get("Titanium") is None:
        DAO.create("Titanium", 10)
    app.run(debug=True, host='0.0.0.0', port=8001)
