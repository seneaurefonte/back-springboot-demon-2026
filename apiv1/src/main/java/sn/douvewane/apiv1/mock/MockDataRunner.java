package sn.douvewane.apiv1.mock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.douvewane.apiv1.auth.entities.Role;
import sn.douvewane.apiv1.auth.entities.RoleType;
import sn.douvewane.apiv1.auth.repositories.RoleRepository;
import sn.douvewane.apiv1.demande.Demande;
import sn.douvewane.apiv1.demande.DemandeRepository;
import sn.douvewane.apiv1.demande.StatutDemande;
import sn.douvewane.apiv1.patient.Patient;
import sn.douvewane.apiv1.patient.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class MockDataRunner implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final DemandeRepository demandeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MockDataRunner(PatientRepository patientRepository, DemandeRepository demandeRepository,
                         RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.demandeRepository = demandeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Créer les rôles s'ils n'existent pas
        if (roleRepository.findByName(RoleType.PATIENT).isEmpty()) {
            roleRepository.save(new Role(RoleType.PATIENT));
        }
        if (roleRepository.findByName(RoleType.ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleType.ADMIN));
        }

        // Exécuter seulement si la table est vide
        if (patientRepository.count() == 0) {
            Role patientRole = roleRepository.findByName(RoleType.PATIENT).orElseThrow();

            // Création de patients
            Patient p1 = new Patient();
            p1.setEmail("pierre.durand@email.com");
            p1.setPassword(passwordEncoder.encode("password123"));
            p1.setNumero("PAT-001");
            p1.setNom("Durand");
            p1.setPrenom("Pierre");
            p1.setTelephone("771234567");
            p1.setAdresse("Dakar, Plateau");
            p1.setAntecedents("Hypertension");
            p1.setRoles(new HashSet<>(Set.of(patientRole)));
            p1.setEnabled(true);
            patientRepository.save(p1);

            Patient p2 = new Patient();
            p2.setEmail("fatou.diop@email.com");
            p2.setPassword(passwordEncoder.encode("password123"));
            p2.setNumero("PAT-002");
            p2.setNom("Diop");
            p2.setPrenom("Fatou");
            p2.setTelephone("786543210");
            p2.setAdresse("Thiès, Mbour");
            p2.setAntecedents("Diabète");
            p2.setRoles(new HashSet<>(Set.of(patientRole)));
            p2.setEnabled(true);
            patientRepository.save(p2);

            Patient p3 = new Patient();
            p3.setEmail("moussa.ndiaye@email.com");
            p3.setPassword(passwordEncoder.encode("password123"));
            p3.setNumero("PAT-003");
            p3.setNom("Ndiaye");
            p3.setPrenom("Moussa");
            p3.setTelephone("769876543");
            p3.setAdresse("Saint-Louis");
            p3.setAntecedents("Aucun");
            p3.setRoles(new HashSet<>(Set.of(patientRole)));
            p3.setEnabled(true);
            patientRepository.save(p3);

            // Création de demandes associées aux patients
            Demande d1 = new Demande();
            d1.setReference("DEM-101");
            d1.setSpecialite("Cardiologue");
            d1.setDateDemandee(LocalDate.now().plusDays(2));
            d1.setHeure(LocalTime.of(10, 30));
            d1.setMotif("Douleurs thoraciques fréquentes");
            d1.setStatut(StatutDemande.en_attente);
            d1.setPatient(p1);
            demandeRepository.save(d1);

            Demande d2 = new Demande();
            d2.setReference("DEM-102");
            d2.setSpecialite("Generaliste");
            d2.setDateDemandee(LocalDate.now().plusDays(5));
            d2.setHeure(LocalTime.of(9, 0));
            d2.setMotif("Consultation de routine");
            d2.setStatut(StatutDemande.accepte);
            d2.setPatient(p1);
            demandeRepository.save(d2);

            Demande d3 = new Demande();
            d3.setReference("DEM-103");
            d3.setSpecialite("Endocrinologue");
            d3.setDateDemandee(LocalDate.now().plusDays(10));
            d3.setHeure(LocalTime.of(15, 0));
            d3.setMotif("Suivi diabète post-traitement");
            d3.setStatut(StatutDemande.en_attente);
            d3.setPatient(p2);
            demandeRepository.save(d3);

            Demande d4 = new Demande();
            d4.setReference("DEM-104");
            d4.setSpecialite("Dermatologue");
            d4.setDateDemandee(LocalDate.now().plusDays(1));
            d4.setHeure(LocalTime.of(11, 15));
            d4.setMotif("Examen grain de beauté");
            d4.setStatut(StatutDemande.annule);
            d4.setPatient(p3);
            demandeRepository.save(d4);

            System.out.println("✅ ---------- JEU DE DONNEES (MOCK) CREE AVEC SUCCES ---------- ✅");
            System.out.println("👥 Patients insérés : " + patientRepository.count());
            System.out.println("📝 Demandes insérées : " + demandeRepository.count());
            System.out.println("🔐 Rôles créés : " + roleRepository.count());
            System.out.println("----------- CREDENTIALS POUR TEST ----------");
            System.out.println("👤 Utilisateur 1 : pierre.durand@email.com / password123");
            System.out.println("👤 Utilisateur 2 : fatou.diop@email.com / password123");
            System.out.println("👤 Utilisateur 3 : moussa.ndiaye@email.com / password123");
            System.out.println("-------------------------------------------");
        }
    }
}
