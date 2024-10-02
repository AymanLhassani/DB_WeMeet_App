package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class SPACEMEETING extends Model {
    public int numberPeople;
    public String scheduleAvailable;
    public String location;
    public Boolean ventilation;

    @OneToMany (mappedBy = "spacemeeting_O")
    public  List<MEETING> meetings;

    public SPACEMEETING(){}

    public SPACEMEETING(int numberPeople, String scheduleAvailable, String location, Boolean ventilation) {
            this.numberPeople = numberPeople;
            this.scheduleAvailable = scheduleAvailable;
            this.location = location;
            this.ventilation = ventilation;
    }
}
