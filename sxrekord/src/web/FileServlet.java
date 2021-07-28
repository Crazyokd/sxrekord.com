package web;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;

public class FileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("请求下载文件==>"+request.getParameter("filename"));

        //1.获取要下载的文件名
        String downloadFileName=request.getParameter("filename");
        //2.读取要下载的文件内容（通过ServletContext对象可以读取）
        ServletContext servletContext=getServletContext();

        //获取要下载的文件类型
        String mimeType=servletContext.getMimeType("/file/"+downloadFileName);

        //再回传前，通过响应头告诉客户端返回的数据类型
        response.setContentType(mimeType);


        //设置响应头并避免乱码问题和保证兼容性
        setCharset(request,response,downloadFileName);

        InputStream inputStream=servletContext.getResourceAsStream("/file/"+downloadFileName);
//        InputStream inputStream=new FileInputStream(new File("D:\\workspace\\JavaWeb\\storage_space\\a.jpg"));
        //获取响应的输出流
        OutputStream outputStream=response.getOutputStream();

        //读取输入流中全部的数据，复制给输出流，输出给客户端
        copyFile(inputStream,outputStream);
    }

    //设置好字符集
    public void setCharset(HttpServletRequest request,HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        String ua=request.getHeader("User-Agent");
        String str;
        if(ua.contains("Firefox")){
            str="attachment;fileName="+"=?utf-8?B?"+
                    Base64.getEncoder().encode(fileName.getBytes("utf-8"))+"?=";
        }else{
            str="attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8");
            //把编码后的中文字符串设置到响应头中
        }
        response.setHeader("Content-Disposition",str);
    }

    public void copyFile(InputStream inputStream,OutputStream outputStream) throws IOException {
            byte[] bytes=new byte[1024];
            int len;
            while((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
    }
}
