package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "follow_follower")
@NamedQueries({
    @NamedQuery(
        name = "getMyAllFollows",
        query = "SELECT f FROM FollowFollower AS f WHERE f.follower = :follow ORDER BY f.id DESC"
        ),
    @NamedQuery(
        name = "getMyFollowsCount",
        query = "SELECT COUNT(f) FROM FollowFollower AS f WHERE f.follower = :follower"
        )
})
public class FollowFollower {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follow_employee_id", nullable = false)
    private Employee follow;

    @ManyToOne
    @JoinColumn(name = "follower_employee_id", nullable = false)
    private Employee follower;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollow() {
        return follow;
    }

    public void setFollow(Employee follow) {
        this.follow = follow;
    }

    public Employee getFollower() {
        return follower;
    }

    public void setFollower(Employee follower) {
        this.follower = follower;
    }
 }
