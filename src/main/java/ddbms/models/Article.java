package ddbms.models;

public class Article {

    private Long timestamp;
    private String aid;
    private String title;
    private String category;
    private String abst;
    private String articleTags;
    private String authors;
    private String language;
    private String text = null;
    private String image = null;
    private String video = null;

    /**
     * Constructor for getting an article list
     */
    public Article(String timestamp, String aid, String title, String category, String abst, String articleTags, String authors, String language, String image) {
        this.timestamp = Long.parseLong(timestamp);
        this.aid = aid;
        this.title = title;
        this.category = category;
        this.abst = abst;
        this.articleTags = articleTags;
        this.authors = authors;
        this.language = language;
        this.image = image;
    }

    /**
     * Constructor for getting a single article
     */
    public Article(String timestamp, String aid, String title, String category, String abst, String articleTags, String authors, String language, String text, String image, String video) {
        this(timestamp, aid, title, category, abst, articleTags, authors, language, image);
        this.text = text;
        this.video = video;
    }

    /**
     * Constructor for creating an article
     */
    public Article(String aid, Long timestamp, String title, String category, String abst, String articleTags, String authors, String language, String text, String image, String video) {
        this.aid = aid;
        this.timestamp = timestamp;
        this.title = title;
        this.category = category;
        this.abst = abst;
        this.articleTags = articleTags;
        this.authors = authors;
        this.language = language;
        this.text = text;
        this.image = image;
        this.video = video;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getAid() {
        return aid;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAbst() {
        return abst;
    }

    public String getArticleTags() {
        return articleTags;
    }

    public String getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }
}
