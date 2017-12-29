package ddbms.models;

import java.util.UUID;

public class BeRead {
    private String id;
    private Long timestamp;
    private String aid;
    private Integer readNum = 0;
    private String readUidList = "";
    private Integer commentNum = 0;
    private String commentUidList = "";
    private Integer agreeNum = 0;
    private String agreeUidList = "";
    private Integer shareNum = 0;
    private String shareUidList = "";

    /**
     * Constructor for getting an entry
     */
    public BeRead(String id, String timestamp, String aid, String readNum, String readUidList, String commentNum, String commentUidList, String agreeNum, String agreeUidList, String shareNum, String shareUidList) {
        this.id = id;
        this.timestamp = Long.parseLong(timestamp);
        this.aid = aid;
        this.readNum = Integer.parseInt(readNum);
        this.readUidList = readUidList;
        this.commentNum = Integer.parseInt(commentNum);
        this.commentUidList = commentUidList;
        this.agreeNum = Integer.parseInt(agreeNum);
        this.agreeUidList = agreeUidList;
        this.shareNum = Integer.parseInt(shareNum);
        this.shareUidList = shareUidList;
    }

    /**
     * Constructor for creating an entry
     */
    public BeRead(String aid) {
        this.aid = aid;
        this.timestamp = System.currentTimeMillis();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getAid() {
        return aid;
    }

    public int getReadNum() {
        return readNum;
    }

    public String[] getReadUids() {
        return readUidList.split(";");
    }

    public int getCommentNum() {
        return commentNum;
    }

    public String[] getCommentUids() {
        return commentUidList.split(";");
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public String[] getAgreeUids() {
        return agreeUidList.split(";");
    }

    public int getShareNum() {
        return shareNum;
    }

    public String getShareUidList() {
        return shareUidList;
    }
    
    public void comment(String uid, boolean undo) {
        if (undo) {
            commentUidList = removeFromList(commentUidList, uid);
            commentNum--;
        } else {
            commentUidList = addToList(commentUidList, uid);
            commentNum++;
        }
    }

    public void agree(String uid, boolean undo) {
        if (undo) {
            agreeUidList = removeFromList(agreeUidList, uid);
            agreeNum--;
        } else {
            agreeUidList = addToList(agreeUidList, uid);
            agreeNum++;
        }
    }

    public void share(String uid, boolean undo) {
        if (undo) {
            shareUidList = removeFromList(shareUidList, uid);
            shareNum--;
        } else {
            shareUidList = addToList(shareUidList, uid);
            shareNum++;
        }
    }
    
    private String addToList(String list, String uid) {
        return list + uid + ";";
    }
    
    private String removeFromList(String list, String uid) {
        return list.replace(uid + ";", "");
    }
}
