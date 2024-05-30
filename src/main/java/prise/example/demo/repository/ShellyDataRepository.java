package prise.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prise.example.demo.model.ShellyData;

import java.util.Date;
import java.util.List;

@Repository
public interface ShellyDataRepository extends JpaRepository<ShellyData, Long> {
    List<ShellyData> findByTimestampBetween(Date start, Date end);
}
