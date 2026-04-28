package sn.douvewane.apiv1.demande;

import jakarta.persistence.*;
import lombok.*;
import sn.douvewane.apiv1.patient.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString(exclude = "patient")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
