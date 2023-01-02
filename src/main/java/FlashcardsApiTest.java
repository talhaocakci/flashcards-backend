import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class FlashcardsApiTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletConfig config;

    @Mock
    ServletContext servletContext;

    @Mock
    PrintWriter printWriter;

    @Mock
    ServletInputStream servletInputStream;

    @Mock
    FlashcardRetriever flashcardRetriever;

    @Mock
    AuthenticationChecker authenticationChecker;

    @Captor
    ArgumentCaptor<String> responseStringCaptor;

    @Captor
    ArgumentCaptor<Integer> statusCaptor;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateWithNoAuthorization() throws ServletException, IOException {

        Mockito.doReturn(printWriter).when(response).getWriter();

        FlashcardsApi servlet = new FlashcardsApi(authenticationChecker, flashcardRetriever) {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.init(config);

        servlet.doPost(request, response);

        Mockito.verify(printWriter).print(responseStringCaptor.capture());

        Mockito.verify(response).setStatus(statusCaptor.capture());

        String writtenString = responseStringCaptor.getValue();
        Integer status = statusCaptor.getValue();

        System.out.println(writtenString);
        System.out.println(status);

        BasicResponse apiResponse = objectMapper.readValue(writtenString, BasicResponse.class);

        Assertions.assertEquals("Please log in to update/create cards", apiResponse.message);
        Assertions.assertEquals(403, status);

    }

    @Test
    public void testCreateWithAuthorization() throws ServletException, IOException {

        Mockito.doReturn(printWriter).when(response).getWriter();

        Mockito.doReturn("tocakci").when(authenticationChecker).getAuthenticatedUser(any(HttpServletRequest.class));

        Mockito.doReturn(servletInputStream).when(request).getInputStream();

        FlashcardPostRequest postRequest = new FlashcardPostRequest();
        postRequest.setContent("my content");

        String requestString = objectMapper.writeValueAsString(postRequest);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestString.getBytes("UTF-8"));

        Mockito.when(servletInputStream.read(any(), anyInt(), anyInt())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                byte[] output = (byte[]) args[0];
                int offset = (int) args[1];
                int length = (int) args[2];
                return byteArrayInputStream.read(output, offset, length);
            }});

        List<Flashcard> cards = List.of(new Flashcard(1, "My content", "demouser"));

        Mockito.doReturn(cards).when(flashcardRetriever).retrieveAll();

        FlashcardsApi servlet = new FlashcardsApi(authenticationChecker, flashcardRetriever) {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.init(config);

        servlet.doPost(request, response);

        Mockito.verify(printWriter).print(responseStringCaptor.capture());

        Mockito.verify(response).setStatus(statusCaptor.capture());

        String writtenString = responseStringCaptor.getValue();
        Integer status = statusCaptor.getValue();

        System.out.println(writtenString);
        System.out.println(status);

        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse apiResponse = objectMapper.readValue(writtenString, BasicResponse.class);

        Assertions.assertEquals("Card has been created", apiResponse.message);
        Assertions.assertEquals(200, status);

    }

    @Test
    public void testGetCards() throws IOException, ServletException {
        Mockito.doReturn(printWriter).when(response).getWriter();

        List<Flashcard> cards = List.of(new Flashcard(1, "My content", "demouser"));

        Mockito.doReturn(cards).when(flashcardRetriever).retrieveAll();

        FlashcardsApi servlet = new FlashcardsApi(authenticationChecker, flashcardRetriever) {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.init(config);

        servlet.doGet(request, response);

        Mockito.verify(printWriter).print(responseStringCaptor.capture());

        Mockito.verify(response).setStatus(statusCaptor.capture());

        String writtenString = responseStringCaptor.getValue();
        Integer status = statusCaptor.getValue();

        System.out.println(writtenString);
        System.out.println(status);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Flashcard> apiResponse = objectMapper.readValue(writtenString, new TypeReference<List<Flashcard>>(){});

        Assertions.assertEquals("My content", apiResponse.get(0).getContent());
        Assertions.assertEquals(200, status);

    }
}
