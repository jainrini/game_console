package appstart.services;

import appstart.models.Item;
import appstart.models.ShoppingResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private static final String url = "https://dragonsofmugloar.com/api/v2/";
    private static RestTemplate restTemplate = new RestTemplate();
    public List<Item> getItems(String gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("gameId",gameId);
        ResponseEntity<Item[]> itemsFromShop= restTemplate.exchange(url + "{gameId}/shop", HttpMethod.GET, null, Item[].class, params);
        Item[] items = itemsFromShop.getBody();
        return Arrays.asList(items);
    }

    public Item purchaseItemWith(Integer goldBalance, List<Item> itemsToPurchase) {
        List<Item> collect = itemsToPurchase.stream().filter(c -> c.getCost() <= goldBalance).collect(Collectors.toList());
        int i = averageOfItems(collect);
        int nearToAverage = findNearToAverage(i, collect);
        Optional<Item> first = collect.stream().filter(c -> c.getCost().equals(nearToAverage)).findFirst();
        if(first.isPresent())
        return first.get();
        else {
            throw  new NoSuchElementException();
        }
    }

    private int findNearToAverage(int i, List<Item> collect) {
        Integer[] cost = collect.stream().map(c -> c.getCost()).toArray(Integer[]::new);
        int closest = findClosest(cost, i);
        return closest;
    }
    public static int findClosest(Integer arr[], int target)
    {
        int n = arr.length;
        if (target <= arr[0])
            return arr[0];
        if (target >= arr[n - 1])
            return arr[n - 1];

        // do binary search
        int i = 0, j = n, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (arr[mid] == target)
                return arr[mid];

            /* If target is less than array element,
               then search in left */
            if (target < arr[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr[mid - 1])
                    return getClosest(arr[mid - 1],
                            arr[mid], target);

                /* Repeat for left half */
                j = mid;
            }

            // If target is greater than mid
            else {
                if (mid < n-1 && target < arr[mid + 1])
                    return getClosest(arr[mid],
                            arr[mid + 1], target);
                i = mid + 1; // update i
            }
        }

        // Only single element left after search
        return arr[mid];
    }
    public static int getClosest(int val1, int val2,
                                 int target)
    {
        if (target - val1 >= val2 - target)
            return val2;
        else
            return val1;
    }

    private int averageOfItems(List<Item> collect) {
        return collect.stream()
                .map(Item::getCost)
                .reduce(0, Integer::sum) / collect.size();
    }

    public void purchaseItem(String gameId, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("gameId", gameId);
        params.put("itemId", id);
        ResponseEntity<ShoppingResponse> itemsFromShop= restTemplate.exchange(url + "{gameId}/shop/buy/{itemId}", HttpMethod.POST, null, ShoppingResponse.class, params);
        ShoppingResponse items = itemsFromShop.getBody();
    }
}
