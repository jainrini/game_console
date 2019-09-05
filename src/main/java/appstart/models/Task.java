package appstart.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task{
    private String adId;
    private String message;
    private String reward;
    private Integer expiresIn;
    private String probability;
}
