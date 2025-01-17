import play.test.*;
import play.jobs.*;
import models.*;

import java.util.Date;
import java.util.List;

@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
        // Load default data if the database is empty
        if(USER.count() == 0) {
            Date date = new Date();

            int date_year = date.getYear() + 1900;
            int date_month = date.getMonth() + 1;
            int date_day = date.getDate();

            String mod = "/";   //arregla problema amb els mesos 01, 02 ... 09 amb el display sense 0 (1, 2 ... 9)
            if(date_month < 10)
            {
                mod = mod + "0";
            }

            USER user_mayor = new USER("Ajuntament", "castefa", 0, 5);
            user_mayor.save();

            SPACEMEETING spc1 = new SPACEMEETING(1, 4, date_day + mod + date_month + "/" + date_year , "Castelldefels", 9, 21, "9-11 14-15"  , true, "/public/images/space1_4ppl.jpg", user_mayor);
            spc1.save();
            SPACEMEETING spc2 = new SPACEMEETING(2, 6, date_day + mod + date_month + "/" + date_year , "Castelldefels", 9, 14,null,  true, "/public/images/space2_6ppl.jpg", user_mayor);
            spc2.save();
            SPACEMEETING spc3 = new SPACEMEETING(3, 5, date_day + mod + date_month + "/" + date_year , "Castelldefels", 8, 20,null, true, "/public/images/space3_5ppl.jpg", user_mayor);
            spc3.save();
            SPACEMEETING spc4 = new SPACEMEETING(4, 4, date_day + mod + date_month + "/" + date_year , "Castelldefels", 15, 21,null, false, "/public/images/backrooms.jpg", user_mayor);
            spc4.save();
            SPACEMEETING spc5 = new SPACEMEETING(5, 6, date_day + mod + date_month + "/" + date_year , "Castelldefels", 10, 19,null, true, "/public/images/space5_6ppl.jpg", user_mayor);
            spc5.save();

            USER us1 = new USER("Ayman", "1234", 3, 1);
            us1.save();
            USER us2 = new USER("Enric", "dev", 1, 1);
            us2.save();
            MEETING mtng1 = new MEETING(3, "4/10/24-12h00", null, null);
            mtng1.save();


        }
    }
    
}