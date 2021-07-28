<%@ page import="java.io.File" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Rekord
  Date: 2021/7/24
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>file upload and download</title>
    <style type="text/css">
        form>input{
            display: block;
            margin-left: 47%;
        }
    </style>
</head>
<body>
    <h1 align="center">Welcome to File Storage Base</h1>
    <hr width="100%"/>
    <form action="/sxrekord/fileUploadServlet" method="post"
      enctype="multipart/form-data">
    <input type="file" name="file"><br/>
    <input type="submit" value="上传文件">
    </form>
    <hr />
<%--
1.获取文件列表
2.令每一个文件对应一个a标签，a标签的值为文件名
3.a标签的href属性也应当加上请求下载的文件名
4.FileDownload程序负责处理发来的下载请求，主要依据来自于请求参数
--%>
<%
    String path="D:\\workspace\\JavaWeb\\sxrekord\\web\\file";
    File file=new File(path);
    List<String> list_filename=new ArrayList<>();
    for(File f:file.listFiles()){
//        System.out.println(f.getName());
        list_filename.add(f.getName());
    }
    pageContext.setAttribute("filename_array",list_filename);
%>
    <table border="1" align="center">
        <c:forEach items="${filename_array}" var="filename">
            <tr>
                <td><a href="/sxrekord/fileServlet?filename=${filename}">${filename}</a> </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
