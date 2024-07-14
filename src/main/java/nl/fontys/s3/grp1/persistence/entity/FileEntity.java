package nl.fontys.s3.grp1.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="file")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String fileName;

    @Lob
    @NotNull
    @Column(name = "data")
    private byte[] data;

    @NotNull
    @Column(name="filetype")
    private String filetype;

    @CreationTimestamp
    @NotNull
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story_id")
    private StoryEntity storyEntity;
}
