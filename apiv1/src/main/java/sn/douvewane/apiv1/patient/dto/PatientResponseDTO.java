package sn.douvewane.apiv1.patient.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PatientResponseDTO {
    private Long id;
    private String numero;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String antecedents;
    private LocalDateTime createdAt;
}
