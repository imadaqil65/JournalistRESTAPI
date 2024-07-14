package nl.fontys.s3.grp1.domain.dto.journalist;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateJournalistRequest {
    @NotBlank
    @Email
    @NotNull
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull
    private LocalDate birthday;
}
