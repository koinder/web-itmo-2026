package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                    "SELECT * FROM `Article` ORDER BY `creationTime` DESC, `id` DESC")) {

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Article article = new Article();
                        article.setId(resultSet.getLong("id"));
                        article.setUserId(resultSet.getLong("userId"));
                        article.setTitle(resultSet.getString("title"));
                        article.setText(resultSet.getString("text"));
                        article.setCreationTime(resultSet.getTimestamp("creationTime"));
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }

        return articles;
    }
}