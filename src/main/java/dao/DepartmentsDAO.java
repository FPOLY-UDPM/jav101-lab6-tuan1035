package dao;

import entity.Departments;
import util.JDBC1;
import util.JdbcV2;
import util.jdbcV3;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentsDAO {

    // --- ĐÃ SỬA: Đổi tên bảng thành [Department] và các cột thành chữ thường [id], [name], [description] ---

    // JDBC V1
    private String stmSELECT = "SELECT [id], [name], [description] FROM [dbo].[Department]";

    // JDBC V2
    private String stmSELECT_byId = "SELECT [id], [name], [description] FROM [dbo].[Department]WHERE [id] = ?";
    private String stmSELECT_byName = " SELECT [id], [name], [description] FROM [dbo].[Department]WHERE [name] LIKE ?";

    // Câu lệnh SQL phục vụ CRUD
    private String stmINSERT = "INSERT INTO [dbo].[Department] ([id], [name], [description]) VALUES (?, ?, ?)";
    private String stmUPDATE = "UPDATE [dbo].[Department] SET [name] = ?, [description] = ? WHERE [id] = ?";
    private String stmDELETE = "DELETE FROM [dbo].[Department] WHERE [id] = ?";

    // JDBC V3 (Các lệnh Stored Procedure - giữ nguyên theo bài mẫu, chỉ kích hoạt khi bạn đã tạo Proc trong SQL Server)
    // JDBC V3
    private String callSELECT = "exec spSelectAll";
    private String callSELECT_byId = "exec spSelectById(?)";
    private String callSELECT_byName = "exec spSelectByName(?)";
    private String callINSERT = "exec spInsert(?,?,?)";
    private String callUPDATE = "exec spUpdate(?,?,?)";
    private String callDELETE_byId = "exec spDeleteById(?)";

    /**
     * Hàm test nhanh ra Console (Đã sửa tên cột thành chữ thường để không bị lỗi kết nối)
     */
    public void checkDepartmentDAO() {
        try {
            String sql = stmSELECT;
            ResultSet resultSet = JDBC1.executeQuery(sql);
            while (resultSet.next()) {
                // ĐÃ SỬA: Chuyển sang chữ thường "id", "name", "description"
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
     * Lấy toàn bộ danh sách phòng ban
     */
    public List<Departments> findAll() {
        List<Departments> list = new ArrayList<>();
        try {
            // Hiện tại đang ưu tiên dùng cách chạy ổn định nhất là JdbcV2
            ResultSet resultSet = JdbcV2.executeQuery(stmSELECT);
            while (resultSet.next()) {
                Departments dept = new Departments();

                // ĐÃ SỬA: Chuyển sang chữ thường cho khớp SQL
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
            System.out.println("❌ Lỗi ở hàm findAll()!");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm phòng ban theo mã ID chính xác
     */
    public Departments findById(String id) {
        Departments dept = null;
        try {
            ResultSet resultSet = JdbcV2.executeQuery(stmSELECT_byId, id);
            if (resultSet.next()) {
                dept = new Departments();
                // ĐÃ SỬA: Chuyển sang chữ thường cho khớp SQL
                dept.setId(resultSet.getString("id").trim());
                dept.setName(resultSet.getString("name"));
                dept.setDescription(resultSet.getString("description"));
            }
            if (resultSet != null) {
                resultSet.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm findById()!");
            e.printStackTrace();
        }
        return dept;
    }

    /**
     * Tìm kiếm danh sách phòng ban theo tên (LIKE)
     */
    public List<Departments> findByName(String name) {
        List<Departments> list = new ArrayList<>();
        try {
            ResultSet resultSet = JdbcV2.executeQuery(stmSELECT_byName, "%" + name + "%");
            while (resultSet.next()) {
                Departments dept = new Departments();
                // ĐÃ SỬA: Chuyển sang chữ thường cho khớp SQL
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
            System.out.println("❌ Lỗi ở hàm findByName()!");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 3. THÊM MỚI PHÒNG BAN
     */
    public int insert(Departments dept) {
        try {
            // Đang bật chạy mặc định bằng SQL thuần (JdbcV2) để an toàn và khớp bảng của bạn.
            // Nếu trên lớp thầy bắt dùng Procedure, bạn mở comment dòng JdbcV3 ở dưới ra nhé.
            return JdbcV2.executeUpdate(stmINSERT, dept.getId(), dept.getName(), dept.getDescription());
            // return JdbcV3.executeUpdate(callINSERT, dept.getId(), dept.getName(), dept.getDescription());
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm insert()!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 4. CẬP NHẬT PHÒNG BAN
     */
    public int update(Departments dept) {
        try {
            return JdbcV2.executeUpdate(stmUPDATE, dept.getName(), dept.getDescription(), dept.getId());
            // return JdbcV3.executeUpdate(callUPDATE, dept.getName(), dept.getDescription(), dept.getId());
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm update()!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 5. XÓA PHÒNG BAN
     */
    public int delete(String id) {
        try {
            return JdbcV2.executeUpdate(stmDELETE, id);
            // return JdbcV3.executeUpdate(callDELETE_byId, id);
        } catch (Exception e) {
            System.out.println("❌ Lỗi ở hàm delete()!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 6. Hàm getAll viết theo chuẩn đóng tài nguyên tự động (Try-with-resources)
     */
    public List<Departments> getAll() {
        List<Departments> list = new ArrayList<>();
        // Lưu ý: Chỉ dùng hàm này khi bạn đã tạo Stored Procedure tên là [spSelectAll] trong SQL Server
        try (java.sql.Connection conn = util.jdbcV3.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(callSELECT);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Departments dept = new Departments();
                // ĐÃ SỬA: Đồng bộ tên cột chữ thường
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