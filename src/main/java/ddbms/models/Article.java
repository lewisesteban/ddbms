package ddbms.models;

import java.util.Date;

public class Article {

    private Date timestamp;
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

    public Article(Date timestamp, String aid, String title, String category, String abst, String articleTags, String authors, String language, String text, String image, String video) {
        this(timestamp, aid, title, category, abst, articleTags, authors, language);
        this.text = text;
        this.image = image;
        this.video = video;
    }

    public Article(Date timestamp, String aid, String title, String category, String abst, String articleTags, String authors, String language) {
        this.timestamp = timestamp;
        this.aid = aid;
        this.title = title;
        this.category = category;
        this.abst = abst;
        this.articleTags = articleTags;
        this.authors = authors;
        this.language = language;
    }

    public Date getTimestamp() {
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
