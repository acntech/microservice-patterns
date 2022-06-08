package db.testdata;

import org.flywaydb.core.api.callback.BaseCallback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.flywaydb.core.internal.exception.FlywaySqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataCallback extends BaseCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataCallback.class);
    private static final List<String> FIRST_NAMES = Arrays.asList("Betty", "Hayden", "Cortney", "Joey", "Britney", "Frank", "Sue", "Ellen", "Mark", "Andy", "Jill", "Steve", "Holly", "Daniel", "Eric", "Irene");
    private static final List<String> LAST_NAMES = Arrays.asList("Taylor", "Ross", "Carter", "Mitchell", "Campbell", "Jackson", "Wilson", "Morris", "Williams", "Hall", "Adams", "Baker", "Roberts", "Howard", "Fisher");
    private static final String ADDRESS = "4 Privet Drive";
    private static final int ROWS_TO_INSERT = 10;

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.AFTER_MIGRATE;
    }

    @Override
    public void handle(Event event, Context context) {
        LOGGER.info("Running \"Test Data\" callback");
        try (Statement statement = context.getConnection().createStatement()) {
            var random = new Random();
            for (int i = 1; i <= ROWS_TO_INSERT; i++) {
                var firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
                var lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));

                var rowCount = 0;
                try (ResultSet resultSet = statement.executeQuery(
                        String.format("SELECT * FROM CUSTOMERS WHERE FIRST_NAME='%s' AND LAST_NAME='%s'", firstName, lastName))) {
                    resultSet.last();
                    rowCount = resultSet.getRow();
                } catch (SQLException e) {
                    throw new FlywaySqlException("Failed to execute query for current statement", e);
                }

                if (rowCount > 0) {
                    LOGGER.info("Test data already exists for customer \"{} {}\"", firstName, lastName);
                    i--;
                } else {
                    LOGGER.info("Inserting test data row {} for customer \"{} {}\"", i, firstName, lastName);
                    try {
                        statement.executeUpdate(
                                String.format("INSERT INTO CUSTOMERS (CUSTOMER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CREATED) VALUES ('%s', '%s', '%s', '%s', CURRENT_TIMESTAMP)",
                                        UUID.randomUUID(), firstName, lastName, ADDRESS));
                    } catch (SQLException e) {
                        throw new FlywaySqlException("Failed to execute update for current statement", e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new FlywaySqlException("Failed to create statement for current connection", e);
        }
    }
}