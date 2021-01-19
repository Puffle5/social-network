package start.entities.models;
import java.util.Date;

public class PostBindingModel
{
    private Integer ID;
    private String title;
    private String description;
    private String photoUrl;
    private Date date;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PostBindingModel() {}

    public PostBindingModel(Integer ID, String title, String description) {
        this.ID = ID;
        this.title = title;
        this.description = description;
    }

    public Integer getID() {return ID;}

    public String getTitle() {return title;}

    public String getDescription() {return description;}

    public Date getDate() {return date;}

    public void setID(Integer ID) {this.ID = ID;}

    public void setTitle(String title) {this.title = title;}

    public void setDescription(String description) {this.description = description;}

    public void setDate(Date date) {this.date = date;}
}


