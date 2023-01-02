
1- Creating flashcard with post request:

     curl -X POST --data "content=new content" localhost:8080/flashcards-1.0/flashcards 


2- Get flashcards over API

    curl -X GET 'localhost:8080/flashcards-1.0/api/flashcards'  

3- Login over API


    curl -X POST 'localhost:8080/flashcards-1.0/api/login' \
    -v -H 'Content-Type: application/json' \
    -d '{"userName":"demouser","password":"12345"}'


4- Create flashcard over API

     curl --cookie 'JSESSIONID=CHANGE_THIS_TO_JSESSIONID_FROM_THE_LOGINRESPONSE'  \ 
     -X POST "localhost:8080/flashcards-1.0/api/flashcards" \ 
     -H 'Content-Type: application/json' \ -d '{"content":"my experimental quote"}'   


5- Create flashcard by using JWT token acquired from login response

    curl -X POST "localhost:8080/flashcards-1.0/api/flashcards" \
    -H 'Authorization: PUT_TOKEN_FROM_THE_RESPONSE_OF_LOGIN_REQUEST' \
    -H 'Content-Type: application/json' \
    -d '{"content":"my experimental quote"}'

6- Update card with demouser user

5- Create flashcard by using JWT token acquired from login response

    curl -X POST "localhost:8080/flashcards-1.0/api/flashcards" \
    -H 'Authorization: eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJqYXZhdGhsb24iLCJzdWIiOiJkZW1vdXNlciIsInNjb3BlIjoiYWRtaW4iLCJpYXQiOjE2NzI2NTg2NTUsImV4cCI6MTY3MjY2MTY1NX0.Yo2vPFXVt897FsCQiV3djlybfDexLseN32_1YVAHUkL-Y5tAbtWM7PfgVv3Kh0QarIUFyjjNcSFsaID7cmBYiA' \
    -H 'Content-Type: application/json' \
    -d '{"content":"my experimental quote", "id": 1}'