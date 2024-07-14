package nl.fontys.s3.grp1.business;

import nl.fontys.s3.grp1.domain.dto.journalist.LoginRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
