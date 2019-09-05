package appstart.services;


import appstart.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlayGameService implements CommandLineRunner {
    @Autowired
    private  TaskService taskService;

    @Autowired
    private ItemService itemService;
    @Autowired
    private MessageResponseService messageResponseService;
    @Autowired
    private RestTemplate restTemplate;
    private static final String url = "https://dragonsofmugloar.com/api/v2/";

    List<String> result= new ArrayList<>();
    Logger log = LoggerFactory.getLogger(PlayGameService.class);
    public void playGame(Game game) {
        TasksOfGame bestTask = getBestTask(game.getGameId());
        List<Item> itemsToPurchase = getItemsToPurchase(game.getGameId());
        MessageResponse messageResponse = messageResponseService.solveTask(game.getGameId(), bestTask.getBestTask().getAdId());
        Integer goldBalance = messageResponse.getGold();
        log.info("Gold Balance is ="+goldBalance);
        log.info("Message Response "+ messageResponse.getMessage());
        log.info("Score after solving task " + messageResponse.getScore().toString());
        Integer lives = messageResponse.getLives();
        Integer expiresIn = bestTask.getBestTask().getExpiresIn();
        log.info("Life remaining" + lives);
        while(lives>0 && expiresIn>0){
            if(goldBalance>50) {
                purchase(game, goldBalance, itemsToPurchase);
            }
            TasksOfGame bestTask1 = getBestTask(game.getGameId());
            Integer expiresIn1 = bestTask1.getBestTask().getExpiresIn();
            expiresIn=expiresIn1;
            if(expiresIn>0){
                messageResponse= messageResponseService.solveTask(game.getGameId(), bestTask1.getBestTask().getAdId());
                goldBalance = messageResponse.getGold();
                lives=messageResponse.getLives();
                log.info("Gold Balance is ="+goldBalance);
                log.info("Message Response "+ messageResponse.getMessage());
                log.info("Score after solving task " + messageResponse.getScore().toString());
                log.info("Life remaining" + lives);
                log.info("Score after solving task " + messageResponse.getScore().toString());
            }
            if(lives==0) {
                log.info("Final Score" + messageResponse.getScore().toString());
                break;
            }
        }
        log.info("Game Over");
    }



    private void purchase(Game game, Integer goldBalance, List<Item> itemsToPurchase) {
        Item item = itemService.purchaseItemWith(goldBalance, itemsToPurchase);
        itemService.purchaseItem(game.getGameId(),item.getId());

    }

    public Game startGame(){
        ResponseEntity<Game> responseEntity = restTemplate.exchange(url + "game/start", HttpMethod.POST, null, Game.class, new HashMap<>());
        Game game = responseEntity.getBody();
        return  game;
    }

    public TasksOfGame getBestTask(String gameId) {
        List<Task> tasks = taskService.getTask(gameId);
        Task bestTask = taskService.getBestTask(tasks);
        TasksOfGame tasksOfGame= new TasksOfGame(gameId,bestTask,tasks);
        return tasksOfGame;
    }

    public List<Item> getItemsToPurchase(String gameId) {
        return itemService.getItems(gameId);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("starting run method in play service");
        Game game = startGame();
        playGame(game);
    }
}
