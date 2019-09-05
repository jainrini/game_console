package appstart.services;
import appstart.models.MessageResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageResponseService {
    private TaskService taskService;
    private ItemService itemService;
    private static final String url = "https://dragonsofmugloar.com/api/v2/";
    private static RestTemplate restTemplate = new RestTemplate();

    public MessageResponse solveTask(String gameId, String adId) {

            Map<String, String> params = new HashMap<>();
            params.put("gameId", gameId);
            params.put("adId", adId);
            ResponseEntity<MessageResponse> responseEntity = restTemplate.exchange(url + "{gameId}/solve/{adId}", HttpMethod.POST, null, MessageResponse.class, params);

        return responseEntity.getBody();
    }
}
