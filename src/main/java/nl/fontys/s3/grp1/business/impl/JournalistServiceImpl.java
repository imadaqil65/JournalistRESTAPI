package nl.fontys.s3.grp1.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.JournalistService;
import nl.fontys.s3.grp1.business.converter.JournalistConverter;
import nl.fontys.s3.grp1.business.exception.InvalidAccountException;
import nl.fontys.s3.grp1.business.exception.InvalidJournalistException;
import nl.fontys.s3.grp1.configuration.security.token.AccessToken;
import nl.fontys.s3.grp1.domain.Journalist;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistResponse;
import nl.fontys.s3.grp1.domain.dto.journalist.UpdateJournalistRequest;
import nl.fontys.s3.grp1.persistence.JournalistRepository;
import nl.fontys.s3.grp1.persistence.entity.AccountEntity;
import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;
import nl.fontys.s3.grp1.persistence.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JournalistServiceImpl implements JournalistService {
    public final JournalistRepository journalistRepository;
    public final AccountRepository accountRepository;
    public final PasswordEncoder passwordEncoder;
    private final AccessToken requestAccessToken;

    private AccountEntity setAccountEntity(CreateJournalistRequest journalist){
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findById(journalist.getEmail());
        if (optionalAccountEntity.isPresent()) {
            throw new InvalidAccountException("INVALID_EMAIL");
        }

        return AccountEntity.builder()
                .email(journalist.getEmail())
                .password(passwordEncoder.encode(journalist.getPassword()))
                .build();
    }

    private JournalistEntity saveNewJournalist(CreateJournalistRequest journalist){
        AccountEntity account = setAccountEntity(journalist);

        return journalistRepository.save(JournalistEntity.builder()
                .account(account)
                .firstname(journalist.getFirstname())
                .lastname(journalist.getLastname())
                .birthday(journalist.getBirthday())
                .build());
    }

    private void updateFields(UpdateJournalistRequest request, JournalistEntity entity){

        entity.setFirstname(request.getFirstname());
        entity.setLastname(request.getLastname());
        entity.setBirthday(request.getBirthday());

        journalistRepository.save(entity);
    }

    @Override
    @Transactional
    public CreateJournalistResponse createJournalist(CreateJournalistRequest request) {
        JournalistEntity journalist = saveNewJournalist(request);

        return CreateJournalistResponse.builder()
                .id(journalist.getId())
                .build();
    }

    @Override
    public void updateJournalist(UpdateJournalistRequest request) {
        Optional<JournalistEntity> entity = journalistRepository.findById(request.getId());
        if (entity.isEmpty() || !requestAccessToken.getUserId().equals(entity.get().getId())) {
            throw new InvalidJournalistException("INVALID_ID");
        }

        updateFields(request, entity.get());
    }

    @Override
    @Transactional
    public void deleteJournalist(long id) {
        Optional<Journalist> optionalJournalist = getJournalist(id);

        if (optionalJournalist.isEmpty()){
            throw new InvalidJournalistException("INVALID ID");
        }

        String email = optionalJournalist.get().getAccount().getEmail();
        journalistRepository.deleteById(id);
        accountRepository.deleteById(email);
    }

    @Override
    public Optional<Journalist> getJournalist(Long id) {
        return journalistRepository.findById(id).map(JournalistConverter::convert);
    }

}
