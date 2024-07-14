package nl.fontys.s3.grp1.business;

import nl.fontys.s3.grp1.domain.Journalist;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistResponse;
import nl.fontys.s3.grp1.domain.dto.journalist.UpdateJournalistRequest;

import java.util.Optional;

public interface JournalistService {
    CreateJournalistResponse createJournalist(CreateJournalistRequest request);
    void updateJournalist(UpdateJournalistRequest request);
    void deleteJournalist(long id);
    Optional<Journalist> getJournalist(Long id);
}
