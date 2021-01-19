package start.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    private Integer ID;
    private String content;
    private Boolean active;
    private Date date;
    private Post post;
    private User user;

    public Comment() {
    }

    public Comment(Integer ID, String content, Boolean active) {
        this.ID = ID;
        this.content = content;
        this.active = active;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "active")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne()
    @JoinColumn(name = "post_ID")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    @ManyToOne()
    @JoinColumn(name = "user_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
