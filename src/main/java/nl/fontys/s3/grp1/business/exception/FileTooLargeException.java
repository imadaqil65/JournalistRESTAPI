package nl.fontys.s3.grp1.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileTooLargeException extends ResponseStatusException {
    public FileTooLargeException() {
        super(HttpStatus.PAYLOAD_TOO_LARGE, "MAXIMUM_SIZE_EXCEEDED");
    }
}
