package web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to doPost method of loginServlet!");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        //解决回传数据的中文乱码问题
        response.setContentType("text/html;charset=UTF-8");
        if("rekord".equals(username)&&"17573401869yicao".equals(password)){

//            response.getWriter().write("登录成功！");
            //请求转发
//            RequestDispatcher requestDispatcher=request.getRequestDispatcher("/pages/myblog.jsp");
//            requestDispatcher.forward(request,response);
//            //请求重定向
//            response.setStatus(302);
//            response.setHeader("Location","http://localhost:8088/sxrekord/pages/myblog.jsp");
            //请求重定向方法2
            request.getSession().setAttribute("login","success");
            response.sendRedirect("http://localhost:8088/sxrekord/pages/myblog.jsp");
        }else{
            System.out.println("账号或密码不正确");
            response.getWriter().write("账号或密码不正确！");
//            RequestDispatcher requestDispatcher=request.getRequestDispatcher("/index.jsp");
//            requestDispatcher.forward(request,response);
            response.setStatus(302);
            response.setHeader("Location","http://localhost:8088//sxrekordpages/login.jsp");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Welcome to doGet method of loginServlet!");
    }
}
