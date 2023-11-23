package db.testdata;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.callback.BaseCallback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.flywaydb.core.internal.exception.FlywaySqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProductTestDataCallback extends BaseCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductTestDataCallback.class);

    @Override
    public boolean supports(final Event event, final Context context) {
        return event == Event.AFTER_MIGRATE;
    }

    @Override
    public void handle(final Event event, final Context context) {
        LOGGER.info("Running \"Insert Test Data\" callback");

        final var resource = context.getConfiguration().getClassLoader().getResource("db/testdata/PRODUCTS.csv");
        Assert.notNull(resource, "Failed to find CSV file");

        try (final var reader = new BufferedReader(new FileReader(resource.getFile()))) {
            final var columnList = Arrays.stream(reader.readLine().split("\\s*,\\s*"))
                    .collect(Collectors.toList());
            columnList.add("CREATED");
            final var columns = String.join(",", columnList);

            try (final var statement = context.getConnection().createStatement()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final var valueList = Arrays.stream(line.split("\\s*,\\s*"))
                            .map(value -> "'" + value + "'")
                            .collect(Collectors.toList());
                    valueList.add("CURRENT_TIMESTAMP");
                    final var values = String.join(",", valueList);

                    try (final var resultSet = statement.executeQuery(
                            "SELECT * FROM PRODUCTS WHERE PRODUCT_ID=" + valueList.get(0))) {
                        resultSet.last();
                        if (resultSet.getRow() > 0) {
                            LOGGER.warn("Test data already exists for product \"{}\"", valueList.get(1));
                        }
                    } catch (SQLException e) {
                        throw new FlywaySqlException("Failed to execute query for current statement", e);
                    }

                    LOGGER.debug("Inserting test data row for product \"{}\"", valueList.get(1));
                    try {
                        statement.executeUpdate("INSERT INTO PRODUCTS (" + columns + ") VALUES (" + values + ")");
                    } catch (SQLException e) {
                        throw new FlywaySqlException("Failed to execute update for current statement", e);
                    }
                }

            } catch (SQLException e) {
                throw new FlywaySqlException("Failed to create statement for current connection", e);
            }
        } catch (FileNotFoundException e) {
            throw new FlywayException("Failed to find CSV file", e);
        } catch (IOException e) {
            throw new FlywayException("Failed to read CSV file", e);
        }
    }
}