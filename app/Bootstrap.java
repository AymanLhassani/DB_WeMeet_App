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
            int date_day = date.getDate();

            SPACEMEETING spc1 = new SPACEMEETING(4, "26/11/2024" , "Castelldefels", "9h::21h", true, "/public/images/space1_4ppl.jpg");
            spc1.save();
            SPACEMEETING spc2 = new SPACEMEETING(6, date_day + "/" + date_month + "/" + date_year , "Castelldefels", "9h::21h",  true, "/public/images/space2_6ppl.jpg");
            spc2.save();
            SPACEMEETING spc3 = new SPACEMEETING(5, "26/11/2024" , "Castelldefels", "9h::21h", true, "/public/images/space3_5ppl.jpg");
            spc3.save();
            SPACEMEETING spc4 = new SPACEMEETING(4, "26/11/2024" , "Castelldefels", "9h::21h", false, "/public/images/space4_4ppl.jpg");
            spc4.save();
            SPACEMEETING spc5 = new SPACEMEETING(6, "26/11/2024" , "Castelldefels", "9h::21h", true, "/public/images/space5_6ppl.jpg");
            spc5.save();

            USER us1 = new USER("Ayman", "1234", 3, 1);
            us1.save();
            USER us2 = new USER("Enric", "dev", 1, 1);
            us2.save();
            MEETING mtng1 = new MEETING(  3, "4/10/24-12h00");
            mtng1.save();


        }
    }
    
}