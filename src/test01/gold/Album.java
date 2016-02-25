package test01.gold;

public class Album {
    private String id;
    private String owner;
    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public Album setId(String id) {
        this.id = id;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public Album setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Album setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Album setDescription(String description) {
        this.description = description;
        return this;
    }
}
