import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class PropertyReader<T> {

    public T getConfiguration(String fileName, Class clazz) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            try {
                File file = new File(resource.toURI());
                final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                return (T) mapper.readValue(file, clazz);

            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
