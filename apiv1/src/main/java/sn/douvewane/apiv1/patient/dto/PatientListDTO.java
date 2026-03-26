package sn.douvewane.apiv1.patient.dto;

import lombok.Data;

@Data
public class PatientListDTO {
    private Long id;
    private String numero;
    private String nom;
    private String prenom;
    private String telephone;
}
