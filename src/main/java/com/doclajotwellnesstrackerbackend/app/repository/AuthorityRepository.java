package com.doclajotwellnesstrackerbackend.app.repository;

import com.doclajotwellnesstrackerbackend.app.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Authority findByName(String userAuthorityName);
}
