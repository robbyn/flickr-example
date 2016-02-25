package test01.gold;

public class PhotoInfo {
    private String id;
    private String name;
    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public PhotoInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PhotoInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PhotoInfo setDescription(String description) {
        this.description = description;
        return this;
    }
}
