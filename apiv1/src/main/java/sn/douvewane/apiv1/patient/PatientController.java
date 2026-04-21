package sn.douvewane.apiv1.patient;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.douvewane.apiv1.patient.dto.PatientListDTO;
import sn.douvewane.apiv1.patient.dto.PatientRequestDTO;
import sn.douvewane.apiv1.patient.dto.PatientResponseDTO;
import sn.douvewane.apiv1.shared.PageResponse;
import sn.douvewane.apiv1.shared.RestResponse;

@RestController
@RequestMapping("/api/v1/patients")
@CrossOrigin(origins = "*") // Allow for frontend integration
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<RestResponse<PageResponse<PatientListDTO>>> getAllPatients(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "${api.pagination.default-page}") int page,
            @RequestParam(defaultValue = "${api.pagination.default-size}") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
                
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PatientListDTO> result = patientService.searchPatients(keyword, pageable);
        return ResponseEntity.ok(RestResponse.success(new PageResponse<>(result), "Liste des patients récupérée avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<PatientResponseDTO>> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(RestResponse.success(patient, "Patient récupéré avec succès")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RestResponse<PatientResponseDTO>> createPatient(@Valid @RequestBody PatientRequestDTO patientDto) {
        PatientResponseDTO created = patientService.createPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success(created, "Patient créé avec succès"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<PatientResponseDTO>> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequestDTO patientDto) {
        PatientResponseDTO updated = patientService.updatePatient(id, patientDto);
        return ResponseEntity.ok(RestResponse.success(updated, "Patient mis à jour avec succès"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(RestResponse.success(null, "Patient supprimé avec succès"));
    }
}
