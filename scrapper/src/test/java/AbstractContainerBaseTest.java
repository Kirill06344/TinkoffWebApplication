import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public abstract class AbstractContainerBaseTest {
    static final PostgreSQLContainer<?> MY_POSTGRES_CONTAINER;

    static {
        MY_POSTGRES_CONTAINER = new PostgreSQLContainer<>(PostgreSQLTestImages.POSTGRES_TEST_IMAGE)
                .withUsername("postgres")
                .withPassword("password")
                .withDatabaseName("scrapper");
        MY_POSTGRES_CONTAINER.start();
    }

    protected DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(container.getJdbcUrl());
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(config);
    }
}
