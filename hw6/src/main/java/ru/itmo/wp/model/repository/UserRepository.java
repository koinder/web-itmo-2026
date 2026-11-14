package ru.itmo.wp.model.repository;

import java.util.List;

import ru.itmo.wp.model.domain.User;

public interface UserRepository {
    User find(long id);

    User findByLogin(String login);

    User findByLoginAndPasswordSha(String login, String passwordSha);

    User findByEmail(String email);
    
    List<User> findAll();

    void save(User user, String passwordSha);
    
    long findCount();

    void updateAdmin(long id, boolean admin);
}
