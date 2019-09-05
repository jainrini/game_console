package appstart.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TasksOfGame {

    private String gameId;
    private Task bestTask;
    private List<Task> allTask;

    public TasksOfGame(String gameId, Task bestTask, List<Task> allTask) {
        this.gameId = gameId;
        this.bestTask = bestTask;
        this.allTask = allTask;
    }
}
