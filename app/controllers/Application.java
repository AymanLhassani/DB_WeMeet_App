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
            user.save();
            connectedUser();
            User_Service = user;
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
            User_Service = null;
            username = "";

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
            SPACEMEETING update_space = all_spaces.get(id - 1);
            update_space.schedule = new_schedule;
            update_space.UpdateOnDataBase(rootDirectory + DataBaseDirectory, update_space.iden, new_schedule);
            System.out.println("ID: " + id);
            update_space.save();

            MEETING new_meeting = new MEETING(number_people, schedule_meeting, update_space, User_Service);
            new_meeting.save();

            Home(false,"", "", "");
        }
        else    //default
        {
            String[] data_div = new String[3];

            data_div = date_search.split("-");

            String date_year = data_div[0];
            String date_month = data_div[1];
            String date_day = data_div[2];

            String date_mod = date_day + "/" + date_month + "/" + date_year;

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

    public static void Profile(boolean do_delete, boolean do_update, boolean do_logout, String new_password)
    {
        if(do_delete)
        {
            USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
            u.delete();
            renderTemplate("Application/init.html");    //equivalent to ProfileHTML
        }
        else if(do_update)
        {
            if (!Objects.equals(new_password, null))
            {
                USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
                u.Password = new_password;
                u.save();
                renderTemplate("Application/init.html");    //equivalent to ProfileHTML
            }
        }
        else if(do_logout){
            User_Service = null;
            username = "";

            index();
        }
        else
        {
            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);

            List<MEETING> meetings_rented = MEETING.find("byUser_Reserve_Iden", User_Service.iden).fetch();
            List<SPACEMEETING> spaces_rented = new ArrayList<>();
            for(MEETING me : meetings_rented)
            {
                SPACEMEETING sp = SPACEMEETING.find("byIden", me.Space_Reserve.iden).first();
                spaces_rented.add(sp);
            }

            List<SPACEMEETING> spaces_tenant = SPACEMEETING.find("byUser_Renter_Iden", User_Service.iden).fetch();

            session.put("username", username);
            renderArgs.put("ConnectedUser", User_Service);
            renderArgs.put("spaces_rented", spaces_rented);         //contains only the spaces rented by this user
            renderArgs.put("spaces_tenant", spaces_tenant);         //contains only the spaces "tenant" by this user
            renderTemplate("Application/profile.html");     //equivalent to ProfileHTML
        }
    }
}