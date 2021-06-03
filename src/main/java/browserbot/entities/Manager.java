//FIN
package browserbot.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@ToString
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Pattern(regexp = "^\\+7\\d{10}$")
    private String phone;

    @NotNull
    private String comment;

    @NotNull
    private boolean isWorking;

    /**
     * ManagerRotator использует это
     * //@todo
     */
    @Transient
    private int index;

    public String getDescription() {
        return getComment() + " " + getPhone();
    }
}
