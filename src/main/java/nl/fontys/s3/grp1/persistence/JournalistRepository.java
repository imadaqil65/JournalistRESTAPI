package nl.fontys.s3.grp1.persistence;

import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalistRepository extends JpaRepository<JournalistEntity, Long> {

    Optional<JournalistEntity> findByAccountEmail(String email);
}
