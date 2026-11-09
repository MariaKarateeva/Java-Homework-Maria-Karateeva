package validation;

public record ValidationError(String fieldName, String annotationName, String message) {
}