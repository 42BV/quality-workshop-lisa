package nl._42.qualityws.cleancode.config.web.errorhandling;

import java.util.Arrays;

import org.springframework.validation.FieldError;

public class FieldErrorBody {

    private final String field;

    private final String code;

    private final String message;

    private final Object[] arguments;

    FieldErrorBody(FieldError error) {
        this.field = error.getField();
        this.code = error.getCode();
        this.message = error.getDefaultMessage();
        this.arguments = error.getArguments();
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArguments() {
        return arguments != null ? Arrays.copyOf(arguments, arguments.length) : null;
    }
}
