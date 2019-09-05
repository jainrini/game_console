import appstart.models.Game;
import appstart.models.Item;
import appstart.models.MessageResponse;
import appstart.models.Task;
import appstart.services.ItemService;
import appstart.services.MessageResponseService;
import appstart.services.PlayGameService;
import appstart.services.TaskService;
import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameApplicationTest {
    @Mock
    private TaskService taskService;
    @Mock
    private ItemService itemService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private MessageResponseService messageResponseService;
    @InjectMocks
    private PlayGameService service;
    private static final String url = "https://dragonsofmugloar.com/api/v2/";
    @Test
    public void testRun() throws Exception {
        when(restTemplate.exchange(url + "game/start", HttpMethod.POST, null, Game.class, new HashMap<>())).
                thenReturn(ResponseEntity.ok(Game.builder().gameId("1").gold(100).highScore(100).lives(5).score(40).level(2).build()));
        Map<String, String> params = new HashMap<>();
        params.put("gameId", "1");
        Task task = Task.builder().adId("1").expiresIn(3).message("Hello Adventure 1").
                probability("Sure thing").reward("20").build();
        Task task2 = Task.builder().adId("2").expiresIn(3).message("Hello Adventure 2").
                probability("Impossible").reward("20").build();
        List<Task> taskList = Arrays.asList(new Task[]{task, task2});
        MessageResponse messageResponse = MessageResponse.builder().gold(20).highScore(100).score(1000).lives(0).message("Success").success(true).turn(0).build();
        Item item = Item.builder().cost(20).id("1").name("Key").build();
        when(taskService.getTask("1")).thenReturn(Arrays.asList(new Task[]{task,task2}));
        when(taskService.getBestTask(taskList)).thenReturn(task);
        when(messageResponseService.solveTask("1","1")).thenReturn(messageResponse);
        when(restTemplate.exchange(url + "{gameId}/messages", HttpMethod.GET, null, Task[].class, params)).
                thenReturn(ResponseEntity.ok(new Task[]{task}));
        when(restTemplate.exchange(url + "{gameId}/shop", HttpMethod.GET, null, Item[].class, params)).
                thenReturn(ResponseEntity.ok(new Item[]{item}));
        when(restTemplate.exchange(url + "{gameId}/solve/{adId}", HttpMethod.POST, null, MessageResponse.class, params))
                .thenReturn(ResponseEntity.ok(messageResponse));
        service.run(Strings.EMPTY);
        Assert.assertNotNull(messageResponse);
    }
}
