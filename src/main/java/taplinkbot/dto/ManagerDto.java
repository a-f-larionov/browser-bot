package taplinkbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
public class ManagerDto {

    private long id;

    private String phone;

    private String comment;

    @JsonProperty("isWorking")
    private boolean isWorking;
}
