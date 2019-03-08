from flask import Flask
from flask_restplus import Api, Resource, fields, reqparse
from . import models
from flask import Blueprint

blueprint = Blueprint('api', __name__)

api = Api(
    blueprint, version='1.0', title='Empire materials API',
    description='A simple REST API to manage building resources',
)

ns = api.namespace('materials', description='Materials operations')

# swagger api model
material_api_model = api.model('Material', {
    'name': fields.String(
        readOnly=True, description='The material identifier'),
    'value': fields.Integer(
        required=True, description='The material quantity')
})


# parser for material value updates (put)
parser = reqparse.RequestParser()
parser.add_argument('value', type=int, help='invalid value')

# Object to persist materials
DAO = models.MaterialDAO()


@ns.route('/')
class MaterialListResource(Resource):
    '''Shows a list of all materials, and lets you POST new ones'''
    @ns.doc('list_materials')
    @ns.marshal_list_with(material_api_model)
    def get(self):
        '''List all materials'''
        return DAO.list(), 200

    @ns.doc('create_material')
    @ns.expect(material_api_model)
    @ns.marshal_with(material_api_model, code=201)
    def post(self):
        '''Create a new material'''
        return DAO.create(**api.payload), 201


@ns.route('/<string:name>')
@ns.response(404, 'Material not found')
@ns.param('name', 'The material identifier')
class MaterialResource(Resource):
    '''A single material item and lets you update them (the quantity)'''
    @ns.doc('get_material')
    @ns.marshal_with(material_api_model)
    def get(self, name):
        '''Fetch the material by its name'''
        material = DAO.get(name)
        if material is None:
            return None, 404
        return material

    @ns.doc('delete_material')
    @ns.response(204, 'Material deleted')
    def delete(self, name):
        '''Delete the material'''
        if DAO.delete(name):
            return '', 204
        else:
            return '', 404

    @ns.doc('update material quantity')
    @ns.marshal_with(material_api_model)
    @ns.param('value', 'The material new quantity')
    def put(self, name):
        args = parser.parse_args()
        print(args)
        '''Update the resource quantity'''
        material = DAO.update(name, args.get("value"))
        if material:
            return material
        else:
            return '', 404
