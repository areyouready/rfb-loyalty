package de.fnortheim.repository;

import de.fnortheim.domain.RfbUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RfbUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RfbUserRepository extends JpaRepository<RfbUser, Long> {

}
