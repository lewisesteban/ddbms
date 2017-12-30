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
        PreparedStatement st = conn.prepareStatement("drop table if exists popular_rank;" +
        "create table popular_rank (\"id\" varchar(64), \"timestamp\" char(14), temporalGranularity varchar(32), articleAidList text);" +
        "drop table if exists be_read;" +
        "create table be_read (\"id\" VARCHAR(64), \"timestamp\" char(14),\"aid\" char(5),\"readNum\" varchar(16),\"readUidList\" text," +
                "\"commentNum\" varchar(16),\"commentUidList\" text,\"agreeNum\" varchar(16),\"agreeUidList\" text," +
                "\"shareNum\" varchar(16),\"shareUidList\" text);");
        st.execute();
        st.close();
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

    public ArrayList<PopularRank> getPopularRank() throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from popular_rank;");
        ResultSet rs = st.executeQuery();
        ArrayList<PopularRank> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new PopularRank(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        rs.close();
        st.close();
        return list;
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