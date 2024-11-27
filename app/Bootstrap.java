import play.test.*;
import play.jobs.*;
import models.*;

import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
        // Load default data if the database is empty
        if(USER.count() == 0) {
            Date date = new Date();

            int date_year = date.getYear() + 1900;
            int date_month = date.getMonth() + 1;
            int date_day = date.getDay();

            USER ob1 = new USER("Ayman", "1234", 3, 1);
            ob1.save();
            USER ob2 = new USER("Enric", "dev", 1, 1);
            ob2.save();
            SPACEMEETING ob3 = new SPACEMEETING(4, "26/11/2024" ,"Castelldefels", "9h::21h",  true);
            ob3.save();
            MEETING ob4 = new MEETING(  3, "4/10/24-12h00");
            ob4.save();
            SPACEMEETING ob5 = new SPACEMEETING(6, date_day + "/" + date_month + "/" + date_year ,"Castelldefels", "9h::21h",  false);
            ob5.save();
            SPACEMEETING ob6 = new SPACEMEETING(5, "26/11/2024" ,"Castelldefels", "9h::21h",  true);
            ob6.save();
            SPACEMEETING ob8 = new SPACEMEETING(4, "26/11/2024" ,"Castelldefels", "9h::21h",  false);
            ob8.save();
            SPACEMEETING ob9 = new SPACEMEETING(6, "26/11/2024" ,"Castelldefels", "9h::21h",  true);
            ob9.save();
        }
    }
    
}