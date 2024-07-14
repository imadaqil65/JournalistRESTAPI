package nl.fontys.s3.grp1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Story {
    private Long id;
    private String title;
    private String description;
    private Boolean isPublished;
    private LocalDate publishDate;
    private List<File> files;
}
