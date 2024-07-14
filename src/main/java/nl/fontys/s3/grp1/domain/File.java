package nl.fontys.s3.grp1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long id;
    private String name;
    private byte[] data;
    private String filetype;
    private LocalDateTime createdDate;
}
