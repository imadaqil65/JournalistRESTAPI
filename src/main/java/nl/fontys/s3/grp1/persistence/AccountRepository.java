package nl.fontys.s3.grp1.persistence;

import nl.fontys.s3.grp1.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
}
