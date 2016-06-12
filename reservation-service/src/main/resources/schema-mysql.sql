DROP TABLE IF EXISTS reservation;

CREATE TABLE reservation
(
  id                BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  reservation_name  VARCHAR(255)
);

ALTER TABLE reservation AUTO_INCREMENT 0;