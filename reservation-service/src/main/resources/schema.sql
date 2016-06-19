DROP TABLE IF EXISTS reservation;

CREATE TABLE reservation
(
  id                BIGINT(20) NOT NULL AUTO_INCREMENT,
  reservation_name  VARCHAR(255),
  PRIMARY KEY (id)
);
