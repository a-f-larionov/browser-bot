package taplinkbot.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter
    //@tdod Validator phone number
    private String phone;

    @Getter
    private String comment;

    @Getter
    @Setter
    private boolean isWorking;

    /**
     * ManagerRotator использует это
     */
    @Transient
    @Getter
    @Setter
    private int index;

    public String getDescription() {
        return getComment() + " " + getPhone();
    }
}
