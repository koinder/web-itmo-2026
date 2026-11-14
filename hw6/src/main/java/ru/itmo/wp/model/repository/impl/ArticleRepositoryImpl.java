package ru.itmo.wp.model.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

public class ArticleRepositoryImpl implements ArticleRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(Article article) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `Article` (`userId`, `title`, `text`, `creationTime`) "
                            + "VALUES (?, ?, ?, NOW())")) {

                statement.setLong(1, article.getUserId());
                statement.setString(2, article.getTitle());
                statement.setString(3, article.getText());

                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new RepositoryException("Can't save Article.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save Article.", e);
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();

        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Article` WHERE `hidden`=FALSE ORDER BY `creationTime` DESC, `id` DESC")) {

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Article article = new Article();
                        article.setId(resultSet.getLong("id"));
                        article.setUserId(resultSet.getLong("userId"));
                        article.setTitle(resultSet.getString("title"));
                        article.setText(resultSet.getString("text"));
                        article.setCreationTime(resultSet.getTimestamp("creationTime"));
                        article.setHidden(resultSet.getBoolean("hidden"));
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }

        return articles;
    }
    
    @Override
    public List<Article> findByUserId(long userId) {
        List<Article> articles = new ArrayList<>();

        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Article` WHERE `userId`=? ORDER BY `creationTime`, `id`")) {
                statement.setLong(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Article article = new Article();
                        article.setId(resultSet.getLong("id"));
                        article.setUserId(resultSet.getLong("userId"));
                        article.setTitle(resultSet.getString("title"));
                        article.setText(resultSet.getString("text"));
                        article.setCreationTime(resultSet.getTimestamp("creationTime"));
                        article.setHidden(resultSet.getBoolean("hidden"));
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }

        return articles;
    }

    @Override
    public void updateHidden(long id, long userId, boolean hidden) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `Article` SET `hidden`=? WHERE `id`=? AND `userId`=?")) {

                statement.setBoolean(1, hidden);
                statement.setLong(2, id);
                statement.setLong(3, userId);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't update Article visibility.", e);
        }
    }

    @Override
    public Article find(long id) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Article` WHERE `id`=?")) {

                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    Article article = new Article();
                    article.setId(resultSet.getLong("id"));
                    article.setUserId(resultSet.getLong("userId"));
                    article.setTitle(resultSet.getString("title"));
                    article.setText(resultSet.getString("text"));
                    article.setCreationTime(resultSet.getTimestamp("creationTime"));
                    article.setHidden(resultSet.getBoolean("hidden"));

                    return article;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Article.", e);
        }
    }
}