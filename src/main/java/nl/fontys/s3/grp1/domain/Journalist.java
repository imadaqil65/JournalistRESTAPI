package nl.fontys.s3.grp1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Journalist {
    private Long id;
    private Account account;
    private String firstname;
    private String lastname;
    private LocalDate birthday;
    //private List<Article> stories;
}
