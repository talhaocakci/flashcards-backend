<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
 <link rel="stylesheet" href="styles.css">
 <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Trirong">
</head>

<body>
    <div class="flashcard center-screen">
        <h2>You are inside, enjoy the app, <c:out value="${sessionScope.username}" /></h2>
         <a href ="createflashcard.jsp">Create flashcard</a>
         <br/>
         <a href ="flashcards">See all flashcards</a>
    </div>
</body>
</html>
