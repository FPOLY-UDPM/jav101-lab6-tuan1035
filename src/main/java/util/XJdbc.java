package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XJdbc {
    public static void main(String[] args) {
        // 1. Cấu hình chuỗi kết nối (Sửa databaseName, user, password theo máy của bạn)
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ThanhTuan;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String password = "123456"; // Điền mật khẩu sa của bạn vào đây

        System.out.println("Đang thử kết nối đến SQL Server...");

        // 2. Thử mở kết nối bằng khối lệnh try-with-resources (tự động đóng khi xong)
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            if (conn != null) {
                System.out.println("=========================================");
                System.out.println("✅ KẾT NỐI THÀNH CÔNG ĐẾN SQL SERVER!");
                System.out.println("Tên Database: " + conn.getCatalog());
                System.out.println("=========================================");
            }

        } catch (SQLException e) {
            System.err.println("=========================================");
            System.err.println("❌ KẾT NỐI THẤT BẠI!");
            System.err.println("Lý do: " + e.getMessage());
            System.err.println("=========================================");
            e.printStackTrace();
        }
    }
}