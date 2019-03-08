FROM python:3.7.0-alpine

RUN mkdir -p /app
COPY ./requirements.txt /app
WORKDIR /app

RUN pip install --no-cache-dir -r requirements.txt

ENTRYPOINT python -B main.py

