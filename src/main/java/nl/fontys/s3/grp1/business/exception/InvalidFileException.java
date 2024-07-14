package nl.fontys.s3.grp1.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidFileException extends ResponseStatusException {
    public InvalidFileException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
