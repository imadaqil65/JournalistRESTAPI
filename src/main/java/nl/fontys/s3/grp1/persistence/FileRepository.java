package nl.fontys.s3.grp1.persistence;

import nl.fontys.s3.grp1.persistence.entity.FileEntity;
import nl.fontys.s3.grp1.persistence.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findAllByStoryEntity_Id(Long id);

    @Query("SELECT f.id FROM FileEntity f WHERE f.storyEntity.id = :storyId")
    List<Long> getFileIdsByStoryId(Long storyId);


}
