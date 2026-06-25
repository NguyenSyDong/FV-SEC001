public class CampaignStats {

    long impressions;
    long clicks;
    double spend;
    long conversions;

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public double getSpend() {
        return spend;
    }

    public void setSpend(double spend) {
        this.spend = spend;
    }

    public long getConversions() {
        return conversions;
    }

    public void setConversions(long conversions) {
        this.conversions = conversions;
    }

    public void add(long impressions, long clicks, double spend, long conversions) {
        this.impressions += impressions;
        this.clicks += clicks;
        this.spend += spend;
        this.conversions += conversions;
    }

    public double getCtr() {
        return impressions == 0 ? 0 : (double) clicks / impressions;
    }

    public Double getCpa() {
        return conversions == 0 ? null : spend / conversions;
    }


}