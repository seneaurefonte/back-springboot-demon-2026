package sn.douvewane.apiv1.demande.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sn.douvewane.apiv1.demande.StatutDemande;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DemandeRequestDTO {
    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;
    
    private String specialite;
    private LocalDate dateDemandee;
    private LocalTime heure;
    private String motif;
    private StatutDemande statut;
}
