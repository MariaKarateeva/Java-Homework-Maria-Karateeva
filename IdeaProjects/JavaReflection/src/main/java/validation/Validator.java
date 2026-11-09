package validation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Validator {

    private Validator() {
    }

    public static ValidationResult validate(Object object) {
        Objects.requireNonNull(object, "object must not be null");

        List<ValidationError> errors = new ArrayList<>();
        for (Class<?> type = object.getClass();
             type != null && type != Object.class;
             type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                validateField(object, field, errors);
            }
        }
        return new ValidationResult(errors);
    }

    private static void validateField(Object object, Field field, List<ValidationError> errors) {
        NotNull notNull = field.getAnnotation(NotNull.class);
        Size size = field.getAnnotation(Size.class);
        Range range = field.getAnnotation(Range.class);
        Email email = field.getAnnotation(Email.class);

        if (notNull == null && size == null && range == null && email == null) {
            return;
        }

        if (size != null) {
            checkSizeConfig(field, size);
        }
        if (range != null) {
            checkRangeConfig(field, range);
        }
        if (email != null) {
            requireCharSequence(field, "Email");
        }

        Object value = readValue(object, field);
        String name = field.getName();

        if (notNull != null && value == null) {
            errors.add(new ValidationError(name, "NotNull", notNull.message()));
        }
        if (size != null && value != null) {
            int length = ((CharSequence) value).length();
            if (length < size.min() || length > size.max()) {
                errors.add(new ValidationError(name, "Size", size.message()));
            }
        }
        if (range != null && value != null) {
            double number = ((Number) value).doubleValue();
            if (Double.isNaN(number) || number < range.min() || number > range.max()) {
                errors.add(new ValidationError(name, "Range", range.message()));
            }
        }
        if (email != null && value != null && !isValidEmail((CharSequence) value)) {
            errors.add(new ValidationError(name, "Email", email.message()));
        }
    }

    private static Object readValue(Object object, Field field) {
        if (!field.trySetAccessible()) {
            throw new ValidationConfigurationException(
                    "Field is not accessible: " + describe(field));
        }
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ValidationConfigurationException(
                    "Cannot read field: " + describe(field), e);
        }
    }

    private static String describe(Field field) {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }

    private static void checkSizeConfig(Field field, Size size) {
        requireCharSequence(field, "Size");
        if (size.min() < 0 || size.min() > size.max()) {
            throw new ValidationConfigurationException(
                    "Invalid @Size bounds [" + size.min() + ", " + size.max() + "] on "
                            + describe(field));
        }
    }

    private static void checkRangeConfig(Field field, Range range) {
        if (!isNumericType(field.getType())) {
            throw new ValidationConfigurationException(
                    "@Range is not supported for type " + field.getType().getName()
                            + " on " + describe(field));
        }
        double min = range.min();
        double max = range.max();
        if (Double.isNaN(min) || Double.isNaN(max)
                || Double.isInfinite(min) || Double.isInfinite(max) || min > max) {
            throw new ValidationConfigurationException(
                    "Invalid @Range bounds [" + min + ", " + max + "] on " + describe(field));
        }
    }

    private static void requireCharSequence(Field field, String annotation) {
        if (!CharSequence.class.isAssignableFrom(field.getType())) {
            throw new ValidationConfigurationException(
                    "@" + annotation + " is not supported for type " + field.getType().getName()
                            + " on " + describe(field));
        }
    }

    private static boolean isNumericType(Class<?> type) {
        return type == byte.class || type == short.class || type == int.class
                || type == long.class || type == float.class || type == double.class
                || Number.class.isAssignableFrom(type);
    }

    private static boolean isValidEmail(CharSequence value) {
        String s = value.toString();
        if (s.isBlank()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        int at = s.indexOf('@');
        if (at <= 0 || at != s.lastIndexOf('@')) {
            return false;
        }
        String domain = s.substring(at + 1);
        return !domain.isEmpty() && domain.contains(".");
    }
}