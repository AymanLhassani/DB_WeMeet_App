package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class MEETING extends Model {
    public int numberPeopleMeeting;
    public String schedule;

    @ManyToOne
    public SPACEMEETING Space_Reserve;

    @ManyToOne
    public USUARI User_Reserve;

    public MEETING(){}

    public MEETING(int numberPeopleMeeting, String schedule) {
        this.numberPeopleMeeting = numberPeopleMeeting;
        this.schedule = schedule;
    }
}