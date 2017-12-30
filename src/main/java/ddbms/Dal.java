package ddbms;

import ddbms.models.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class Dal {

    private static Dal connector = null;

    public static Dal get() {
        if (connector == null) {
            try {
                connector = new Dal();
            } catch (SQLException e) {
                System.err.println("Connection with database failed");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return connector;
    }

    private Connection conn = null;

    private Dal() throws SQLException {
        conn = PgCon.get();
    }

    public void initDb() throws SQLException {

        // create missing tables
        PreparedStatement st = conn.prepareStatement("drop table if exists popular_rank;" +
        "create table popular_rank (\"id\" varchar(64), \"timestamp\" char(14), temporalGranularity varchar(32), articleAidList text);" +
        "drop table if exists be_read;" +
        "create table be_read (\"id\" VARCHAR(64), \"timestamp\" char(14),\"aid\" char(5),\"readNum\" varchar(16),\"readUidList\" text," +
                "\"commentNum\" varchar(16),\"commentUidList\" text,\"agreeNum\" varchar(16),\"agreeUidList\" text," +
                "\"shareNum\" varchar(16),\"shareUidList\" text);");
        st.execute();
        st.close();

        // fill be-read table
        st = conn.prepareStatement("select * from user_read order by aid;");
        ResultSet rs = st.executeQuery();
        BeRead br = null;
        while (rs.next()) {
            String aid = rs.getString("aid");
            String uid = rs.getString("uid");
            if (br == null || !br.getAid().equals(aid)) {
                if (br != null)
                    createBeread(br);
                br = new BeRead(aid);
            }
            if (rs.getString("readOrNot").equals("1"))
                br.read(uid);
            if (rs.getString("agreeOrNot").equals("1"))
                br.agree(uid, false);
            if (rs.getString("commentOrNot").equals("1"))
                br.comment(uid, false);
            if (rs.getString("shareOrNot").equals("1"))
                br.share(uid, false);
        }
        if (br != null)
            createBeread(br);

        // fill popular-rank table
        Domain.fillPopularRankTable();
    }

    public ArrayList<Article> getArticleList(int pageNumber, int pageSize, String category, String language) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select \"timestamp\", aid, title, category, abstract, \"articleTags\", authors, \"language\" from article WHERE category = ? AND \"language\" = ? limit ? offset ?;");
        st.setInt(1, pageSize);
        st.setInt(2, pageSize * pageNumber);
        st.setString(3, category);
        st.setString(4, language);
        ResultSet rs = st.executeQuery();
        ArrayList<Article> articles = new ArrayList<>();
        while (rs.next()) {
            articles.add(new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        rs.close();
        st.close();
        return articles;
    }

    public ArrayList<Article> getArticleList(String[] articleIds) throws SQLException {
        StringBuilder sql = new StringBuilder("Select \"timestamp\", aid, title, category, abstract, \"articleTags\", authors, \"language\" from article WHERE aid IN (");
        for (int i = 0; i < articleIds.length; ++i) {
            if (i != 0)
                sql.append(",");
            sql.append("?");
        }
        sql.append(");");
        PreparedStatement st = conn.prepareStatement(sql.toString());
        for (int i = 0; i < articleIds.length; ++i) {
            st.setString(i + 1, articleIds[i]);
        }
        ResultSet rs = st.executeQuery();
        ArrayList<Article> articles = new ArrayList<>();
        while (rs.next()) {
            articles.add(new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        rs.close();
        st.close();
        return articles;
    }

    public Article getArticle(String id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select * from article where aid=?;");
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        Article article = null;
        if (rs.next()) {
            article = new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
        }
        rs.close();
        st.close();
        return article;
    }

    public void createArticle(Article article) throws SQLException {
        insertTuple("INSERT INTO article VALUES ", article, Article.class);
    }

    public void createBeread(BeRead beRead) throws SQLException {
        insertTuple("INSERT INTO be_read VALUES ", beRead, BeRead.class);
    }

    public BeRead getBeRead(String aid) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from be_read where aid = ?;");
        st.setString(1, aid);
        BeRead entry = null;
        ResultSet rs = st.executeQuery();
        if (rs.next())
            entry = new BeRead(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
        rs.close();
        st.close();
        return entry;
    }

    public ArrayList<String> getPopularArticles(Long minTimestamp, int nbArticles) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select aid, count(*) from user_read " +
                "where (user_read.\"timestamp\">? and (\"agreeOrNot\"='1' or \"commentOrNot\"='1' or \"shareOrNot\"='1')) " +
                "group by aid order by count(*) desc limit ?;");
        st.setString(1, minTimestamp.toString());
        st.setInt(2, nbArticles);
        ResultSet rs = st.executeQuery();
        ArrayList<String> articles = new ArrayList<>();
        while (rs.next()) {
            articles.add(rs.getString(1));
        }
        return articles;
    }

    public PopularRank getPopularRank(String temporalGranularity) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from popular_rank where temporalGranularity=?;");
        st.setString(1, temporalGranularity);
        ResultSet rs = st.executeQuery();
        PopularRank pr = null;
        if (rs.next()) {
            pr = new PopularRank(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        }
        rs.close();
        st.close();
        return pr;
    }

    public void clearPopularRank() throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM popular_rank WHERE TRUE;");
        st.execute();
        st.close();
    }

    public void createPopularRank(PopularRank entry) throws SQLException {
        insertTuple("INSERT INTO popular_rank VALUES ", entry, PopularRank.class);
    }

    public ArrayList<Read> getReadList(String uid) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from user_read where uid = ?;");
        st.setString(1, uid);
        ResultSet rs = st.executeQuery();
        ArrayList<Read> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Read(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)));
        }
        rs.close();
        st.close();
        return list;
    }

    public Read getRead(String uid, String aid) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from user_read where uid = ? and aid = ?;");
        st.setString(1, uid);
        st.setString(2, aid);
        ResultSet rs = st.executeQuery();
        Read read = null;
        if (rs.next()) {
            read = new Read(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10));
        }
        rs.close();
        st.close();
        return read;
    }

    public void createRead(Read entry) throws SQLException {
        insertTuple("INSERT INTO user_read VALUES ", entry, Read.class);
    }

    public User getUser(String uid) throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from \"user\" where uid = ?;");
        st.setString(1, uid);
        ResultSet rs = st.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
        }
        rs.close();
        st.close();
        return user;
    }

    public void createUser(User user) throws SQLException {
        insertTuple("INSERT INTO \"user\" VALUES ", user, User.class);
    }

    public void updateBeread(BeRead beRead) throws SQLException {
        PreparedStatement st = conn.prepareStatement("update be_read set " +
        "\"timestamp\" = ?, \"readNum\" = ?, \"readUidList\" = ?, \"commentNum\" = ?, \"commentUidList\" = ?," +
        "\"agreeNum\" = ?, \"agreeUidList\" = ?, \"shareNum\" = ?, \"shareUidList\" = ? " +
        "where aid = ?;");
        st.setString(1, beRead.getTimestamp().toString());
        st.setString(2, Integer.toString(beRead.getReadNum()));
        st.setString(3, beRead.getReadUidsString());
        st.setString(4, Integer.toString(beRead.getCommentNum()));
        st.setString(5, beRead.getCommentUidsString());
        st.setString(6, Integer.toString(beRead.getAgreeNum()));
        st.setString(7, beRead.getAgreeUidsString());
        st.setString(8, Integer.toString(beRead.getShareNum()));
        st.setString(9, beRead.getShareUidsString());
        st.setString(10, beRead.getAid());
        st.execute();
    }

    public void updateRead(Read read) throws SQLException {
        PreparedStatement st = conn.prepareStatement("update user_read set " +
        "\"agreeOrNot\" = ?, \"commentOrNot\" = ?, \"shareOrNot\" = ?, \"commentDetail\" = ? " +
        "where \"uid\" = ? and \"aid\" = ?;");
        st.setString(1, read.isAgreeOrNot() ? "1" : "0");
        st.setString(2, read.isShareOrNot() ? "1" : "0");
        st.setString(3, read.isCommentOrNot() ? "1" : "0");
        st.setString(4, read.getCommentDetail());
        st.setString(5, read.getUid());
        st.setString(6, read.getAid());
        st.execute();
    }

    private void insertTuple(String sqlQueryBeginning, Object entry, Class<?> model) throws SQLException {
        StringBuilder sqlTuple = new StringBuilder("");
        boolean first = true;
        for (Field field : model.getDeclaredFields()) {
            if (first) {
                first = false;
            } else {
                sqlTuple.append(",");
            }
            sqlTuple.append("?");
        }
        String sql = sqlQueryBeginning + "(" + sqlTuple + ");";
        PreparedStatement st = conn.prepareStatement(sql);
        int i = 1;
        for (Field field : model.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                st.setString(i, field.getType() == Boolean.class ? ((Boolean)field.get(entry) ? "1" : "0") :
                        field.get(entry).toString());
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
            i++;
        }
        st.execute();
        st.close();
    }
}
