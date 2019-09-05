package appstart.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingResponse {
    private boolean shoppingSuccess;
    private Integer lives;
    private Integer gold;
    private Integer level;
    private Integer turn;

 }
