package nl.fontys.s3.grp1.domain.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFileRequest {
    @NotNull
    private Long storyId;

    @NotNull
    private MultipartFile file;
}
