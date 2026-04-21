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
import sn.douvewane.apiv1.shared.PageResponse;
import sn.douvewane.apiv1.shared.RestResponse;

@RestController
@RequestMapping("/api/v1/demandes")
@CrossOrigin(origins = "*") // Allow for frontend integration
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public ResponseEntity<RestResponse<PageResponse<DemandeResponseDTO>>> getAllDemandes(
            @RequestParam(required = false) Long patientId,
            @RequestParam(defaultValue = "${api.pagination.default-page}") int page,
            @RequestParam(defaultValue = "${api.pagination.default-size}") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DemandeResponseDTO> result;
        if (patientId != null) {
            result = demandeService.getDemandesByPatientId(patientId, pageable);
        } else {
            result = demandeService.getAllDemandes(pageable);
        }
        return ResponseEntity.ok(RestResponse.success(new PageResponse<>(result), "Liste des demandes récupérée avec succès"));
    }

    @GetMapping("/aujourdhui")
    public ResponseEntity<RestResponse<PageResponse<DemandeResponseDTO>>> getDemandesDuJour(
            @RequestParam(defaultValue = "${api.pagination.default-page}") int page,
            @RequestParam(defaultValue = "${api.pagination.default-size}") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DemandeResponseDTO> result = demandeService.getDemandesDuJour(pageable);
        return ResponseEntity.ok(RestResponse.success(new PageResponse<>(result), "Liste des demandes du jour récupérée avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<DemandeResponseDTO>> getDemandeById(@PathVariable Long id) {
        return demandeService.getDemandeById(id)
                .map(demande -> ResponseEntity.ok(RestResponse.success(demande, "Demande récupérée avec succès")))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RestResponse<DemandeResponseDTO>> createDemande(@Valid @RequestBody DemandeRequestDTO dto) {
        DemandeResponseDTO created = demandeService.createDemande(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success(created, "Demande créée avec succès"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<DemandeResponseDTO>> updateDemande(@PathVariable Long id, @Valid @RequestBody DemandeRequestDTO dto) {
        DemandeResponseDTO updated = demandeService.updateDemande(id, dto);
        return ResponseEntity.ok(RestResponse.success(updated, "Demande mise à jour avec succès"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteDemande(@PathVariable Long id) {
        demandeService.deleteDemande(id);
        return ResponseEntity.ok(RestResponse.success(null, "Demande supprimée avec succès"));
    }
}
