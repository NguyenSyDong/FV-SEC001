import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdPerformanceAggregator {
    public void process(String inputFile, String outputFolder) throws IOException {
        Map<String, CampaignStats> campaignMap = aggregateCampaignStats(inputFile);
        List<Map.Entry<String, CampaignStats>> topCtr = getTop10ByCtr(campaignMap);
        List<Map.Entry<String, CampaignStats>> topCpa = getTop10ByCpa(campaignMap);
        writeResult(topCtr, outputFolder + "/top10_ctr.csv");
        writeResult(topCpa, outputFolder + "/top10_cpa.csv");
    }

    private Map<String, CampaignStats> aggregateCampaignStats(String inputFile) throws IOException {
        Map<String, CampaignStats> campaignStatsMap = new HashMap<String, CampaignStats>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 6) {
                    continue;
                }
                String campaignId = columns[0];
                long impressions = Long.parseLong(columns[2]);
                long clicks = Long.parseLong(columns[3]);
                double spend = Double.parseDouble(columns[4]);
                long conversions = Long.parseLong(columns[5]);
                CampaignStats stats = campaignStatsMap.computeIfAbsent(campaignId, k -> new CampaignStats());
                stats.add(impressions,clicks,spend,conversions);
            }
        }
        return campaignStatsMap;
    }

    private List<Map.Entry<String, CampaignStats>> getTop10ByCtr(Map<String, CampaignStats> campaignMap) {
        return campaignMap.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue().getCtr(), a.getValue().getCtr()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<String, CampaignStats>> getTop10ByCpa(Map<String, CampaignStats> campaignMap) {
        return campaignMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCpa() != null)
                .sorted((a, b) -> Double.compare( a.getValue().getCpa(), b.getValue().getCpa()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private void writeResult(List<Map.Entry<String, CampaignStats>> result,  String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println( "campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA");
            for (Map.Entry<String, CampaignStats> entry : result) {
                CampaignStats stats = entry.getValue();
                String cpa = stats.getCpa() == null ? "" : String.format("%.4f",stats.getCpa());
                writer.printf("%s,%d,%d,%.2f,%d,%.4f,%s%n", entry.getKey(), stats.getImpressions(), stats.getClicks(),
                        stats.getSpend(), stats.getConversions(), stats.getCtr(), cpa);
            }
        }
    }
}
