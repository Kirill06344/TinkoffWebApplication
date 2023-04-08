import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class IntegrationEnvironment extends AbstractContainerBaseTest {

    private final Path PATH_TO_MIGRATIONS = new File(".").toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");

    @Test
    public void test() {
        DataSource dataSource = getDataSource(MY_POSTGRES_CONTAINER);
        assertDoesNotThrow(() -> startMigrations(dataSource));
    }

    public void startMigrations(DataSource dataSource) throws SQLException, LiquibaseException, FileNotFoundException {
        Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(PATH_TO_MIGRATIONS), database);
        log.info(liquibase.getChangeLogFile());
        liquibase.update(new Contexts(), new LabelExpression());
    }
}
