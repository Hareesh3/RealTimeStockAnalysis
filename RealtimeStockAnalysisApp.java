// RealtimeStockAnalysisApp.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main application class for real-time stock analysis.
 * This class orchestrates fetching data, analyzing it, and displaying results.
 */
public class RealtimeStockAnalysisApp {
    // Array of stock ticker symbols to monitor
    private static final String[] SYMBOLS = {"AAPL", "GOOG", "MSFT", "AMZN", "NVDA"};
    // Interval at which to fetch new data (in seconds)
    private static final int FETCH_INTERVAL_SECONDS = 5;
    // Period for calculating the Simple Moving Average (SMA)
    private static final int SMA_PERIOD = 10;

    public static void main(String[] args) {
        // Initialize the StockService to fetch (simulated) data
        StockService stockService = new StockService();
        // Initialize the StockAnalyzer with the desired SMA period
        StockAnalyzer stockAnalyzer = new StockAnalyzer(SMA_PERIOD);

        // Create a ScheduledExecutorService to run tasks periodically.
        // Using a single thread for simplicity, but can be scaled for more complex scenarios.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Print initial application information to the console
        System.out.println("Starting Real-time Stock Analysis...");
        System.out.println("Monitoring symbols: " + String.join(", ", SYMBOLS));
        System.out.println("Fetching data every " + FETCH_INTERVAL_SECONDS + " seconds.");
        System.out.println("Calculating " + SMA_PERIOD + "-period Simple Moving Average (SMA).\n");

        // Schedule the main analysis task to run at a fixed rate.
        // The task will start immediately (initialDelay = 0) and repeat every FETCH_INTERVAL_SECONDS.
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Print a timestamp for each cycle for clarity
                System.out.println("--- " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " ---");

                // Iterate through each symbol to fetch and analyze its data
                for (String symbol : SYMBOLS) {
                    // 1. Fetch Latest Stock Data (simulated or real API call)
                    StockData latestData = stockService.getRealtimeStockPrice(symbol);
                    System.out.println("Fetched: " + latestData);

                    // 2. Analyze the Data
                    Map<String, Double> analysis = stockAnalyzer.analyze(latestData);

                    // 3. Display Analysis Results
                    Double sma = analysis.get("SMA");
                    if (sma != null && !sma.isNaN()) {
                        // If SMA is calculated and not NaN, print it
                        System.out.printf("  Analysis for %s: SMA(%.0f) = %.2f\n", symbol, (double)SMA_PERIOD, sma);
                        // Example of further analysis based on SMA:
                        // if (latestData.getPrice() > sma) {
                        //     System.out.println("    Price is currently above its " + SMA_PERIOD + "-period SMA.");
                        // } else if (latestData.getPrice() < sma) {
                        //     System.out.println("    Price is currently below its " + SMA_PERIOD + "-period SMA.");
                        // }
                    } else {
                        // If not enough data for SMA yet, inform the user
                        System.out.println("  Analysis for " + symbol + ": Not enough data for SMA yet.");
                    }
                }
                System.out.println(); // Add a blank line for better readability between cycles
            } catch (Exception e) {
                // Catch any unexpected exceptions during the task execution
                System.err.println("An error occurred during analysis cycle: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        }, 0, FETCH_INTERVAL_SECONDS, TimeUnit.SECONDS); // Initial delay, period, and time unit

        // Add a shutdown hook to gracefully terminate the scheduler when the JVM exits.
        // This ensures that background threads are properly cleaned up.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down stock analysis scheduler...");
            scheduler.shutdown(); // Initiate shutdown
            try {
                // Wait for existing tasks to terminate, up to 5 seconds
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow(); // Forcefully shut down if tasks don't terminate
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow(); // Re-interrupt and force shutdown if interrupted
            }
            System.out.println("Scheduler shut down.");
        }));

        // The main thread will exit, but the ScheduledExecutorService will keep the JVM alive
        // until it's explicitly shut down (e.g., by Ctrl+C or the shutdown hook).
    }
}
