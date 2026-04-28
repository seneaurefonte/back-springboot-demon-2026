package sn.douvewane.apiv1.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.douvewane.apiv1.auth.dtos.LoginRequestDTO;
import sn.douvewane.apiv1.auth.dtos.LoginResponseDTO;
import sn.douvewane.apiv1.auth.services.AuthService;
import sn.douvewane.apiv1.shared.RestResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(RestResponse.success(response, "Connexion réussie"));
    }
}
