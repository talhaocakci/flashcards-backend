<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
 <link rel="stylesheet" href="styles.css">
 <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Trirong">
</head>

<body>
    <div class="flashcard center-screen">
        <h2>Create card</h2>
        <h3>You will create a flashcard as <c:out value="${sessionScope.username}" /></h2>
        <form action="flashcards" method="post">
            <div class="form-group">
               <textarea class="form-control" name="content" placeholder="Enter the content"></textarea>
               <button class="btn"> Save </button>
            </div>
        </form>
    </div>
</body>
</html>
