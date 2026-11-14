package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.List;

public class TalkService {
    private final TalkRepository talkRepository = new TalkRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    public List<Talk> findByUserId(long userId) {
        return talkRepository.findByUserId(userId);
    }
    
    public void validate(Talk talk) throws ValidationException {
        String text = talk.getText();

        if (text == null || text.isBlank()) {
            throw new ValidationException("Message text is required");
        }
        if (userRepository.find(talk.getTargetUserId()) == null) {
            throw new ValidationException("Recipient does not exist");
        }
        if (text.length() > 2500) {
            throw new ValidationException("Message is too long");
        }
    }

    public void save(Talk talk) throws ValidationException {
        validate(talk);
        talkRepository.save(talk);
    }
}