package nl.fontys.s3.grp1.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="journalist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_email")
    private AccountEntity account;

    @NotBlank
    @Column(name="first_name")
    private String firstname;

    @NotBlank
    @Column(name = "last_name")
    private String lastname;

    @Past
    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryEntity> stories;
}
