package util;

import java.sql.*;

public class JdbcV2 {
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dburl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=ThanhTuan;encrypt=false";
    static String username = "sa";
    static String password = "123456";

    static {
        try { // Nạp driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Mở kết nối */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dburl, username, password);
    }

    /** Thao tác dữ liệu (Insert, Update, Delete) */
    public static int executeUpdate(String sql, Object... values) throws SQLException {
        // String sql : là câu lệnh sql để thực hiện thao tác với dữ liệu
        // Object... values : là CÁC THAM SỐ nếu có kèm theo trong câu lệnh sql

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        // Đây là vòng lặp để gán các giá trị value vào tham số tương ứng của câu lệnh sql
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        return statement.executeUpdate();
    }

    /** Truy vấn dữ liệu (Select) */
    public static ResultSet executeQuery(String sql, Object... values) throws SQLException {
        // String sql : là câu lệnh sql để thực hiện thao tác với dữ liệu
        // Object... values : là CÁC THAM SỐ nếu có kèm theo trong câu lệnh sql

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        // Đây là vòng lặp để gán các giá trị value vào tham số tương ứng của câu lệnh sql
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        return statement.executeQuery();
    }
}