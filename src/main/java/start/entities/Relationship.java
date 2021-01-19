package start.entities;

import start.entities.enumerators.RelationshipStatus;

import javax.persistence.*;

@Entity
@Table(name = "relationships")
public class Relationship {
    private Integer ID;
    private RelationshipStatus status;
    private User firstUser;
    private User secondUser;

    public Relationship() {
    }

    public Relationship(RelationshipStatus status, User firstUser, User secondUser) {
        this.status = status;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getID() {
        return ID;
    }

    public RelationshipStatus getStatus() {
        return status;
    }

    @ManyToOne()
    @JoinColumn(name = "first_user_ID")
    public User getFirstUser() {
        return firstUser;
    }

    @ManyToOne()
    @JoinColumn(name = "second_user_ID")
    public User getSecondUser() {
        return secondUser;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }
}



