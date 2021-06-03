package browserbot.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //@tdod Validator phone number, setter to validate it
    private String phone;

    private String comment;

    private boolean isWorking;

    /**
     * ManagerRotator использует это
     */
    @Transient
    private int index;

    public String getDescription() {
        return getComment() + " " + getPhone();
    }
}
