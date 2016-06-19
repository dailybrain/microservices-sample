# microservices-sample


## Services

* Configuration service on port `8888`
* Eureka service on port `8761`
* Reservation service on port `8000`
* Edge service on port `9999`
* Hystrix dashboard on port `7979`
* Zipkin dashboard on port `9411`


## Local setup

    ./mvnw package

    docker-compose -d rabbitmq

    ./mvnw -pl config-service spring-boot:run
    ./mvnw -pl discovery-service spring-boot:run
    ./mvnw -pl reservation-service spring-boot:run
    ./mvnw -pl edge-service spring-boot:run
    ./mvnw -pl hystrix-dashboard spring-boot:run
    ./mvnw -pl zipkin-dashboard spring-boot:run



## Docker setup

Launch:

    docker-machine start default
    eval $(docker-machine env default)

    ./start.sh
    (later...)
    ./stop.sh


Scaling

    docker-compose scale reservation-service=2


Clean:

    docker stop $(docker ps -q)
    docker rm $(docker ps -a -q)


Open Circuit Breaker

    ab -k -c 350 -n 20000 http://192.168.99.100:9999/reservations/names


Insert in Queue:

    for i in {1..20}; do curl -H "Content-Type: application/json" -XPOST http://192.168.99.100:9999/reservations -d '{ "reservationName" : "Offline $i" }'; done

