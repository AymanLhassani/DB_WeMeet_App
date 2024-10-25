package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class USUARI extends Model {
    public String Name;
    public String Password;
    public int numberTimesRent; //numero de veces que alquila un espacio de alguien
    public int numberTimesTenant; //numero de veces que alquila su espacio

    @OneToMany(mappedBy = "User_Renter")
    public List<SPACEMEETING> spaces = new ArrayList<>();

    @OneToMany(mappedBy = "User_Reserve")
    public List<MEETING> meetings  = new ArrayList<>();

    public USUARI() {}

    public USUARI(String name, String password, int numberRent, int numberR) {
        this.Name = name;
        this.Password = password;
        this.numberTimesRent = numberRent;
        this.numberTimesTenant = numberR;
    }
}
