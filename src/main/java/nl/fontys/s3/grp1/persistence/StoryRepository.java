package nl.fontys.s3.grp1.persistence;

import nl.fontys.s3.grp1.persistence.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<StoryEntity, Long> {
    List<StoryEntity> findAllByTitleContaining(String Title);
    List<StoryEntity> findAllByAuthorId(Long id);
    List<StoryEntity> findAllByIsPublishedIsTrue();
    List<StoryEntity> findAllByTitleContainingAndAuthorId(String title, Long id);
}
