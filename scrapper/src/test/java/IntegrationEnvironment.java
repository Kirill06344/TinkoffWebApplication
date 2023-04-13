import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class IntegrationEnvironment implements AutoCloseable {
    static final PostgreSQLContainer<?> MY_POSTGRES_CONTAINER;

    private static final Path PATH_TO_MIGRATIONS = new File(".").toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");

    static {
        MY_POSTGRES_CONTAINER = new PostgreSQLContainer<>(PostgreSQLTestImages.POSTGRES_TEST_IMAGE);
        MY_POSTGRES_CONTAINER.start();
        DataSource dataSource = getDataSource(MY_POSTGRES_CONTAINER);
        try {
            startMigrations(dataSource);
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(container.getJdbcUrl());
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(config);
    }

    private static void startMigrations(DataSource dataSource) throws SQLException, LiquibaseException, FileNotFoundException {
        Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(PATH_TO_MIGRATIONS), database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    @Override
    public void close() {
        MY_POSTGRES_CONTAINER.close();
    }
}
