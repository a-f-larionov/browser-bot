package taplinkbot.managers;

public enum Manager {

    Manager9206("+79066919206", "Менеджер 1 9206"),
    Manager3990("+79606923990", "Менеджер 2 3990"),
    Manager9434("+79207039434", "Менеджер 3 9434"),
    Manager0307("+79102790307", "Менеджер 4 0307");

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
