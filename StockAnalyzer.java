// StockAnalyzer.java
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Performs analysis on stock data, such as calculating technical indicators.
 */
public class StockAnalyzer {
    // Stores historical price data for each stock symbol using a LinkedList
    // LinkedList is efficient for adding to front and removing from end (sliding window)
    private final Map<String, LinkedList<Double>> historicalPrices = new HashMap<>();
    private final int smaPeriod; // The period (number of data points) for Simple Moving Average calculation

    /**
     * Constructs a new StockAnalyzer with a specified SMA period.
     * @param smaPeriod The number of recent prices to use for SMA calculation.
     */
    public StockAnalyzer(int smaPeriod) {
        if (smaPeriod <= 0) {
            throw new IllegalArgumentException("SMA period must be a positive integer.");
        }
        this.smaPeriod = smaPeriod;
    }

    /**
     * Adds a new stock data point to the historical record and performs analysis.
     * This method calculates the Simple Moving Average (SMA) if enough data points are available.
     *
     * @param stockData The latest StockData object (symbol, price, timestamp).
     * @return A map containing the analysis results (e.g., "SMA" -> calculated_value).
     * Returns Double.NaN for SMA if not enough data points are available yet.
     */
    public Map<String, Double> analyze(StockData stockData) {
        String symbol = stockData.getSymbol();
        double currentPrice = stockData.getPrice();

        // Get or create the LinkedList for this symbol's historical prices
        LinkedList<Double> prices = historicalPrices.computeIfAbsent(symbol, k -> new LinkedList<>());

        // Add the current price to the beginning of the list
        prices.addFirst(currentPrice);

        // Maintain the fixed window size for SMA calculation
        if (prices.size() > smaPeriod) {
            prices.removeLast(); // Remove the oldest price from the end
        }

        Map<String, Double> analysisResults = new HashMap<>();
        // Calculate SMA only if we have enough data points for the specified period
        if (prices.size() == smaPeriod) {
            double sma = calculateSMA(prices);
            analysisResults.put("SMA", sma);
        } else {
            // If not enough data, indicate that SMA is not applicable yet
            analysisResults.put("SMA", Double.NaN); // Not a Number
        }
        return analysisResults;
    }

    /**
     * Calculates the Simple Moving Average (SMA) for a given list of prices.
     *
     * @param prices A list of double values representing historical prices.
     * @return The calculated SMA, or 0.0 if the list is null or empty.
     */
    private double calculateSMA(List<Double> prices) {
        if (prices == null || prices.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Double price : prices) {
            sum += price;
        }
        return sum / prices.size();
    }
}
