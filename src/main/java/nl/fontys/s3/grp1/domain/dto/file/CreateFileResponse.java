package nl.fontys.s3.grp1.domain.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.grp1.domain.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFileResponse {
    @NotNull
    private File file;
}
