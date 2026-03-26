package sn.douvewane.apiv1.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Page<Patient> searchPatients(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return patientRepository.findAll(pageable);
        }
        return patientRepository.searchByNomOrPrenom(keyword, pageable);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient createPatient(Patient patient) {
        if(patient.getNumero() == null || patient.getNumero().trim().isEmpty()){
            patient.setNumero("PAT-" + System.currentTimeMillis());
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedDetails) {
        return patientRepository.findById(id).map(patient -> {
            patient.setNom(updatedDetails.getNom());
            patient.setPrenom(updatedDetails.getPrenom());
            patient.setTelephone(updatedDetails.getTelephone());
            patient.setAdresse(updatedDetails.getAdresse());
            patient.setAntecedents(updatedDetails.getAntecedents());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'id " + id));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
