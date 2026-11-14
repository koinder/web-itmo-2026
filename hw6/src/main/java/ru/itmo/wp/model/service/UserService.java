package ru.itmo.wp.model.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

public class UserService {
    private static final String PASSWORD_SALT = "1174f9d7bc21e00e9a5fd0a783a44c9a9f73413c";

    private final UserRepository userRepository = new UserRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        if (Strings.isNullOrEmpty(user.getLogin())) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8 letters");
        }

        String email = user.getEmail();
        if (Strings.isNullOrEmpty(email)) {
            throw new ValidationException("Email is required");
        }
        if ((email.indexOf("@") == -1) || (email.indexOf("@") != email.lastIndexOf("@"))) {
            throw new ValidationException("Email must contain exactly one @");
        }
        
        if (userRepository.byLoginOrEmail(user.getLogin(), email)) {
            throw new ValidationException("Login or email is already in use");
        }

        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) {
            throw new ValidationException("Password can't be longer than 64 characters");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Passwords do not match");
        }

    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        if (Strings.isNullOrEmpty(loginOrEmail)) {
            throw new ValidationException("Login or email is required");
        }
        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (findByLoginAndPassword(loginOrEmail, password) == null) {
            throw new ValidationException("Invalid login, email or password");
        }
    }

    public User findByLoginAndPassword(String loginOrEmail, String password) {
        return userRepository.findByLoginAndPasswordSha(loginOrEmail, getPasswordSha(password));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long findCount() {
        return userRepository.findCount();
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public User find(long id) {
        return userRepository.find(id);
    }

    public void updateAdmin(long currentUserId, long id, boolean admin) throws ValidationException {
        User currentUser = userRepository.find(currentUserId);
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new ValidationException("Administrator rights are required");
        }
        if (userRepository.find(id) == null) {
            throw new ValidationException("User does not exist");
        }
        userRepository.updateAdmin(id, admin);
    }
}
