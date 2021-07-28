package web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SafeFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//        req.getRequestDispatcher("/index.html").forward(req,resp);
        HttpServletRequest request=(HttpServletRequest)req;
//        String isLogin=request.getSession().getAttribute("login").toString();

        if(request.getSession().getAttribute("login")!=null){
            chain.doFilter(req, resp);
            System.out.println("登录成功");
        }else{
            //拦截直接访问用户
            HttpServletResponse response=(HttpServletResponse)resp;
            response.sendRedirect("http://localhost:8088/sxrekord/index.html");
        }
    }


    public void init(FilterConfig config) throws ServletException {

    }

}
