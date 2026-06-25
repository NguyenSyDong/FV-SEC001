import java.io.File;

public class Main {
    public static void main(String[] args) {
        String inputFile = "src/dataFiles/ad_data.csv";
        String outputFolder = "results";
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        AdPerformanceAggregator aggregator = new AdPerformanceAggregator();
        try {
            aggregator.process(inputFile, outputFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}