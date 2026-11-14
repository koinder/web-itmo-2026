package ru.itmo.wp.model.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.TalkRepository;

import java.util.List;
import java.util.ArrayList;

public class TalkRepositoryImpl implements TalkRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(Talk talk) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `Talk` (`sourceUserId`, `targetUserId`, `text`, `creationTime`) "
                            + "VALUES (?, ?, ?, NOW())")) {

                statement.setLong(1, talk.getSourceUserId());
                statement.setLong(2, talk.getTargetUserId());
                statement.setString(3, talk.getText());

                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new RepositoryException("Can't save Talk.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save Talk.", e);
        }
    }

    @Override
    public List<Talk> findByUserId(long userId) {
        List<Talk> talks = new ArrayList<>();

        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Talk` WHERE `sourceUserId`=? OR `targetUserId`=? ORDER BY `creationTime`, `id`")) {
                statement.setLong(1, userId);
                statement.setLong(2, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Talk talk = new Talk();
                        talk.setId(resultSet.getLong("id"));
                        talk.setSourceUserId(resultSet.getLong("sourceUserId"));
                        talk.setTargetUserId(resultSet.getLong("targetUserId"));
                        talk.setText(resultSet.getString("text"));
                        talk.setCreationTime(resultSet.getTimestamp("creationTime"));
                        talks.add(talk);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Talks.", e);
        }

        return talks;
    }
}
