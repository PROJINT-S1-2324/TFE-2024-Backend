package prise.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prise.example.demo.model.ShellyEclaiData;

import java.util.Date;
import java.util.List;

@Repository
public interface ShellyEclaiDataRepository extends JpaRepository<ShellyEclaiData, Long> {
    List<ShellyEclaiData> findByTimestampBetween(Date start, Date end);
}
