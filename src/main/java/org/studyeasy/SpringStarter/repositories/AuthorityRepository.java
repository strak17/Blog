package org.studyeasy.SpringStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studyeasy.SpringStarter.models.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
