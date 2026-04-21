package sn.douvewane.apiv1.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    Page<Demande> findByPatientId(Long patientId, Pageable pageable);
    Page<Demande> findByDateDemandee(LocalDate date, Pageable pageable);
}
