package nl.fontys.s3.grp1.domain.dto.story;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.grp1.domain.Story;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStoriesResponse {
    private List<Story> storyList;
}
