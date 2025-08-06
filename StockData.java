// StockData.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single stock data point at a specific time.
 * This class is a Plain Old Java Object (POJO) to hold stock information.
 */
public class StockData {
    private String symbol;
    private double price;
    private LocalDateTime timestamp;

    /**
     * Constructs a new StockData object.
     *
     * @param symbol    The ticker symbol of the stock (e.g., "AAPL").
     * @param price     The price of the stock at the given timestamp.
     * @param timestamp The LocalDateTime when this stock data was recorded.
     */
    public StockData(String symbol, double price, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    /**
     * Gets the stock ticker symbol.
     * @return The symbol as a String.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the stock price.
     * @return The price as a double.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the timestamp of the stock data.
     * @return The timestamp as a LocalDateTime object.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the StockData object.
     * @return A formatted string containing the symbol, price, and timestamp.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Symbol: %s, Price: %.2f, Time: %s", symbol, price, timestamp.format(formatter));
    }
}
