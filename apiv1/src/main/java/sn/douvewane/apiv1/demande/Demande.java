package sn.douvewane.apiv1.demande;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.douvewane.apiv1.patient.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String specialite;
    
    private LocalDate dateDemandee;
    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.en_attente;
    
    @Column(columnDefinition = "TEXT")
    private String motif;

    private LocalDate dateCreation;
    private LocalDate derniereModification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDate.now();
        derniereModification = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        derniereModification = LocalDate.now();
    }
}
