package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SPACEMEETING extends Model {

    public int iden;
    public int numberPeople;
    public String location;
    public String date;
    public int schedule_start;
    public int schedule_end;
    public String schedule;
    public Boolean tv;
    public String imagepath;

    @OneToMany (mappedBy = "Space_Reserve")
    public  List<MEETING> Listmeetings = new ArrayList<>();

    @ManyToOne
    public USER User_Renter;

    public SPACEMEETING(){}

    public SPACEMEETING(int iden, int numberPeople, String date, String location, int schedule_start, int schedule_end, String schedule, Boolean tv, String imagepath, USER user_Renter) {

        this.iden = iden;
        this.numberPeople = numberPeople;
        this.date = date;
        this.location = location;
        this.schedule_start = schedule_start;
        this.schedule_end = schedule_end;
        this.schedule = schedule;
        this.tv = tv;
        this.imagepath = imagepath;
        this.User_Renter = user_Renter;
    }
}