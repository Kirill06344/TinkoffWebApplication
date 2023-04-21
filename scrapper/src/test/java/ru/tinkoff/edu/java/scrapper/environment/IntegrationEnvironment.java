package ru.tinkoff.edu.java.scrapper.environment;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class IntegrationEnvironment implements AutoCloseable {
    protected static final PostgreSQLContainer<?> MY_POSTGRES_CONTAINER;

    private static final Path PATH_TO_MIGRATIONS = new File(".").toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");

    static {
        MY_POSTGRES_CONTAINER = new PostgreSQLContainer<>(PostgreSQLTestImages.POSTGRES_TEST_IMAGE);
        MY_POSTGRES_CONTAINER.start();
        try {
            startMigrations();
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startMigrations() throws SQLException, LiquibaseException, FileNotFoundException {
        Connection connection = MY_POSTGRES_CONTAINER.createConnection("");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(PATH_TO_MIGRATIONS), database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    @Override
    public void close() {
        MY_POSTGRES_CONTAINER.close();
    }
}
