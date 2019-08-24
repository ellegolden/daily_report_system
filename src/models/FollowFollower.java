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
                query = "SELECT f FROM FollowFollower AS f WHERE f.follower_employee = :follower ORDER BY f.id DESC"
                ),
        @NamedQuery(
                name = "getMyFollowsCount",
                query = "SELECT COUNT(f) FROM FollowFollower AS f WHERE f.follower_employee = :follower"
                ),
        @NamedQuery(
                name = "getFollowFollower",
                query = "SELECT f FROM FollowFollower AS f WHERE f.follower_employee = :follower AND f.follow_employee = :follow"
                )
})

public class FollowFollower {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follow_employee_id", nullable = false)
    private Employee follow_employee;

    @ManyToOne
    @JoinColumn(name = "follower_employe_id", nullable = false)
    private Employee follower_employee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollow_employee() {
        return follow_employee;
    }

    public void setFollow_employee(Employee follow_employee) {
        this.follow_employee = follow_employee;
    }

    public Employee getFollower_employee() {
        return follower_employee;
    }

    public void setFollower_employee(Employee follower_employee) {
        this.follower_employee = follower_employee;
    }
}