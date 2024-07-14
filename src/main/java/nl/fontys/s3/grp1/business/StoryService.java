package nl.fontys.s3.grp1.business;

import nl.fontys.s3.grp1.domain.Story;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryRequest;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryResponse;
import nl.fontys.s3.grp1.domain.dto.story.GetAllStoriesResponse;
import nl.fontys.s3.grp1.domain.dto.story.UpdateStoryRequest;

import java.util.Optional;

public interface StoryService {
    CreateStoryResponse createStory(CreateStoryRequest request);
    GetAllStoriesResponse getAllStories();
    GetAllStoriesResponse getAllStoriesByAuthorId(Long id);
    void publishOrUnpublishStory(Long id);
    Optional<Story> getStoryById(Long id);
    void updateStory(UpdateStoryRequest request);
    void deleteStoryById(Long id);
}
