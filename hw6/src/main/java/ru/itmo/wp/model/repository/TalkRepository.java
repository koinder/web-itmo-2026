package ru.itmo.wp.model.repository;
import java.util.List;

import ru.itmo.wp.model.domain.Talk;

public interface TalkRepository {
    void save(Talk talk);

    List<Talk> findByUserId(long userId);

}
