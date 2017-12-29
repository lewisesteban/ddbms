package ddbms.models;

import java.util.UUID;

public class PopularRank {
    private String id;
    private Long timestamp;
    private String temporalGranularity;
    private String aidList = "";

    /**
     * Constructor for getting entries
     */
    public PopularRank(String id, String timestamp, String temporalGranularity, String aidList) {
        this.id = id;
        this.timestamp = Long.parseLong(timestamp);
        this.temporalGranularity = temporalGranularity;
        this.aidList = aidList;
    }

    /**
     * Constructor for creating entries
     */
    public PopularRank(String temporalGranularity, Iterable<String> aidList) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.temporalGranularity = temporalGranularity;
        for (String aid : aidList) {
            this.aidList += aid + ";";
        }
    }

    public String getId() {
        return id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getTemporalGranularity() {
        return temporalGranularity;
    }

    public String[] getArticleIds() {
        return aidList.split(";");
    }
}
