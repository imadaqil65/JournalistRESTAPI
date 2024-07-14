package nl.fontys.s3.grp1.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="story")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "journalist_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private JournalistEntity author;

    @CreationTimestamp
    @Column(name = "publish_date")
    private LocalDate publishDate;

    @NotNull
    @Column(name = "published")
    private Boolean isPublished;

    @OneToMany(mappedBy = "storyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files;
}
