FROM maven:3.5-jdk-8

COPY ./ /app
WORKDIR /app

RUN mvn clean package --batch-mode

ENTRYPOINT java -jar target/deathstar-defenses-1.0-SNAPSHOT.jar
