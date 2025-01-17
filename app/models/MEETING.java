package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MEETING extends Model {
    public int numberPeopleMeeting;
    public String schedule;

    @ManyToOne
    public SPACEMEETING Space_Reserve;

    @ManyToOne
    public USER User_Reserve;

    public MEETING(){}

    public MEETING(int numberPeopleMeeting, String schedule, SPACEMEETING space_Reserve, USER user_Reserve) {
        this.numberPeopleMeeting = numberPeopleMeeting;
        this.schedule = schedule;
        this.Space_Reserve = space_Reserve;
        this.User_Reserve = user_Reserve;
    }
}