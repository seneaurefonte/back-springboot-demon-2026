package sn.douvewane.apiv1.patient.mapper;

import org.springframework.stereotype.Component;
import sn.douvewane.apiv1.patient.Patient;
import sn.douvewane.apiv1.patient.dto.PatientListDTO;
import sn.douvewane.apiv1.patient.dto.PatientRequestDTO;
import sn.douvewane.apiv1.patient.dto.PatientResponseDTO;

@Component
public class PatientMapper {
    
    public Patient toEntity(PatientRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setTelephone(dto.getTelephone());
        patient.setAdresse(dto.getAdresse());
        patient.setAntecedents(dto.getAntecedents());
        return patient;
    }
    
    public PatientResponseDTO toDto(Patient entity) {
        if (entity == null) {
            return null;
        }
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        dto.setAdresse(entity.getAdresse());
        dto.setAntecedents(entity.getAntecedents());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    public PatientListDTO toListDto(Patient entity) {
        if (entity == null) {
            return null;
        }
        PatientListDTO dto = new PatientListDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        return dto;
    }
}
