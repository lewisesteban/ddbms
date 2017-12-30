package ddbms;

import ddbms.models.Article;
import ddbms.models.BeRead;
import ddbms.models.PopularRank;
import ddbms.models.Read;

import java.sql.SQLException;
import java.util.ArrayList;

enum temporalGranularity {
    daily,
    weekly,
    monthly
}

public class Domain {

    /**
     * Returns a list of articles without their content
     */
    public static ArrayList<Article> viewArticles(int pageNumber, int pageSize, String category, String language) throws SQLException {
        return Dal.get().getArticleList(pageNumber, pageSize, category, language);
    }

    /**
     * Creates a new article
     */
    public static void createArticle(Article article) throws SQLException {
        Dal.get().createArticle(article);
    }

    /**
     * Returns the user-related statistics of an article
     */
    public static BeRead getArticleStats(String aid) throws SQLException {
        return Dal.get().getBeRead(aid);
    }

    /**
     * Provides an article with its content
     */
    public static Article readArticle(String uid, String aid) throws SQLException {
        BeRead br = Dal.get().getBeRead(aid);
        boolean newBr = false;
        if (br == null) {
            br = new BeRead(aid);
            newBr = true;
        }

        boolean updateBr = false;
        Read read = Dal.get().getRead(uid, aid);
        if (read == null) {
            read = new Read(uid, aid);
            Dal.get().createRead(read);
            br.read(uid);
            updateBr = true;
        }

        if (newBr)
            Dal.get().createBeread(br);
        else if (updateBr)
            Dal.get().updateBeread(br);

        return Dal.get().getArticle(aid);
    }

    /**
     * Updates the popular tables
     */
    public static void fillPopularRankTable() throws SQLException {
        Dal.get().clearPopularRank();
        Long currentTime = 1506000000008L; // all entries in the DB are too old!
        Dal.get().createPopularRank(new PopularRank("daily",
                Dal.get().getPopularArticles(currentTime - 1000L * 60L * 60L * 24L, 5)));
        Dal.get().createPopularRank(new PopularRank("weekly",
                Dal.get().getPopularArticles(currentTime - 7L * 1000L * 60L * 60L * 24L, 5)));
        Dal.get().createPopularRank(new PopularRank("monthly",
                Dal.get().getPopularArticles(currentTime - 30L * 1000L * 60L * 60L * 24L, 5)));
    }

    /**
     * Returns the most popular articles without their content
     */
    public static ArrayList<Article> getPopularArticles(temporalGranularity t) throws SQLException {
        String tStr;
        if (t == temporalGranularity.daily)
            tStr = "daily";
        else if (t == temporalGranularity.weekly)
            tStr = "weekly";
        else
            tStr = "monthly";
        PopularRank pr = Dal.get().getPopularRank(tStr);
        return Dal.get().getArticleList(pr.getArticleIds());
    }



    // S3

    // init: separate users table
    // program arg: beijing/hongkong

    // view comments
    // add comment
    // view user
    // view users from other area (beijing/hongkong)
}
