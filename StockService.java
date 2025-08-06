// StockService.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Simulates fetching real-time stock data.
 * In a real application, this would connect to a stock API (e.g., Alpha Vantage, Financial Modeling Prep).
 * For this example, it generates random, but plausible, stock prices.
 */
public class StockService {
    private final Random random = new Random();
    // Stores the last generated price for each symbol to simulate continuity
    private final Map<String, Double> lastPrices = new HashMap<>();

    /**
     * Constructs a new StockService and initializes some mock last prices for common symbols.
     */
    public StockService() {
        lastPrices.put("AAPL", 170.0);
        lastPrices.put("GOOG", 150.0);
        lastPrices.put("MSFT", 420.0);
    }

    /**
     * Simulates fetching the current price for a given stock symbol.
     * The price will fluctuate slightly around its last known value.
     * In a real scenario, this method would make an actual API call.
     *
     * @param symbol The stock ticker symbol (e.g., "AAPL").
     * @return A StockData object with a simulated real-time price and current timestamp.
     */
    public StockData getRealtimeStockPrice(String symbol) {
        // Get the current base price for fluctuation
        double currentPrice = lastPrices.getOrDefault(symbol, 100.0); // Default if symbol not initialized

        // Generate a random fluctuation (e.g., -1.0 to +1.0)
        double fluctuation = (random.nextDouble() - 0.5) * 2;
        double newPrice = currentPrice + fluctuation;

        // Ensure price doesn't go negative and stays within a reasonable range
        newPrice = Math.max(1.0, newPrice); // Minimum price
        newPrice = Math.min(500.0, newPrice); // Maximum price for realism

        // Update the last price for this symbol for the next simulation cycle
        lastPrices.put(symbol, newPrice);

        return new StockData(symbol, newPrice, LocalDateTime.now());
    }

    /**
     * Placeholder for a real API call.
     * This method demonstrates how you would typically make an HTTP GET request
     * and read the raw response. JSON parsing would be the next step.
     *
     * @param apiUrl The full URL of the stock API endpoint to call.
     * @return The raw JSON response as a String, or null if an error occurs during the fetch.
     */
    public String fetchRealData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Recommended to set a User-Agent

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // HTTP 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                System.err.println("GET request failed. Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching real data from API: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return null;
        }
    }
}
