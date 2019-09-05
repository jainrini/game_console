package appstart.services;

import appstart.models.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TaskService {
    private static final String url = "https://dragonsofmugloar.com/api/v2/";
    private static RestTemplate restTemplate = new RestTemplate();
    private static final Map<String,Integer> probabilitiesMap = new HashMap<>();
    static {
        probabilitiesMap.put("Piece of cake",9);
        probabilitiesMap.put("Walk in the park",3);
        probabilitiesMap.put("Sure thing",10);
        probabilitiesMap.put("Quite likely",8);
        probabilitiesMap.put("Hmm....",4);
        probabilitiesMap.put("Gamble",7);
        probabilitiesMap.put("Rather detrimental",6);
        probabilitiesMap.put("Risky",5);
        probabilitiesMap.put("Playing with fire",2);
        probabilitiesMap.put("Suicide mission",1);
        probabilitiesMap.put("Impossible",0);
    }
    public List<Task> getTask(String gameId) {

        Map<String, String> params = new HashMap<>();
        params.put("gameId", gameId);
        ResponseEntity<Task[]> exchange = restTemplate.exchange(url + "{gameId}/messages", HttpMethod.GET, null, Task[].class, params);
        Task[] tasks = exchange.getBody();
        return Arrays.asList(tasks);
    }

    public Task getBestTask(List<Task> tasks) {
        int avgProbability = getAvgProbability(tasks);
        List<Task> withHighestProbability= new ArrayList<>();
        List<Task> withMinProbability= new ArrayList<>();
        tasks.forEach(adventure -> {
            if(probabilitiesMap.containsKey(adventure.getProbability())){
                if(probabilitiesMap.get(adventure.getProbability())>avgProbability){
                    withHighestProbability.add(adventure);
                }
                else{
                    withMinProbability.add(adventure);
                }
            }
        });

        if(!withHighestProbability.isEmpty()) {
            List<Task> highExpiry = withHighExpiry(withHighestProbability);
            highExpiry.stream().forEach(t->Integer.parseInt(t.getReward()));
            //Task task = withHighestProbability.stream().max(Comparator.comparing(Task::getReward)).orElseThrow(NoSuchElementException::new);
            sortByReward(highExpiry);
            return highExpiry.get(0);
        }
        else {
            //getTask with highest reward
            //Task task = withMinProbability.stream().filter(h -> h.getReward() != null).max(Comparator.comparing(Task::getReward)).get();
            List<Task> highExpiry = withHighExpiry(withMinProbability);
            highExpiry.stream().forEach(t->Integer.parseInt(t.getReward()));
            //Task task = withHighestProbability.stream().max(Comparator.comparing(Task::getReward)).orElseThrow(NoSuchElementException::new);
            sortByReward(highExpiry);
            return highExpiry.get(0);
        }
    }

    private List<Task> withHighExpiry(List<Task> withHighestProbability) {
        int avgExpiry = withHighestProbability
                .stream()
                .map(Task::getExpiresIn)
                .reduce(0, Integer::sum) / withHighestProbability.size();
        List<Task> withHighExpiry= new ArrayList<>();
        List<Task> lowExpiry= new ArrayList<>();
        for(Task t:withHighestProbability){
            if(t.getExpiresIn()>avgExpiry){
                withHighExpiry.add(t);
            }
            else {
                lowExpiry.add(t);
            }
        }
        if(withHighExpiry.isEmpty()){
            return lowExpiry;
        }
        return withHighExpiry;

    }

    private void sortByReward(List<Task> task) {
        Collections.sort(task, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                    int n1=Integer.parseInt(o1.getReward());
                    int n2=Integer.parseInt(o2.getReward());
                    if (n1<=n2){
                        return 1;
                    }
                    return -1;
            }
        });
    }

    private int getAvgProbability(List<Task> tasks) {
      return tasks
                .stream()
                .map(Task::getProbability)
                .filter(p->probabilitiesMap.containsKey(p))
                .map(probability -> probabilitiesMap.get(probability))
                .reduce(0, Integer::sum) / tasks.size();

    }
}
