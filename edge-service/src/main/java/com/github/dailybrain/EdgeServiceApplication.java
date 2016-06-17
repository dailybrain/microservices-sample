package com.github.dailybrain;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@EnableZuulProxy
@EnableHystrix
@EnableBinding(Source.class)
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class EdgeServiceApplication {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}
}

@RestController
@RequestMapping("/reservations")
class ReservationEdgeController {

    private static final Logger log = LoggerFactory.getLogger(ReservationEdgeController.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ReservationEdgeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private Source outputChannelSource;

    @RequestMapping(method = RequestMethod.POST)
    public void write(@RequestBody Reservation r)
    {
        MessageChannel channel = this.outputChannelSource.output();
        channel.send(
                org.springframework.messaging.support.MessageBuilder.withPayload(r.getReservationName()).build()
        );
    }

    public Collection<String> fallback(MultiValueMap<String,String> params) {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping(method = RequestMethod.GET, value = "/names")
    public Collection<String> names(@RequestParam MultiValueMap<String,String> params) {

        log.info("GET /names params: {}", params);

        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://reservation-service/reservations")
                .queryParams(params)
                .build()
                .encode()
                .toUri();

        return this.restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Resources<Reservation>>() {
                })
                .getBody()
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList());
    }
}

class Reservation {

    public Reservation() {
    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    private String reservationName;

    public String getReservationName() {
        return reservationName;
    }
}