package nl.fontys.s3.grp1.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.LoginService;
import nl.fontys.s3.grp1.business.exception.InvalidCredentialsException;
import nl.fontys.s3.grp1.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.grp1.configuration.security.token.exception.InvalidAccessTokenException;
import nl.fontys.s3.grp1.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.grp1.domain.dto.journalist.LoginRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.LoginResponse;
import nl.fontys.s3.grp1.persistence.JournalistRepository;
import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JournalistRepository journalistRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    @Override
    public LoginResponse login(LoginRequest request) {
        JournalistEntity journalist = journalistRepository.findByAccountEmail(request.getEmail()).orElseThrow(InvalidCredentialsException::new);

        if (!matchesPassword(request.getPassword(), journalist.getAccount().getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(journalist);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(JournalistEntity journalist) {
        try {
            assert journalist != null;
            Long userId = journalist.getId();
            String role = "JOURNALIST";

            return accessTokenEncoder.encode(
                    new AccessTokenImpl(journalist.getAccount().getEmail(), userId, role));
        } catch (Exception e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}
