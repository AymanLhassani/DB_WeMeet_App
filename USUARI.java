package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class USUARI extends Model {
    public String name;
    public String password;
    public int numberTimesRent; //numero de veces que alquila un espacio de alguien
    public int numberTimesTenant; //numero de veces que alquila su espacio

    @ManyToOne
    public MEETING meeting_O;

    public USUARI() {}

    public USUARI(String name, String password, int numberRent, int numberR) {
        this.name = name;
        this.password = password;
        this.numberTimesRent = numberRent;
        this.numberTimesTenant = numberR;
    }
}
