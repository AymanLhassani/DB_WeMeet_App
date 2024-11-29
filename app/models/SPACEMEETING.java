package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SPACEMEETING extends Model {

    public int numberPeople;
    public String location;
    public String date;
    public String scheduleAvailable;
    public Boolean tv;
    public String imagepath;

    @OneToMany (mappedBy = "Space_Reserve")
    public  List<MEETING> Listmeetings = new ArrayList<>();

    @ManyToOne
    public USER User_Renter;

    public SPACEMEETING(){}

    public SPACEMEETING(int numberPeople, String date, String location, String scheduleAvailable, Boolean tv, String imagepath) {

        this.numberPeople = numberPeople;
        this.date = date;
        this.location = location;
        this.scheduleAvailable = scheduleAvailable;
        this.tv = tv;
        this.imagepath = imagepath;
    }
}