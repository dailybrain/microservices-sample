package com.github.dailybrain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
}

@Component
@Profile({"docker", "development"})
class DummyCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository;


    @Autowired
    public DummyCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of("Vincent", "Josh", "Robert")
                .forEach(n -> reservationRepository.save(new Reservation(n)));

        reservationRepository.findAll().forEach(System.out::println);

    }
}

/*
@RestController
@RefreshScope
class MessageRestController {

    @Value("${message}")
    private String message;

    @RequestMapping("/message")
    String read() {
        return this.message;
    }

}
*/

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

@Entity
class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reservationName;

    Reservation() {

    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getId() {

        return id;
    }

    public String getReservationName() {
        return reservationName;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

}

