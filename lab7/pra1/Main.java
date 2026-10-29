import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    @MaxLength(20)
    String username;

    @NotBlank
    @MaxLength(30)
    String email;

    @NotBlank
    @MaxLength(15)
    String password;

    SignupForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

class Validator {
    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String fieldName = field.getName();

                if (field.isAnnotationPresent(NotBlank.class)) {
                    if (value == null || value.toString().trim().isEmpty()) {
                        errors.add(fieldName + " must not be blank");
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength annotation = field.getAnnotation(MaxLength.class);
                    int max = annotation.value();
                    if (value != null && value.toString().length() > max) {
                        errors.add(fieldName + " must not exceed " + max + " characters");
                    }
                }
            } catch (IllegalAccessException e) {
                errors.add("Cannot access field: " + field.getName());
            }
        }

        return errors;
    }
}

public class Main {
    public static void main(String[] args) {
        SignupForm form = new SignupForm(
            "",
            "verylongemailaddress123456789@example.com",
            "12345678901234567890"
        );

        List<String> errors = Validator.validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid.");
        } else {
            System.out.println("Validation Errors:");
            for (String error : errors) {
                System.out.println("- " + error);
            }
        }
    }
}
