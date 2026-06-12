package servlet; // Tùy thuộc vào package thực tế của bạn

import dao.DepartmentsDAO;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Import Class DAO và Entity của bạn vào đây
// ví dụ: import dao.DepartmentDAO; hoặc import entity.Department;

@WebServlet("/test")
public class test extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. GỌI DAO ĐỂ LẤY DỮ LIỆU TỪ SQL SERVER (Hãy đổi tên Class DAO chuẩn theo bài của bạn)
        // Ví dụ bài của bạn có thể tên là DepartmentDAO hoặc DepartmentsDAO
        DepartmentsDAO dao = new DepartmentsDAO();
        List list = dao.findAll(); // Hoặc hàm getAll(), findAll() tùy bạn viết trong DAO

        // --- ĐOẠN LOG KIỂM TRA CHÍ MẠNG ---
        // Thêm dòng này để nhìn xem Java có lôi được dữ liệu từ SQL lên không
        System.out.println("====== DEBUG: KẾT QUẢ TỪ JAVA ======");
        System.out.println("Số lượng phòng ban lấy được: " + (list != null ? list.size() : "null"));
        System.out.println("====================================");

        // 2. ĐẨY DANH SÁCH SANG JSP (Chữ "departments" phải viết giống hệt trong file phongban.jsp)
        request.setAttribute("departments", list);

        // 3. ĐIỀU HƯỚNG SANG FILE JSP MỚI ĐỔI TÊN
        request.getRequestDispatcher("/views/phongban.jsp").forward(request, response);
    }
}