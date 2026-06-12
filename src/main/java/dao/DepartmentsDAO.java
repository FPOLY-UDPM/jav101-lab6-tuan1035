package dao;

import entity.Departments;
import util.JDBC1;
import util.JdbcV2;
import util.jdbcV3; // Đã sửa chữ J viết hoa cho đồng bộ với cấu hình hệ thống

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentsDAO {

    // JDBC V1
    private String stmSELECT = "SELECT [id], [name], [description] FROM [dbo].[Department]";

    // JDBC V2 - ĐÃ SỬA: Cách khoảng trắng chữ WHERE để tránh lỗi dính chữ SQL
    private String stmSELECT_byId = "SELECT [id], [name], [description] FROM [dbo].[Department] WHERE [id] = ?";
    private String stmSELECT_byName = "SELECT [id], [name], [description] FROM [dbo].[Department] WHERE [name] LIKE ?";

    // Câu lệnh SQL phục vụ CRUD
    private String stmINSERT = "INSERT INTO [dbo].[Department] ([id], [name], [description]) VALUES (?, ?, ?)";
    private String stmUPDATE = "UPDATE [dbo].[Department] SET [name] = ?, [description] = ? WHERE [id] = ?";
    private String stmDELETE = "DELETE FROM [dbo].[Department] WHERE [id] = ?";

    // JDBC V3 (Các lệnh Stored Procedure)
    private String callSELECT = "exec spSelectAll";
    private String callSELECT_byId = "exec spSelectById(?)";
    private String callSELECT_byName = "exec spSelectByName(?)";
    private String callINSERT = "exec spInsert(?,?,?)";
    private String callUPDATE = "exec spUpdate(?,?,?)";
    private String callDELETE_byId = "exec spDeleteById(?)";

    /**
     * Hàm test nhanh ra Console
     */
    public void checkDepartmentDAO() {
        try {
            String sql = stmSELECT;
            ResultSet resultSet = JDBC1.executeQuery(sql);
            while (resultSet.next()) {
                String maPhong = resultSet.getString("id");
                String tenPhong = resultSet.getString("name");
                String motaPhong = resultSet.getString("description");

                System.out.println(maPhong);
                System.out.println(tenPhong);
                System.out.println(motaPhong);
            }
            if (resultSet != null) {
                resultSet.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy toàn bộ danh sách phòng ban bằng Stored Procedure (JDBC V3)
     */
    public List<Departments> findAll() {
        List<Departments> list = new ArrayList<>();
        try {
            // ĐÃ CHUYỂN: Chuyển sang dùng câu lệnh Proc và lớp tiện ích JdbcV3
            ResultSet resultSet = jdbcV3.executeQuery(callSELECT);
            while (resultSet.next()) {
                Departments dept = new Departments();

                if (resultSet.getString("id") != null) {
                    dept.setId(resultSet.getString("id").trim());
                }
                dept.setName(resultSet.getString("name"));
                dept.setDescription(resultSet.getString("description"));

                list.add(dept);
            }
            if (resultSet != null) {
                resultSet.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm findAll() bằng Stored Procedure!");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm phòng ban theo mã ID chính xác bằng Stored Procedure (JDBC V3)
     */
    public Departments findById(String id) {
        Departments dept = null;
        try {
            // ĐÃ CHUYỂN: Dùng callSELECT_byId và JdbcV3
            ResultSet resultSet = jdbcV3.executeQuery(callSELECT_byId, id);
            if (resultSet.next()) {
                dept = new Departments();
                dept.setId(resultSet.getString("id").trim());
                dept.setName(resultSet.getString("name"));
                dept.setDescription(resultSet.getString("description"));
            }
            if (resultSet != null) {
                resultSet.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm findById() bằng Stored Procedure!");
            e.printStackTrace();
        }
        return dept;
    }

    /**
     * Tìm kiếm danh sách phòng ban theo tên bằng Stored Procedure (JDBC V3)
     */
    public List<Departments> findByName(String name) {
        List<Departments> list = new ArrayList<>();
        try {
            String keyword = "%" + name + "%";
            // ĐÃ CHUYỂN: Dùng callSELECT_byName và JdbcV3
            ResultSet resultSet = jdbcV3.executeQuery(callSELECT_byName, keyword);
            while (resultSet.next()) {
                Departments dept = new Departments();
                if (resultSet.getString("id") != null) {
                    dept.setId(resultSet.getString("id").trim());
                }
                dept.setName(resultSet.getString("name"));
                dept.setDescription(resultSet.getString("description"));
                list.add(dept);
            }
            if (resultSet != null) {
                resultSet.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm findByName() bằng Stored Procedure!");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 3. THÊM MỚI PHÒNG BAN bằng Stored Procedure (JDBC V3)
     */
    public int insert(Departments dept) {
        try {
            // ĐÃ KÍCH HOẠT: Chuyển hẳn sang dùng Stored Procedure theo yêu cầu
            return jdbcV3.executeUpdate(callINSERT, dept.getId(), dept.getName(), dept.getDescription());
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm insert() bằng Stored Procedure!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 4. CẬP NHẬT PHÒNG BAN bằng Stored Procedure (JDBC V3)
     */
    public int update(Departments dept) {
        try {
            // ĐÃ KÍCH HOẠT: Chuyển hẳn sang dùng Stored Procedure theo yêu cầu
            return jdbcV3.executeUpdate(callUPDATE, dept.getName(), dept.getDescription(), dept.getId());
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm update() bằng Stored Procedure!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 5. XÓA PHÒNG BAN bằng Stored Procedure (JDBC V3)
     */
    public int delete(String id) {
        try {
            // ĐÃ KÍCH HOẠT: Chuyển hẳn sang dùng Stored Procedure theo yêu cầu
            return jdbcV3.executeUpdate(callDELETE_byId, id);
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm delete() bằng Stored Procedure!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 6. Hàm getAll viết theo chuẩn đóng tài nguyên tự động (Try-with-resources)
     */
    public List<Departments> getAll() {
        List<Departments> list = new ArrayList<>();
        // ĐÃ SỬA: Sửa lại tên class thành JdbcV3 (viết hoa chữ J) cho đúng chuẩn đặt tên Class của Java
        try (java.sql.Connection conn = util.jdbcV3.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(callSELECT);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Departments dept = new Departments();
                if (resultSet.getString("id") != null) {
                    dept.setId(resultSet.getString("id").trim());
                }
                dept.setName(resultSet.getString("name"));
                dept.setDescription(resultSet.getString("description"));
                list.add(dept);
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm getAll() bằng Stored Procedure!");
            e.printStackTrace();
        }
        return list;
    }
}