package sn.douvewane.apiv1.demande.dto;

import lombok.Data;
import sn.douvewane.apiv1.demande.StatutDemande;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DemandeResponseDTO {
    private Long id;
    private String reference;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private String specialite;
    private LocalDate dateDemandee;
    private LocalTime heure;
    private StatutDemande statut;
    private String motif;
    private LocalDate dateCreation;
    private LocalDate derniereModification;
}
