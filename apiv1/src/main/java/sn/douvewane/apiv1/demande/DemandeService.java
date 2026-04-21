package sn.douvewane.apiv1.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sn.douvewane.apiv1.demande.dto.DemandeRequestDTO;
import sn.douvewane.apiv1.demande.dto.DemandeResponseDTO;
import sn.douvewane.apiv1.demande.mapper.DemandeMapper;
import sn.douvewane.apiv1.exception.EntityNotFoundException;
import sn.douvewane.apiv1.patient.Patient;
import sn.douvewane.apiv1.patient.PatientRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DemandeService {
    private final DemandeRepository demandeRepository;
    private final PatientRepository patientRepository;
    private final DemandeMapper demandeMapper;

    public DemandeService(DemandeRepository demandeRepository, PatientRepository patientRepository, DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;
        this.patientRepository = patientRepository;
        this.demandeMapper = demandeMapper;
    }

    public Page<DemandeResponseDTO> getAllDemandes(Pageable pageable) {
        return demandeRepository.findAll(pageable).map(demandeMapper::toDto);
    }
    
    public Page<DemandeResponseDTO> getDemandesByPatientId(Long patientId, Pageable pageable) {
        return demandeRepository.findByPatientId(patientId, pageable).map(demandeMapper::toDto);
    }

    public Page<DemandeResponseDTO> getDemandesDuJour(Pageable pageable) {
        return demandeRepository.findByDateDemandee(LocalDate.now(), pageable).map(demandeMapper::toDto);
    }

    public Optional<DemandeResponseDTO> getDemandeById(Long id) {
        return demandeRepository.findById(id).map(demandeMapper::toDto);
    }

    public DemandeResponseDTO createDemande(DemandeRequestDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id " + dto.getPatientId()));
                
        Demande demande = demandeMapper.toEntity(dto);
        demande.setPatient(patient);
        demande.setReference("DEM-" + System.currentTimeMillis());
        
        Demande savedDemande = demandeRepository.save(demande);
        return demandeMapper.toDto(savedDemande);
    }

    public DemandeResponseDTO updateDemande(Long id, DemandeRequestDTO dto) {
        return demandeRepository.findById(id).map(demande -> {
            demande.setSpecialite(dto.getSpecialite());
            demande.setDateDemandee(dto.getDateDemandee());
            demande.setHeure(dto.getHeure());
            demande.setMotif(dto.getMotif());
            if (dto.getStatut() != null) {
                demande.setStatut(dto.getStatut());
            }
            if (!demande.getPatient().getId().equals(dto.getPatientId())) {
                Patient patient = patientRepository.findById(dto.getPatientId())
                        .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id " + dto.getPatientId()));
                demande.setPatient(patient);
            }
            Demande updatedDemande = demandeRepository.save(demande);
            return demandeMapper.toDto(updatedDemande);
        }).orElseThrow(() -> new EntityNotFoundException("Demande non trouvée avec l'id " + id));
    }

    public void deleteDemande(Long id) {
        demandeRepository.deleteById(id);
    }
}
