package sn.douvewane.apiv1.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.douvewane.apiv1.auth.dtos.LoginRequestDTO;
import sn.douvewane.apiv1.auth.dtos.LoginResponseDTO;
import sn.douvewane.apiv1.auth.entities.User;
import sn.douvewane.apiv1.auth.jwt.JwtTokenProvider;
import sn.douvewane.apiv1.auth.repositories.UserRepository;
import sn.douvewane.apiv1.exception.EntityNotFoundException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException("Email ou mot de passe incorrect");
        }

        String token = jwtTokenProvider.generateToken(user);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setUserId(user.getId());
        
        if (!user.getRoles().isEmpty()) {
            response.setRole(user.getRoles().iterator().next().getName().toString());
        }

        return response;
    }
}
