# microservices-sample

## Local setup

    ./mvnw package
    ./mvnw -pl config-service spring-boot:run
    ./mvnw -pl discovery-service spring-boot:run


## Docker setup

Launch:

    docker-machine start default
    eval $(docker-machine env default)

    ./run.sh


Scaling

    docker-compose scale reservation-service=2


Clean:

    docker stop $(docker ps -q)
    docker rm $(docker ps -a -q)


