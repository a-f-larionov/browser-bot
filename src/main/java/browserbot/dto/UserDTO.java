//FIN
package browserbot.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserDTO {

    /**
     * id пользователя
     */
    public Long id;

    /**
     * Имя пользователя
     */
    public String username;
}
