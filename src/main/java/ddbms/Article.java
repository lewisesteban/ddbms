package ddbms;

public class Article {

    private String id;
    private String title;
    private String content = null;

    public Article(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Article(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
