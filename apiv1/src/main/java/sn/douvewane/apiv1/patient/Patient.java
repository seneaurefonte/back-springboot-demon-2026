package sn.douvewane.apiv1.patient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    
    @Column(columnDefinition = "TEXT")
    private String antecedents;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if(createdAt == null) {
             createdAt = LocalDateTime.now();
        }
    }
}
