package ru.tinkoff.edu.java.scrapper;

import org.junit.Test;
import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class AvailabilityOfTablesTest extends IntegrationEnvironment {

    @Test
    public void implement_migrationsIntoDatabase() {
        try(Connection connection = MY_POSTGRES_CONTAINER.createConnection("")) {
            assertTrue(tableExists(connection, "chat"));
        } catch (SQLException e) {
            fail("Unable to work with db!");
        }
    }



    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }
}
