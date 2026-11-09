package validation;

import java.util.List;
import java.util.Objects;

public final class ValidationResult {
    private final List<ValidationError> errors;

    public ValidationResult(List<ValidationError> errors) {
        Objects.requireNonNull(errors, "errors must not be null");
        this.errors = List.copyOf(errors);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ValidationResult{valid=" + isValid() + ", errors=" + errors + '}';
    }
}