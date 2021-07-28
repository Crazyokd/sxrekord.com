<%--
  Created by IntelliJ IDEA.
  User: Rekord
  Date: 2021/7/24
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login My Blog</title>
    <style type="text/css">
        .main{
            /*background-color: black;*/
            font-size: 24px;
            width:500px;
            margin: 100px auto;

        }
        .main>form{
            background-color: blueviolet;
            width: 300px;
            text-align: left;
            /*background-color: aliceblue;*/
            /*width:500px;*/
            margin: 0 auto;
        }
        button{
            font-size: 24px;
            width: 100px;
            margin: 0 auto;
            display: block;
            text-align:center;
        }
        .main>form>input{
            height: 30px;
        }
    </style>
</head>
<body bgcolor="#00bfff">
<h1 align="center">Welcome to Login Interface</h1>
<div class="main">
    <form action="/sxrekord/loginServlet" method="post">
        username: <input type="text" name="username"/><br/><br/>
        password: <input type="password" name="password"/><br/><br/>
        <button type="submit">login</button>
    </form>
</div>
</body>
</html>

