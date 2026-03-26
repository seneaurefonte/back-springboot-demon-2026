package sn.douvewane.apiv1.demande.mapper;

import org.springframework.stereotype.Component;
import sn.douvewane.apiv1.demande.Demande;
import sn.douvewane.apiv1.demande.dto.DemandeRequestDTO;
import sn.douvewane.apiv1.demande.dto.DemandeResponseDTO;

@Component
public class DemandeMapper {

    public Demande toEntity(DemandeRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Demande demande = new Demande();
        demande.setSpecialite(dto.getSpecialite());
        demande.setDateDemandee(dto.getDateDemandee());
        demande.setHeure(dto.getHeure());
        demande.setMotif(dto.getMotif());
        if (dto.getStatut() != null) {
            demande.setStatut(dto.getStatut());
        }
        return demande;
    }

    public DemandeResponseDTO toDto(Demande entity) {
        if (entity == null) {
            return null;
        }
        DemandeResponseDTO dto = new DemandeResponseDTO();
        dto.setId(entity.getId());
        dto.setReference(entity.getReference());
        
        if (entity.getPatient() != null) {
            dto.setPatientId(entity.getPatient().getId());
            dto.setPatientNom(entity.getPatient().getNom());
            dto.setPatientPrenom(entity.getPatient().getPrenom());
        }
        
        dto.setSpecialite(entity.getSpecialite());
        dto.setDateDemandee(entity.getDateDemandee());
        dto.setHeure(entity.getHeure());
        dto.setStatut(entity.getStatut());
        dto.setMotif(entity.getMotif());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDerniereModification(entity.getDerniereModification());
        return dto;
    }
}
