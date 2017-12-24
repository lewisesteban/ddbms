package ddbms;

import java.sql.*;
import java.util.ArrayList;

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
        conn = DriverManager.getConnection("jdbc:postgresql://133.130.103.216:37368/bank?sslmode=disable", "maxroach", "");
        //conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank?sslmode=disable", "postgres", "root");
    }

    public ArrayList<Article> getArticleList(int pageNumber, int pageSize) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select id, title from articles limit ? offset ?;");
        st.setInt(1, pageSize);
        st.setInt(2, pageSize * pageNumber);
        ResultSet rs = st.executeQuery();
        ArrayList<Article> articles = new ArrayList<>();
        while (rs.next()) {
            articles.add(new Article(rs.getString(1), rs.getString(2)));
        }
        return articles;
    }

    public Article getArticle(String id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("Select id, title, content from articles where id=?;");
        st.setString(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return new Article(rs.getString(1), rs.getString(2), rs.getString(3));
        }
        return null;
    }
}
