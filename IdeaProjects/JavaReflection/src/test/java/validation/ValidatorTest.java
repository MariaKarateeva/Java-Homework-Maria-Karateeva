package validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    static class User {
        @NotNull(message = "Name cannot be null")
        @Size(min = 2, max = 50) private String name;
        @Range(min = 18, max = 120) private int age;
        @Email private String email;

        User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }

    static class Opt {
        @Size(min = 2, max = 5) String s;
        @Range(min = 0, max = 10) Double n;
        @Email String e;

        Opt(String s, Double n, String e) {
            this.s = s;
            this.n = n;
            this.e = e;
        }
    }

    static class Parent { @NotNull private String p; @NotNull static String st; }
    static class Child extends Parent { @NotNull private String c; }

    static class BadSizeMin { @Size(min = -1, max = 2) String s; }
    static class BadSizeBounds { @Size(min = 5, max = 2) String s; }
    static class BadRangeBounds { @Range(min = 10, max = 1) int n; }
    static class NanRangeBound { @Range(min = Double.NaN, max = 1) int n; }
    static class SizeOnInteger { @Size(min = 1, max = 2) Integer n; }
    static class EmailOnInteger { @Email Integer n; }

    private static Set<String> keys(Object o) {
        return Validator.validate(o).getErrors().stream()
                .map(e -> e.fieldName() + ":" + e.annotationName())
                .collect(Collectors.toSet());
    }

    @Test
    void validObjectHasNoErrors() {
        ValidationResult result = Validator.validate(new User("John", 30, "john@mail.com"));
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void eachAnnotationDetectsViolation() {
        assertEquals(Set.of("name:NotNull"), keys(new User(null, 30, "a@b.c")));
        assertEquals(Set.of("name:Size"), keys(new User("J", 30, "a@b.c")));
        assertEquals(Set.of("age:Range"), keys(new User("John", 5, "a@b.c")));
        assertEquals(Set.of("email:Email"), keys(new User("John", 30, "bad")));
    }

    @Test
    void allViolationsAreCollectedWithDetails() {
        assertEquals(Set.of("name:Size", "age:Range", "email:Email"), keys(new User("J", 5, "bad")));

        ValidationError error = Validator.validate(new User(null, 30, null)).getErrors().get(0);
        assertEquals("name", error.fieldName());
        assertEquals("NotNull", error.annotationName());
        assertEquals("Name cannot be null", error.message());
    }

    @Test
    void sizeAndRangeBoundariesAreInclusive() {
        assertTrue(Validator.validate(new Opt("ab", 0.0, null)).isValid());
        assertTrue(Validator.validate(new Opt("abcde", 10.0, null)).isValid());
        assertEquals(Set.of("s:Size", "n:Range"), keys(new Opt("a", -1.0, null)));
        assertEquals(Set.of("s:Size", "n:Range"), keys(new Opt("abcdef", 11.0, null)));
    }

    @Test
    void nullIsSkippedAndNaNAndBlankEmailAreInvalid() {
        assertTrue(Validator.validate(new Opt(null, null, null)).isValid());
        assertEquals(Set.of("n:Range"), keys(new Opt(null, Double.NaN, null)));
        assertEquals(Set.of("e:Email"), keys(new Opt(null, null, "")));
        assertEquals(Set.of("e:Email"), keys(new Opt(null, null, "   ")));
        assertEquals(Set.of("e:Email"), keys(new Opt(null, null, "no-at-sign")));
    }

    @Test
    void invalidInputThrowsExceptions() {
        assertThrows(NullPointerException.class, () -> Validator.validate(null));
        Object[] badModels = {new BadSizeMin(), new BadSizeBounds(), new BadRangeBounds(),
                new NanRangeBound(), new SizeOnInteger(), new EmailOnInteger()};
        for (Object model : badModels) {
            assertThrows(ValidationConfigurationException.class, () -> Validator.validate(model));
        }
    }

    @Test
    void privateAndInheritedFieldsAreValidatedStaticIgnored() {
        assertEquals(Set.of("p:NotNull", "c:NotNull"), keys(new Child()));
    }

    @Test
    void resultIsImmutable() {
        ValidationResult result = Validator.validate(new User("J", 5, "bad"));
        assertThrows(UnsupportedOperationException.class, () -> result.getErrors().clear());

        List<ValidationError> source = new ArrayList<>(result.getErrors());
        ValidationResult copy = new ValidationResult(source);
        source.clear();
        assertEquals(3, copy.getErrors().size());
        assertThrows(NullPointerException.class, () -> new ValidationResult(null));
    }
}