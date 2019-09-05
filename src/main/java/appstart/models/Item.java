package appstart.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private String name;
    private Integer cost;



}
