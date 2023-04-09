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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class IntegrationEnvironment extends AbstractContainerBaseTest {

    private final Path PATH_TO_MIGRATIONS = new File(".").toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");

    @Test
    public void implement_migrationsIntoDatabase() {
        DataSource dataSource = getDataSource(MY_POSTGRES_CONTAINER);
        assertDoesNotThrow(() -> startMigrations(dataSource));
        try {
            assertTrue(tableExists(dataSource.getConnection(), "chat"));
            assertTrue(tableExists(dataSource.getConnection(), "link"));
            assertTrue(tableExists(dataSource.getConnection(), "chat_link"));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            fail("Connection error!");
        }
    }

    public void startMigrations(DataSource dataSource) throws SQLException, LiquibaseException, FileNotFoundException {
        Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(PATH_TO_MIGRATIONS), database);
        log.info(liquibase.getChangeLogFile());
        liquibase.update(new Contexts(), new LabelExpression());
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }
}
