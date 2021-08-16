package source.controllers.authorization.validation;

import org.springframework.beans.factory.annotation.Autowired;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator
        implements ConstraintValidator<ValidEmail, String> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try {
            return validateEmail(email);
        } catch (AccStorageException e) {
            return false;
        }
    }

    private boolean validateEmail(String email) throws AccStorageException {
        return !accountRepository.exist(email);
    }
}