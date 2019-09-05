/*


package appstart.controller;

import appstart.models.*;
import appstart.services.MessageResponseService;
import appstart.services.PlayGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/game")
public class GameController
{
    @Autowired
    private PlayGameService playGameService;
    private MessageResponseService messageResponseService;

    public GameController(PlayGameService playGameService) {
        this.playGameService = playGameService;
    }

    @RequestMapping("/start")
   public Game startGame(){
        Game game = playGameService.startGame();
        return game;
    }
    @RequestMapping("/getBestTask/{gameId}")
    public TasksOfGame getBestTask(@PathVariable String gameId){
        TasksOfGame tasks = playGameService.getBestTask(gameId);
        return tasks;
    }

    @RequestMapping("/getItems/{gameId}")
    public List<Item> getItems(@PathVariable String gameId){
        return playGameService.getItemsToPurchase(gameId);
    }

    @RequestMapping("/solve/{gameId}/{taskId}")
    public MessageResponse getItems(@PathVariable String gameId,@PathVariable String taskId){
        return messageResponseService.solveTask(gameId,taskId);
    }

    @RequestMapping("/play")
    public List<String> playGame(){
        Game game = playGameService.startGame();
        List<String> result = playGameService.playGame(game);
        return result;
    }
}

*/
