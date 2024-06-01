package prise.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prise.example.demo.model.ShellyBoilData;

import java.util.Date;
import java.util.List;

@Repository
public interface ShellyBoilDataRepository extends JpaRepository<ShellyBoilData, Long> {
    List<ShellyBoilData> findByTimestampBetween(Date start, Date end);
}
