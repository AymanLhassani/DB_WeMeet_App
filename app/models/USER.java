package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class USER extends Model {
    @Id
    public int iden;

    public String Name;
    public String Password;
    public int numberTimesRent; //numero de veces que alquila un espacio de alguien
    public int numberTimesTenant; //numero de veces que alquila su espacio
    public boolean admin;

    @OneToMany(mappedBy = "User_Renter")
    public List<SPACEMEETING> spaces = new ArrayList<>();

    @OneToMany(mappedBy = "User_Reserve")
    public List<MEETING> meetings  = new ArrayList<>();

    public USER() {}

    public USER(int iden, String name, String password, int numberRent, int numberR, boolean admin) {
        this.iden = iden;
        this.Name = name;
        this.Password = password;
        this.numberTimesRent = numberRent;
        this.numberTimesTenant = numberR;
        this.admin = admin;
    }

    public static USER getUser(String name){
        String[] params = name.split(",");
        USER u = USER.find("byNameAndPassword", params[0], params[1]).first();
        return u;
    }
}