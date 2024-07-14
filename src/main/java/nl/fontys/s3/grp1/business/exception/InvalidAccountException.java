package nl.fontys.s3.grp1.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAccountException extends ResponseStatusException {
    public InvalidAccountException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
