package taplinkbot.entities;

import javax.persistence.*;

@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String phone;

    private String comment;

    private boolean isWorking;

    @Transient
    private int index;

    public long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public String getDescription() {
        return getComment() + " " + getPhone();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
