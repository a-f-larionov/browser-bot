package taplinkbot.managers;

public enum Manager {

    Manager9206("+79066919206", "Менеджер буднего дня 1"),
    Manager3990("+79606923990", "Менеджер буднего дня 2"),
    Manager9434("+79207039434", "Менеджер буднего дня 3"),
    Manager0307("+79102790307", "Менеджер буднего дня 4");

    private final String phone;
    private final String comment;

    Manager(String phone, String comment) {
        this.phone = phone;
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return getComment() + " " + getPhone();
    }
}
