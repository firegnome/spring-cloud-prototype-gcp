package ch.hsr.sai14sa12.authmicroservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    ApplicationUser findByUsername(String username);
}
