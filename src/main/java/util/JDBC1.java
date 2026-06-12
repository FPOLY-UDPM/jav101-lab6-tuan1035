package util;

import java.sql.*;

public class JDBC1 {
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // Đã thêm từ khóa 'static' ở đây
    static String url = "jdbc:sqlserver://localhost:1433;databaseName=ThanhTuan;encrypt=true;trustServerCertificate=true;";
    static String username = "sa";
    static String password = "123456";

    static {
        try { // nạp driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Mở kết nối */
    public static Connection getConnection() throws SQLException {
        // Đã sửa: Truyền đầy đủ 3 tham số (url, username, password)
        return DriverManager.getConnection(url, username, password);
    }

    /** Thao tác dữ liệu (INSERT, UPDATE, DELETE) */
    public static int executeUpdate(String sql) throws SQLException {
        // Sử dụng cấu trúc try-with-resources để tự động đóng Connection và Statement
        // tránh bị tràn/nghẽn kết nối (Connection Leak) về sau
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    /** Truy vấn dữ liệu (SELECT) */
    public static ResultSet executeQuery(String sql) throws SQLException {
        // Lưu ý: Đối với ResultSet, chúng ta tạm thời giữ Connection mở để bên ngoài (DAO) đọc dữ liệu.
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }
}