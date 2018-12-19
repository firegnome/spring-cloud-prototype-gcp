package ch.hsr.sai14sa12.blogmicroservice.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotAuthorizedException extends RuntimeException {
}
