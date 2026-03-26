package sn.douvewane.apiv1.mock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.douvewane.apiv1.demande.Demande;
import sn.douvewane.apiv1.demande.DemandeRepository;
import sn.douvewane.apiv1.demande.StatutDemande;
import sn.douvewane.apiv1.patient.Patient;
import sn.douvewane.apiv1.patient.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class MockDataRunner implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final DemandeRepository demandeRepository;

    public MockDataRunner(PatientRepository patientRepository, DemandeRepository demandeRepository) {
        this.patientRepository = patientRepository;
        this.demandeRepository = demandeRepository;
    }

    @Override
    public void run(String... args) {
        // Exécuter seulement si la table est vide
        if (patientRepository.count() == 0) {
            // Création de patients
            Patient p1 = new Patient();
            p1.setNumero("PAT-001");
            p1.setNom("Durand");
            p1.setPrenom("Pierre");
            p1.setTelephone("771234567");
            p1.setAdresse("Dakar, Plateau");
            p1.setAntecedents("Hypertension");
            patientRepository.save(p1);

            Patient p2 = new Patient();
            p2.setNumero("PAT-002");
            p2.setNom("Diop");
            p2.setPrenom("Fatou");
            p2.setTelephone("786543210");
            p2.setAdresse("Thiès, Mbour");
            p2.setAntecedents("Diabète");
            patientRepository.save(p2);

            Patient p3 = new Patient();
            p3.setNumero("PAT-003");
            p3.setNom("Ndiaye");
            p3.setPrenom("Moussa");
            p3.setTelephone("769876543");
            p3.setAdresse("Saint-Louis");
            p3.setAntecedents("Aucun");
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
            System.out.println("---------------------------------------------------------------");
        }
    }
}
