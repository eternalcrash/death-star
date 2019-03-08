from flask_sqlalchemy import SQLAlchemy

# this object is initialized in __init__.py
db = SQLAlchemy()


class Material(db.Model):
    """ Simple database model to store empire's resources"""
    name = db.Column(
        db.String(64), unique=True, nullable=False, primary_key=True)
    value = db.Column(
        db.Integer, nullable=False)

    def __repr__(self):
        return "<{}: {}>".format(self.name, self.value)


class MaterialDAO(object):
    """ DAO object to perform CRUD operations in the database
        a wrapper for SQLAlchemy methods
    """
    def __init__(self):
        pass

    def list(self):
        return Material.query.all()

    def get(self, name):
        return Material.query.filter_by(name=name).first()

    def create(self, name, value):
        material = Material(name=name, value=value)
        db.session.add(material)
        db.session.commit()
        return material

    def update(self, name, newvalue):
        material = Material.query.filter_by(name=name).first()
        if material:
            material.value = newvalue
            db.session.commit()
            return material

    def delete(self, name):
        material = Material.query.filter_by(name=name).first()
        if material:
            db.session.delete(material)
            db.session.commit()
            return True
