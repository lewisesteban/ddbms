package ddbms;

import ddbms.models.Article;
import ddbms.models.BeRead;

import java.sql.SQLException;
import java.util.ArrayList;

public class Domain {

    public static ArrayList<Article> viewArticles(int pageNumber, int pageSize, String category, String language) throws SQLException {
        return Dal.get().getArticleList(pageNumber, pageSize, category, language);
    }

    public static void createArticle(Article article) throws SQLException {
        Dal.get().createArticle(article);
    }

    public static BeRead getArticleStats(String aid) throws SQLException {
        return Dal.get().getBeRead(aid);
    }

    public static Article readArticle(String uid, String aid) throws SQLException {
        return null;
    }
}
