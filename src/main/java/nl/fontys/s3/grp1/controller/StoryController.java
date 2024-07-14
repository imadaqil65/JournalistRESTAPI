package nl.fontys.s3.grp1.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.StoryService;
import nl.fontys.s3.grp1.domain.Story;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryRequest;
import nl.fontys.s3.grp1.domain.dto.story.CreateStoryResponse;
import nl.fontys.s3.grp1.domain.dto.story.GetAllStoriesResponse;
import nl.fontys.s3.grp1.domain.dto.story.UpdateStoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stories")
@AllArgsConstructor
public class StoryController {
    private final StoryService storyService;

    @PostMapping()
    public ResponseEntity<CreateStoryResponse> createStory(@RequestBody @Valid CreateStoryRequest request) {
        CreateStoryResponse response = storyService.createStory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllStoriesResponse> getAllStories() {
        GetAllStoriesResponse response = storyService.getAllStories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("author/{id}")
    public ResponseEntity<GetAllStoriesResponse> getAllStories(@PathVariable(value = "id") final Long id) {
        GetAllStoriesResponse response = storyService.getAllStoriesByAuthorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Story> getStory(@PathVariable(value = "id") final Long id) {
        Optional<Story> optionalStory = storyService.getStoryById(id);
        return optionalStory.map(story -> ResponseEntity.ok().body(story)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateStory(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateStoryRequest request) {
        request.setId(id);
        storyService.updateStory(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("publish/{id}")
    public ResponseEntity<Object> updateStory(@PathVariable(value = "id") Long id) {
        storyService.publishOrUnpublishStory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable(value = "id") Long id) {
        storyService.deleteStoryById(id);
        return ResponseEntity.noContent().build();
    }
}
