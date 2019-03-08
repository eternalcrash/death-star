# Requirements

* git
* docker
* docker-compose

## Install and configure Docker

https://docs.docker.com/install/

## Install and configure docker-compose

https://docs.docker.com/compose/install/


# Run the project

```
docker-compose build

docker compose up


```

The materials API will be accessible at
0.0.0.0:8001
There you could manually test the API with swagger ui



# Run the automatic tests

The materials api (flask)
```
docker-compose run --entrypoint "python -m pytest" materials
```
