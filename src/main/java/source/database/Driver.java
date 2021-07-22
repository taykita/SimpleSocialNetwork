package source.database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Arrays;

public class Driver extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectPostgreSQLDriver();
        PrintWriter printWriter = resp.getWriter();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/booknetwork_db",
                        "postgres", "");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email FROM accounts");

            while (rs.next()) {
                printWriter.println(rs.getString("email"));
            }
            stmt.close();
        } catch (SQLException e) {
            printWriter.println(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    private void connectPostgreSQLDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
