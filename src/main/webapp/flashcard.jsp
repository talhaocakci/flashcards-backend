<html>

<head>
 <link rel="stylesheet" href="styles.css">
 <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Trirong">
</head>

<body>
 <% if (request.getAttribute("id") != null ) {%>
    <div class="center-screen flashcard">
         <%= request.getAttribute("content") %>
    </div>
 <% } else {%>
     <div class="center-screen error">
        <%= request.getAttribute("error") %>
     </div>
 <% }%>
</body>
</html>
