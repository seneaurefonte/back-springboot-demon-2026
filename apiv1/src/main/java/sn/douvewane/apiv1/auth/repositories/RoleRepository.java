package sn.douvewane.apiv1.auth.repositories;

import sn.douvewane.apiv1.auth.entities.Role;
import sn.douvewane.apiv1.auth.entities.RoleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
