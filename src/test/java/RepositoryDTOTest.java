import monolith2microservice.shared.dto.RepositoryDto;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by gmazlami on 12/7/16.
 */
public class RepositoryDTOTest {

    private RepositoryDto repositoryDTO;

    @Before
    public void setUp(){
        repositoryDTO = new RepositoryDto();
        repositoryDTO.setUri("https://github.com/feincms/feincms.git");
    }

    @Test
    public void testGetName(){
        assertEquals("feincms",repositoryDTO.getName());
    }
}
