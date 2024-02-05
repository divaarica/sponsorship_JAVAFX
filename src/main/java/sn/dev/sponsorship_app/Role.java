package sn.dev.sponsorship_app;

public class Role {
    private int id, actived;
    private String name;

    public Role() {
    }

    public Role(int id, int actived, String name) {
        this.id = id;
        this.actived = actived;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActived() {
        return actived;
    }

    public void setActived(int actived) {
        this.actived = actived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
