package nl._42.qualityws.cleancode.config.web.errorhandling;

import java.util.List;

public class ErrorResult {

    private final List<GlobalErrorBody> globalErrors;

    private final List<FieldErrorBody> fieldErrors;

    ErrorResult(List<GlobalErrorBody> globalErrors, List<FieldErrorBody> fieldErrors) {
        this.globalErrors = globalErrors;
        this.fieldErrors = fieldErrors;
    }

    public List<GlobalErrorBody> getGlobalErrors() {
        return globalErrors;
    }

    public List<FieldErrorBody> getFieldErrors() {
        return fieldErrors;
    }
}
