package nl.fontys.s3.grp1.business.converter;

import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.domain.Story;
import nl.fontys.s3.grp1.persistence.entity.StoryEntity;

import java.util.List;

public final class StoryConverter {
    public static Story convertFromStoryEntity(final StoryEntity entity, List<File> files) {
        return Story.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .isPublished(entity.getIsPublished())
                .publishDate(entity.getPublishDate())
                .description(entity.getDescription())
                .files(files)
                .build();
    }
}
