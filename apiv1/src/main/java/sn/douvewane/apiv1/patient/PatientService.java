package sn.douvewane.apiv1.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sn.douvewane.apiv1.exception.EntityNotFoundException;
import sn.douvewane.apiv1.patient.dto.PatientListDTO;
import sn.douvewane.apiv1.patient.dto.PatientRequestDTO;
import sn.douvewane.apiv1.patient.dto.PatientResponseDTO;
import sn.douvewane.apiv1.patient.mapper.PatientMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientListDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toListDto)
                .collect(Collectors.toList());
    }

    public Page<PatientListDTO> searchPatients(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return patientRepository.findAll(pageable).map(patientMapper::toListDto);
        }
        return patientRepository.searchByNomOrPrenom(keyword, pageable).map(patientMapper::toListDto);
    }

    public Optional<PatientResponseDTO> getPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toDto);
    }

    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        Patient patient = patientMapper.toEntity(dto);
        patient.setNumero("PAT-" + System.currentTimeMillis());
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto) {
        return patientRepository.findById(id).map(patient -> {
            patient.setNom(dto.getNom());
            patient.setPrenom(dto.getPrenom());
            patient.setTelephone(dto.getTelephone());
            patient.setAdresse(dto.getAdresse());
            patient.setAntecedents(dto.getAntecedents());
            Patient updatedPatient = patientRepository.save(patient);
            return patientMapper.toDto(updatedPatient);
        }).orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'id " + id));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
