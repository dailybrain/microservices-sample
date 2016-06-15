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


Open Circuit Breaker

    ab -k -c 350 -n 20000 http://192.168.99.100:9999/reservations/names


Insert in Queue:

    for i in {1..20}; do curl -H "Content-Type: application/json" -XPOST http://192.168.99.100:9999/reservations -d '{ "reservationName" : "Offline $i" }'; done

