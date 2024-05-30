CREATE TABLE shelly_data
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    timestamp datetime NULL,
    power DOUBLE NOT NULL,
    voltage DOUBLE NOT NULL,
    current DOUBLE NOT NULL,
    total_energy DOUBLE NOT NULL,
    temperaturec DOUBLE NOT NULL,
    temperaturef DOUBLE NOT NULL,
    CONSTRAINT pk_shellydata PRIMARY KEY (id)
);