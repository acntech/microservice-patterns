package db.testdata;

import no.acntech.product.model.Currency;
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
    private static final List<String> PRODUCT_NAMES = Arrays.asList("Product 1", "Product 2", "Product 3", "Product 4", "Product 5", "Product 6", "Product 7", "Product 8", "Product 9");

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.AFTER_MIGRATE;
    }

    @Override
    public void handle(Event event, Context context) {
        LOGGER.info("Running \"Insert Test Data\" callback");
        try (Statement statement = context.getConnection().createStatement()) {
            var random = new Random();

            random.nextLong(10000);

            PRODUCT_NAMES.forEach(productName -> {
                var rowCount = 0;
                try (ResultSet resultSet = statement.executeQuery(
                        String.format("SELECT * FROM PRODUCTS WHERE NAME='%s'", productName))) {
                    resultSet.last();
                    rowCount = resultSet.getRow();
                } catch (SQLException e) {
                    throw new FlywaySqlException("Failed to execute query for current statement", e);
                }

                if (rowCount > 0) {
                    LOGGER.info("Test data already exists for product \"{}\"", productName);
                } else {
                    LOGGER.info("Inserting test data row for product \"{}\"", productName);
                    try {
                        long stock = random.nextLong(10000);
                        long price = random.nextLong(10, 10000);
                        statement.executeUpdate(
                                String.format("INSERT INTO PRODUCTS (PRODUCT_ID, NAME, STOCK, PRICE, CURRENCY, CREATED) VALUES ('%s', '%s', '%s', '%s', '%s', CURRENT_TIMESTAMP)"
                                        , UUID.randomUUID(), productName, stock, price, Currency.USD.name()));
                    } catch (SQLException e) {
                        throw new FlywaySqlException("Failed to execute update for current statement", e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new FlywaySqlException("Failed to create statement for current connection", e);
        }
    }
}