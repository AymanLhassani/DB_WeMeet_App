import play.test.*;
import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
        // Load default data if the database is empty
        if(USER.count() == 0) {
            USER ob1 = new USER("Ayman", "1234", 3, 1);
            ob1.save();
            USER ob2 = new USER("Enric", "dev", 1, 1);
            ob2.save();
            SPACEMEETING ob3 = new SPACEMEETING(4, "Monday-9h::21h","Castelldefels",  true);
            ob3.save();
            MEETING ob4 = new MEETING(  3, "4/10/24-12h00");
            ob4.save();
        }
    }
    
}