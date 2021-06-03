//FIN
package browserbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ManagerDTO {

    public long id;

    /**
     * Номер телефона
     */
    public String phone;

    /**
     * Комментарий
     */
    public String comment;

    /**
     * Работае ли сейчас менеджер
     */
    @JsonProperty("isWorking")
    public boolean isWorking;
}
