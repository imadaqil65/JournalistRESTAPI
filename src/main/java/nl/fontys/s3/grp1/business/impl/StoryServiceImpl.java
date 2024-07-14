package nl.fontys.s3.grp1.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.StoryService;
import nl.fontys.s3.grp1.business.converter.FileConverter;
import nl.fontys.s3.grp1.business.converter.StoryConverter;
import nl.fontys.s3.grp1.business.exception.InvalidJournalistException;
import nl.fontys.s3.grp1.business.exception.InvalidStoryException;
import nl.fontys.s3.grp1.configuration.security.token.AccessToken;
import nl.fontys.s3.grp1.domain.File;
import nl.fontys.s3.grp1.domain.Story;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryRequest;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryResponse;
import nl.fontys.s3.grp1.domain.dto.story.GetAllStoriesResponse;
import nl.fontys.s3.grp1.domain.dto.story.UpdateStoryRequest;
import nl.fontys.s3.grp1.persistence.FileRepository;
import nl.fontys.s3.grp1.persistence.JournalistRepository;
import nl.fontys.s3.grp1.persistence.StoryRepository;
import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;
import nl.fontys.s3.grp1.persistence.entity.StoryEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final JournalistRepository journalistRepository;
    private final FileRepository fileRepository;
    private final AccessToken requestAccessToken;

    @Override
    public CreateStoryResponse createStory(CreateStoryRequest request) {
        Optional<JournalistEntity> optionalJournalistEntity = journalistRepository.findById(request.getAuthorId());

        if (optionalJournalistEntity.isEmpty() || !optionalJournalistEntity.get().getId().equals(requestAccessToken.getUserId())) {
            throw new InvalidJournalistException("INVALID_JOURNALIST_ID");
        }

        JournalistEntity journalistEntity = optionalJournalistEntity.get();
        StoryEntity storyEntity = saveNewStory(journalistEntity, request);

        return CreateStoryResponse.builder().id(storyEntity.getId()).build();
    }

    @Override
    public final GetAllStoriesResponse getAllStories() {
        List<StoryEntity> entities = storyRepository.findAllByIsPublishedIsTrue();
        return GetAllStoriesResponse.builder().storyList(entities.stream().map(entity -> {
                    List<File> files = FileConverter.convertFromFileEntity(
                            fileRepository.findAllByStoryEntity_Id(entity.getId()));
                    return StoryConverter.convertFromStoryEntity(entity, files);})
                .collect(Collectors.toList())).build();
    }

    @Override
    public GetAllStoriesResponse getAllStoriesByAuthorId(Long id) {
        if(!requestAccessToken.getUserId().equals(id)){
            throw new InvalidJournalistException("JOURNALIST_INVALID");
        }

        List<StoryEntity> entities = storyRepository.findAllByAuthorId(id);
        return GetAllStoriesResponse.builder().storyList(entities.stream().map(entity -> {
            List<File> files = FileConverter.convertFromFileEntity(
                    fileRepository.findAllByStoryEntity_Id(entity.getId()));
            return StoryConverter.convertFromStoryEntity(entity, files);})
                .collect(Collectors.toList())).build();
    }

    @Override
    public Optional<Story> getStoryById(Long id) {
        Optional<StoryEntity> entity = storyRepository.findById(id);

        if (entity.isEmpty()) {
            return Optional.empty();
        }

        if(!requestAccessToken.getUserId().equals(entity.get().getAuthor().getId())){
            throw new InvalidJournalistException("JOURNALIST_INVALID");
        }


        List<File> files = FileConverter.convertFromFileEntity(
                fileRepository.findAllByStoryEntity_Id(id));
        return Optional.of(StoryConverter.convertFromStoryEntity(entity.get(), files));
    }


    @Override
    public void updateStory(UpdateStoryRequest request) {
        Optional<StoryEntity> optionalStoryEntity = storyRepository.findById(request.getId());

        if (optionalStoryEntity.isEmpty() || !optionalStoryEntity.get().getAuthor().getId().equals(requestAccessToken.getUserId())) {
            throw new InvalidStoryException("INVALID_ID");
        }

        StoryEntity storyEntity = optionalStoryEntity.get();
        updateFields(storyEntity, request);

        this.storyRepository.save(storyEntity);
    }

    @Override
    public void deleteStoryById(Long id) {
        this.storyRepository.deleteById(id);
    }

    @Override
    public void publishOrUnpublishStory(Long id) {
        StoryEntity entity = storyRepository.getReferenceById(id);

        if (!requestAccessToken.getUserId().equals(entity.getAuthor().getId())) {
            throw new InvalidJournalistException("JOURNALIST_INVALID");
        }

        boolean newPublishedStatus = !entity.getIsPublished();
        entity.setIsPublished(newPublishedStatus);
        entity.setPublishDate(newPublishedStatus ? LocalDate.now() : null);

        storyRepository.save(entity);
    }


    private StoryEntity saveNewStory(JournalistEntity journalistEntity, CreateStoryRequest request) {
        StoryEntity storyEntity = StoryEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(journalistEntity)
                .isPublished(false)
                .build();

        return storyRepository.save(storyEntity);
    }

    private void updateFields(StoryEntity storyEntity, UpdateStoryRequest request) {
        storyEntity.setTitle(request.getTitle());
        storyEntity.setDescription(request.getDescription());
    }
}
