package nl._42.qualityws.cleancode.config.web.errorhandling;

import java.util.Arrays;

import org.springframework.validation.ObjectError;

public class GlobalErrorBody {

    private final String object;

    private final String code;

    private final String message;

    private final Object[] arguments;

    GlobalErrorBody(ObjectError error) {
        this.object = error.getObjectName();
        this.code = error.getCode();
        this.message = error.getDefaultMessage();
        this.arguments = error.getArguments();
    }

    public String getObject() {
        return object;
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
