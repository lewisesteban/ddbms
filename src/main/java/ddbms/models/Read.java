package ddbms.models;

public class Read {
    private Long timestamp;
    private String uid;
    private String aid;
    private Integer readTimeLength = -1;
    private String readSequence = "";
    private Boolean readOrNot = false;
    private Boolean agreeOrNot = false;
    private Boolean commentOrNot = false;
    private String commentDetail = null;
    private Boolean shareOrNot = false;

    /**
     * Constructor for getting an entry
     */
    public Read(String timestamp, String uid, String aid, String readTimeLength, String readSequence, String readOrNot, String agreeOrNot, String commentOrNot, String commentDetail, String shareOrNot) {
        this.timestamp = Long.parseLong(timestamp);
        this.uid = uid;
        this.aid = aid;
        this.readTimeLength = Integer.parseInt(readTimeLength);
        this.readSequence = readSequence;
        this.readOrNot = "1".equals(readOrNot);
        this.agreeOrNot = "1".equals(agreeOrNot);
        this.commentOrNot = "1".equals(commentOrNot);
        this.commentDetail = commentDetail;
        this.shareOrNot = "1".equals(shareOrNot);
    }

    /**
     * Constructor for creating a new entry when a user reads an article
     */
    public Read(String uid, String aid) {
        this.uid = uid;
        this.aid = aid;
        this.timestamp = System.currentTimeMillis();
        this.readOrNot = true;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getAid() {
        return aid;
    }

    public int getReadTimeLength() {
        return readTimeLength;
    }

    public String getReadSequence() {
        return readSequence;
    }

    public boolean isReadOrNot() {
        return readOrNot;
    }

    public boolean isAgreeOrNot() {
        return agreeOrNot;
    }

    public boolean isCommentOrNot() {
        return commentOrNot;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public boolean isShareOrNot() {
        return shareOrNot;
    }

    public void setAgreed(boolean agreed) {
        this.agreeOrNot = agreed;
    }

    public void setShared(boolean shared) {
        this.shareOrNot = shared;
    }

    public void setComment(String comment) {
        if (comment == null || comment.isEmpty()) {
            this.commentDetail = null;
            this.commentOrNot = false;
        } else {
            this.commentDetail = comment;
            this.commentOrNot = true;
        }
    }
}
