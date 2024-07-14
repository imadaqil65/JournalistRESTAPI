package nl.fontys.s3.grp1.domain.dto.journalist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateJournalistRequest {
    private Long id;

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull
    private LocalDate birthday;
}
