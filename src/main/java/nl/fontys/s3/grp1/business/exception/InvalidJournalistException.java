package nl.fontys.s3.grp1.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidJournalistException extends ResponseStatusException {
    public InvalidJournalistException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
