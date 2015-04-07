import com.fasterxml.jackson.databind.JsonNode;
import com.jivesoftware.datagenerator.model.Person;
import io.nodus.Nodus;
import org.testng.annotations.Test;

/**
 * Created by erwolff on 4/1/15.
 */

@Test
public class ModelTest {


    public void testAuthor() {
        Nodus author = Nodus.builder(Person.class).build();
        JsonNode authorNode = author.getJson();
    }
}
