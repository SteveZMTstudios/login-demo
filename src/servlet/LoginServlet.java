package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String account = req.getParameter("name");
        String pwd = req.getParameter("password");

        if (account == null || pwd == null) {
            resp.getWriter().write("{\"code\":\"400\",\"msg\":\"请提供用户名和密码!\"}");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/login_demo?serverTimezone=GMT%2B8";
        String username = "zcr";
        String password = "927975";

        try {
            // 显式加载 MySQL 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE account = ? AND password = ?");

            pstmt.setString(1, account);
            pstmt.setString(2, pwd);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                resp.getWriter().write("{\"code\":\"200\",\"msg\":\"登录成功!\"}");
            } else {
                resp.getWriter().write("{\"code\":\"500\",\"msg\":\"用户名或密码错误!\"}");
            }

            res.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            resp.getWriter().write("{\"code\":\"501\",\"msg\":\"数据库驱动未找到!\"}");
            e.printStackTrace();
        } catch (SQLException e) {
            resp.getWriter().write("{\"code\":\"501\",\"msg\":\"数据库查询异常!\"}");
            e.printStackTrace();
        }
    }
}