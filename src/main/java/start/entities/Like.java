package start.entities;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    private Integer ID;
    private Boolean active;
    private Post post;
    private User user;

    public Like() {
    }

    public Like(Integer ID, Boolean active) {
        this.ID = ID;
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

    @Column(name = "active")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
