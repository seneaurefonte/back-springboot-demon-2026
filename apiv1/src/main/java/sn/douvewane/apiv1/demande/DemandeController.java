package sn.douvewane.apiv1.demande;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.douvewane.apiv1.demande.dto.DemandeRequestDTO;
import sn.douvewane.apiv1.demande.dto.DemandeResponseDTO;

@RestController
@RequestMapping("/api/v1/demandes")
@CrossOrigin(origins = "*") // Allow for frontend integration
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public Page<DemandeResponseDTO> getAllDemandes(
            @RequestParam(required = false) Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        if (patientId != null) {
            return demandeService.getDemandesByPatientId(patientId, pageable);
        }
        return demandeService.getAllDemandes(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeResponseDTO> getDemandeById(@PathVariable Long id) {
        return demandeService.getDemandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeResponseDTO createDemande(@Valid @RequestBody DemandeRequestDTO dto) {
        return demandeService.createDemande(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemandeResponseDTO> updateDemande(@PathVariable Long id, @Valid @RequestBody DemandeRequestDTO dto) {
        try {
            return ResponseEntity.ok(demandeService.updateDemande(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        demandeService.deleteDemande(id);
        return ResponseEntity.noContent().build();
    }
}
