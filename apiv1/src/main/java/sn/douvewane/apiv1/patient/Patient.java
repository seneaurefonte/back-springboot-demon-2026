package sn.douvewane.apiv1.patient;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import sn.douvewane.apiv1.auth.entities.User;
import sn.douvewane.apiv1.demande.Demande;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User {
    private String numero;
    private String telephone;
    private String adresse;
    
    @Column(columnDefinition = "TEXT")
    private String antecedents;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Demande> demandes = new ArrayList<>();
}
