package ddbms;

import ddbms.models.Article;
import ddbms.models.BeRead;
import ddbms.models.Read;

import java.sql.SQLException;
import java.util.ArrayList;

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

    // top list
    // S3

    // init: separate users table
    // program arg: beijing/hongkong

    // view comments
    // add comment
    // view user
    // view users from other area (beijing/hongkong)
}
