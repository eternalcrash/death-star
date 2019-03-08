# Death-star
*A defense and resources system using microservices for the Empire*
This project uses docker containers and docker compose to provide some services (documented with swagger)
There are defined the following services (see docker-compose.yml)
* A python flask rest api (using flask-restplus) to manage Empire's materials (Plasma and Titanium)
* A java jersey rest api (using jedis (😊) to implement a job queue using redis)
* A redis container
* A redis-browser container



# Requirements

* git
* docker
* docker-compose
* curl

## Install and configure Docker

https://docs.docker.com/install/

## Install and configure docker-compose

https://docs.docker.com/compose/install/


# Run the project

```
docker-compose build

docker compose up

```

# View the endpoints
The project setups 3 endpoints:

* The materials API will be accessible at
0.0.0.0:8001
There you could manually test the API with swagger ui

* The defense API will be accessible at
0.0.0.0:8002/swagger/
> mind the trailing slash '/'
There you could manually test the API with another swagger ui

* A redis browser to see the work queues and time will also be available at
0.0.0.0:4567
> Queues will be created only when you send a build request
> For instance:
> Post a empty defense quadrant and then order a new Deck
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense'
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://0.0.0.0:8002/deathstar-defenses/defense/2?status=BuildingDeck'
```
> Then Deck queue should appear on 0.0.0.0:4567



# Run the automatic tests

The materials api (python / flask)
```
docker-compose run --entrypoint "python -m pytest" materials
```
The defense api (java / jersey) tests are already executed with maven in the docker image creation

## Curl test for the full system
 From the host machine execute
```
bash curl_tests.sh 
```
