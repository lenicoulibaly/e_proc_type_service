package lenicorp.typeservices.controller.exceptions;

import lombok.Getter;
import lombok.Setter;

public class AppException extends RuntimeException
{
    @Getter @Setter
    private String message;

    public AppException(String message) {
        super(message);
        this.message = message;
    }
}
