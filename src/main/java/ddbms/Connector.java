package ddbms;

import ddbms.models.Article;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Connector {

    private static Connector connector = null;

    public static Connector get() {
        if (connector == null) {
            try {
                connector = new Connector();
            } catch (SQLException e) {
                System.err.println("Connection with database failed");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return connector;
    }

    private Connection conn = null;

    private Connector() throws SQLException {
        conn = PgCon.get();
        //conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank?sslmode=disable", "postgres", "root");
    }

    public ArrayList<Article> getArticleList(int pageNumber, int pageSize) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select \"timestamp\", aid, title, category, abstract, \"articleTags\", authors, \"language\" from article limit ? offset ?;");
        st.setInt(1, pageSize);
        st.setInt(2, pageSize * pageNumber);
        ResultSet rs = st.executeQuery();
        ArrayList<Article> articles = new ArrayList<>();
        while (rs.next()) {
            articles.add(new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        return articles;
    }

    public Article getArticle(String id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select * from article where aid=?;");
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
        }
        return null;
    }

    public void createArticle(Article article) throws SQLException {
        PreparedStatement st = sqlInsertTuple("INSERT INTO article VALUES ", article, Article.class);
        st.execute();
    }

    private PreparedStatement sqlInsertTuple(String sqlQueryBeginning, Object entry, Class<?> model) throws SQLException {
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
                st.setString(i, field.get(entry).toString());
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
            i++;
        }
        return st;
    }
}
