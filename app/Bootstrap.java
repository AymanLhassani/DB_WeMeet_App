import play.test.*;
import play.jobs.*;
import models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

            String DATE = date_day + mod + date_month + "/" + date_year;

            String rootDirectory = Paths.get("").toAbsolutePath().toString();

            try (BufferedReader reader = new BufferedReader(new FileReader( rootDirectory + "/WeMEET_main/public/bootstrap/database.txt"))) {
                String line = reader.readLine();
                while (line != null) {

                    if(Objects.equals(line,"USER"))
                    {
                        line = reader.readLine();

                        while (!Objects.equals(line,"*")) {
                            String[] params = line.split("~");
                            USER newuser = new USER(Integer.parseInt(params[0]), params[1], params[2], Integer.parseInt(params[3]), Integer.parseInt(params[4]), Boolean.parseBoolean(params[5]));
                            newuser.save();
                            line = reader.readLine();
                        }
                    }
                    else if(Objects.equals(line,"SPACEMEETING"))
                    {
                        line = reader.readLine();

                        while (!Objects.equals(line,"*")) {
                            String[] params = line.split("~");
                            SPACEMEETING newspace = new SPACEMEETING(Integer.parseInt(params[0]), Integer.parseInt(params[1]), DATE, params[3], Integer.parseInt(params[4]), Integer.parseInt(params[5]), params[6], Boolean.parseBoolean(params[7]), params[8], USER.getUser(params[9]));
                            newspace.save();
                            line = reader.readLine();
                        }
                    }
                    else if(Objects.equals(line,"MEETING"))
                    {
                        line = reader.readLine();

                        while (!Objects.equals(line,"*")) {
                            String[] params = line.split("~");
                            MEETING newmeet = new MEETING(Integer.parseInt(params[0]), params[1], SPACEMEETING.getSpace(Integer.parseInt(params[2])), USER.getUser(params[3]));
                            newmeet.save();
                            line = reader.readLine();
                        }
                    }

                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }











        }
    }
    
}