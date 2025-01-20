package controllers;

import com.mchange.v1.identicator.IdList;
import org.h2.engine.User;
import play.mvc.*;

import models.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Application extends Controller
{

    public static void index()
    {
        User_Service = null;
        username = "";
        session.clear();
        renderTemplate("Application/init.html");
    }

    public static void LoginHTML()
    {
        renderTemplate("Application/login.html");
    }

    public static void RegisterHTML()
    {
        renderTemplate("Application/register.html");
    }

    public static void HomeHTML()
    {
        renderTemplate("Application/home.html");
    }
    public static void ProfileHTML()
    {
        renderTemplate("Application/profile.html");
    }

    public static USER User_Service;
    public static String username;
    public static int cont_login = 0;
    public static int cont_register = 0;

    public static String rootDirectory = Paths.get("").toAbsolutePath().toString();
    public static String DataBaseDirectory = "/WeMEET_main/public/bootstrap/database.txt";

    @Before
    static void connectedUser() {
        username = session.get("username");
    }

    public static void Register(String n, String p)
    {
        USER u = USER.find("byName", n).first();

        if ((u == null) && (n != null) && (p != null))
        {
            List<USER> all_users = USER.findAll();
            USER user = new USER(all_users.size() + 1, n, p, 0, 0, false).save();
            User_Service = user;
            connectedUser();

            User_Service.save();
            String user_info = user.iden + "~" + user.Name + "~" + user.Password + "~" + user.numberTimesRent + "~" + user.numberTimesTenant + "~" + user.admin;
            User_Service.UpdateOnDataBase(rootDirectory + DataBaseDirectory, User_Service.iden, "0", user_info, 0);

            Home(false,"", "", "");
        }
        else if (u != null)  //user already exists
        {
            renderArgs.put("error", 1);
            renderTemplate("Application/register.html");
        }
        else    //first try
        {
            renderTemplate("Application/register.html");
        }
    }

    public static void Login(String n, String p)
    {
        USER u = USER.find("byNameAndPassword", n, p).first();

        if ((u == null) && (cont_login == 0))   //first time logging in
        {
            cont_login = 1;
            renderArgs.put("times", 0);
            renderTemplate("Application/login.html");
        }
        else if ((u == null) && (cont_login != 0))  //second time logging in with error
        {
            cont_login = 0;
            renderArgs.put("times", 1);
            renderTemplate("Application/login.html");
        }
        else
        {
            connectedUser();
            User_Service = u;
            if(!u.admin)
            {
                Home(false,"", "", "");
            }
            else
            {
                Admin(false);
            }

        }
    }

    public static void Home(boolean do_spaces, String location, String date, String people)
    {
        if(do_spaces)
        {
            Spaces(location, date, people, false, 0, null, null, 0);
        }
        else
        {
            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);
            renderTemplate("Application/home.html");    //equivalent to HomeHTML
        }

    }

    public static void Admin(boolean do_logout)
    {
        if(do_logout){
            index();
        }
        else {
            List<SPACEMEETING> all_spaces = SPACEMEETING.findAll();
            List<USER> all_users = USER.findAll();
            List<MEETING> all_meets = MEETING.findAll();

            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);
            renderArgs.put("list_of_spaces", all_spaces);       //contains only the specified spaces
            renderArgs.put("list_of_users", all_users);       //contains only the specified users
            renderArgs.put("list_of_meets", all_meets);       //contains only the specified meetings
            renderTemplate("Application/admin.html");
        }
    }


    public static void Spaces(String location_search, String date_search, String people_search, boolean do_reserve, int id,
                              String new_schedule, String schedule_meeting, int number_people)
    {
        if(do_reserve)
        {
            //update schedule space_meeting
            List<SPACEMEETING> all_spaces = SPACEMEETING.findAll();
            List<MEETING> meetings = MEETING.findAll();

            SPACEMEETING space_updated = all_spaces.get(id - 1);
            space_updated.schedule = new_schedule;
            space_updated.save();
            space_updated.UpdateOnDataBase(rootDirectory + DataBaseDirectory, space_updated.iden, new_schedule, null, 1);


            MEETING new_meeting = new MEETING(number_people, schedule_meeting, space_updated, User_Service);
            new_meeting.save();
            String meeting_info = number_people + "~" + schedule_meeting + "~" + space_updated.iden + "~" + User_Service.Name + "," + User_Service.Password;
            new_meeting.UpdateOnDataBase(rootDirectory + DataBaseDirectory, meetings.size(), null, meeting_info, 0);

            USER user = USER.getUser(User_Service.Name + "," + User_Service.Password);
            user.meetings.add(new_meeting);
            user.spaces.add(space_updated);
            user.numberTimesRent += 1;

            User_Service = user;
            User_Service.save();
            User_Service.UpdateOnDataBase(rootDirectory + DataBaseDirectory, User_Service.iden, String.valueOf(User_Service.numberTimesRent) , null, 1);

            Home(false,"", "", "");
        }
        else    //default
        {
            String[] data_div = date_search.split("-");
            String date_mod = data_div[2] + "/" + data_div[1] + "/" + data_div[0];

            List<SPACEMEETING> all_spaces = SPACEMEETING.find("byLocationAndDate", location_search, date_mod).fetch();
            List<SPACEMEETING> spaces = new ArrayList<>();
            for(SPACEMEETING sp : all_spaces)
            {
                if(sp.numberPeople >= Integer.parseInt(people_search))
                {
                    spaces.add(sp);
                }
            }
            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);
            renderArgs.put("list_of_spaces", spaces);       //contains only the specified spaces
            renderArgs.put("location", location_search);
            renderArgs.put("date", date_mod);
            renderArgs.put("people", people_search);
            renderTemplate("Application/spaces.html");
        }

    }

    public static void Profile(boolean do_delete, boolean do_update, boolean do_logout, boolean do_create_space, String new_password, String space_loc, String space_date, String space_people, String space_start, String space_end, String space_tv, String path_picture )
    {
        if(do_delete)
        {
            /*
            List<SPACEMEETING> spaces_user = SPACEMEETING.find("byUser_Renter_Iden", User_Service.iden).fetch();
            List<MEETING> meetings = MEETING.find("byUser_Reserve_Iden", User_Service.iden).fetch();
            */
            List<SPACEMEETING> spaces = SPACEMEETING.findAll();
            List<MEETING> meetings = MEETING.findAll();

            USER user = USER.getUser(User_Service.Name + "," + User_Service.Password);
            if((user.spaces != null) || (user.spaces.size() != 0)){
                for(SPACEMEETING sp : user.spaces)
                {
                    sp.UpdateOnDataBase(rootDirectory + DataBaseDirectory, sp.iden, null, null, 2);
                    /*
                    int i = spaces.size() - 1;
                    while(sp.iden - 1 < i){
                        spaces.get(i - 1).iden = i;
                        spaces.get(i - 1).save();
                        i--;
                    }
                    */
                    sp.delete();
                    user.save();
                }
            }
            if((user.meetings != null) || (user.meetings.size() != 0)) {
                for (MEETING mt : user.meetings) {
                    String meeting_info = mt.numberPeopleMeeting + "~" + mt.schedule + "~" + mt.Space_Reserve.iden + "~" + user.Name + "," + user.Password;
                    mt.UpdateOnDataBase(rootDirectory + DataBaseDirectory, meetings.size(), meeting_info, null, 1);
                    mt.delete();
                    user.save();
                }
            }

            User_Service.UpdateOnDataBase(rootDirectory + DataBaseDirectory, User_Service.iden, null, null, 4);
            USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
            u.delete();
            index();
        }
        else if(do_update)
        {
            if (!Objects.equals(new_password, null))
            {
                USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
                u.Password = new_password;
                User_Service = u;

                User_Service.save();
                User_Service.UpdateOnDataBase(rootDirectory + DataBaseDirectory, User_Service.iden, User_Service.Password, null, 3);

                Home(false,"", "", "");
            }
        }
        else if(do_logout)
        {
            index();
        }
        else if(do_create_space)
        {
            String[] data_div = space_date.split("-");
            String date_mod = data_div[2] + "/" + data_div[1] + "/" + data_div[0];

            String[] parts = path_picture.split("\\\\");
            String space_path = "/public/images/" + parts[2];

            boolean TV = false;
            if(Objects.equals(space_tv, "yes")){
                TV = true;
            }

            List<SPACEMEETING> spaces = SPACEMEETING.findAll();
            int id = spaces.size() + 1;

            SPACEMEETING new_space = new SPACEMEETING(id, Integer.parseInt(space_people), date_mod, space_loc, Integer.parseInt(space_start), Integer.parseInt(space_end), "null", TV, space_path, User_Service);
            new_space.save();

            String space_info = id + "~" + space_people + "~" + date_mod + "~" + space_loc + "~" + space_start + "~" + space_end + "~" + "null" + "~" + TV + "~" + space_path + "~" + User_Service.Name + "," + User_Service.Password;
            new_space.UpdateOnDataBase(rootDirectory + DataBaseDirectory, id, null, space_info, 0);

            USER user = USER.getUser(User_Service.Name + "," + User_Service.Password);
            user.spaces.add(new_space);
            user.numberTimesTenant += 1;

            User_Service = user;
            User_Service.save();
            User_Service.UpdateOnDataBase(rootDirectory + DataBaseDirectory, User_Service.iden, String.valueOf(User_Service.numberTimesTenant), null, 2);

            Home(false,"", "", "");
        }
        else
        {
            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);

            List<MEETING> meetings_rented = MEETING.find("byUser_Reserve_Iden", User_Service.iden).fetch();
            List<SPACEMEETING> spaces_rented = new ArrayList<>();
            List<String> rented_schedules = new ArrayList<>();
            for(MEETING met : meetings_rented)
            {
                SPACEMEETING sp = SPACEMEETING.find("byIden", met.Space_Reserve.iden).first();
                spaces_rented.add(sp);
                rented_schedules.add(met.schedule);
            }

            List<SPACEMEETING> spaces_tenant = SPACEMEETING.find("byUser_Renter_Iden", User_Service.iden).fetch();

            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);
            renderArgs.put("spaces_rented", spaces_rented);         //contains only the spaces rented by this user
            renderArgs.put("rented_schedules", rented_schedules);         //contains only the schedules of every meeting
            renderArgs.put("spaces_tenant", spaces_tenant);         //contains only the spaces "tenant" by this user
            renderTemplate("Application/profile.html");     //equivalent to ProfileHTML
        }
    }
}