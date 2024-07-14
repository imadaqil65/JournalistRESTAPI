package nl.fontys.s3.grp1.business.impl;

import nl.fontys.s3.grp1.business.exception.InvalidJournalistException;
import nl.fontys.s3.grp1.business.exception.InvalidStoryException;
import nl.fontys.s3.grp1.configuration.security.token.AccessToken;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class StoryServiceTest {
    @Mock
    private AccessToken requestAccessToken;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private JournalistRepository journalistRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private StoryServiceImpl storyServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStory_Success() {
        CreateStoryRequest request = new CreateStoryRequest("Title", "Description", 1L);
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setId(1L);
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(journalistRepository.findById(anyLong())).thenReturn(Optional.of(journalistEntity));
        when(storyRepository.save(any(StoryEntity.class))).thenReturn(new StoryEntity());

        CreateStoryResponse response = storyServiceImpl.createStory(request);

        assertNotNull(response);
        verify(storyRepository, times(1)).save(any(StoryEntity.class));
    }

    @Test
    void testCreateStory_Failure_InvalidJournalist() {
        CreateStoryRequest request = new CreateStoryRequest("Title", "Description", 1L);

        when(journalistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidJournalistException.class, () -> {
            storyServiceImpl.createStory(request);
        });
        verify(storyRepository, never()).save(any(StoryEntity.class));
    }

    @Test
    void testGetAllStories() {
        when(storyRepository.findAllByIsPublishedIsTrue()).thenReturn(List.of(StoryEntity.builder().id(1L).build()));
        when(fileRepository.getFileIdsByStoryId(anyLong())).thenReturn(new ArrayList<>());
        GetAllStoriesResponse response = storyServiceImpl.getAllStories();

        assertNotNull(response);
        assertEquals(1, response.getStoryList().size());
    }

    @Test
    void testGetStoryById_Success() {
        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(StoryEntity.builder().author(JournalistEntity.builder().id(1L).build()).build()));
        when(fileRepository.getFileIdsByStoryId(anyLong())).thenReturn(new ArrayList<>());
        when(requestAccessToken.getUserId()).thenReturn(1L);

        Optional<Story> response = storyServiceImpl.getStoryById(1L);

        assertTrue(response.isPresent());
    }

    @Test
    void testGetStoryById_Failure() {
        when(storyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Story> response = storyServiceImpl.getStoryById(1L);

        assertFalse(response.isPresent());
    }

    @Test
    void testUpdateStory_Success() {
        UpdateStoryRequest request = new UpdateStoryRequest(1L, "New Title", "New Description");
        StoryEntity storyEntity = StoryEntity.builder().author(JournalistEntity.builder().id(1L).build()).build();
        when(storyRepository.findById(request.getId())).thenReturn(Optional.of(storyEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        storyServiceImpl.updateStory(request);

        verify(storyRepository, times(1)).save(any(StoryEntity.class));
    }

    @Test
    void testUpdateStory_Failure_InvalidStory() {
        UpdateStoryRequest request = new UpdateStoryRequest(1L, "New Title", "New Description");

        when(storyRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(InvalidStoryException.class, () -> {
            storyServiceImpl.updateStory(request);
        });
        verify(storyRepository, never()).save(any(StoryEntity.class));
    }

    @Test
    void testDeleteStoryById_Success() {
        doNothing().when(storyRepository).deleteById(anyLong());

        storyServiceImpl.deleteStoryById(1L);

        verify(storyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteStoryById_Failure_StoryNotFound() {
        doThrow(new IllegalArgumentException("Story not found")).when(storyRepository).deleteById(anyLong());

        assertThrows(IllegalArgumentException.class, () -> {
            storyServiceImpl.deleteStoryById(1L);
        });

    }
}
