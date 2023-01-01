<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
 <link rel="stylesheet" href="styles.css">
 <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Trirong">
</head>

<body>
    <div class="flashcard center-screen">
        <h2>Login</h2>
        <form action="login" method="post">
            <div class="form-group">
               <p class = "errormessage"><c:out value="${message}"/></p>
               <input class="form-control" name="username" placeholder="Username"/>
               <input type="password" name="password" class="form-control" placeholder="Password"/>
               <button class="btn"> Login </button>
            </div>
        </form>
    </div>
</body>
</html>
