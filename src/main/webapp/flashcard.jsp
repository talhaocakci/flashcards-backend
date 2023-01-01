<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>

<head>
 <link rel="stylesheet" href="styles.css">
 <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Trirong">
</head>

<body>
    <c:choose>
         <c:when test = "${empty error}" >
             <div class="center-screen">
                  <c:forEach items="${cards}" var="card">
                     <div class="flashcard">
                            <c:out value="${card.content}"/>
                     </div>
                  </c:forEach>
              </div>
         </c:when>
         <c:otherwise>
            <div class="center-screen error">
                <c:out value="${error}" default="Something wrong happened"/>
            </div>
          </c:otherwise>
     </c:choose>
</body>
</html>
