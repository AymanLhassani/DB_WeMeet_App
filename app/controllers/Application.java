package controllers;

import org.h2.engine.User;
import play.mvc.*;

import models.*;

import java.util.List;

public class Application extends Controller
{

    public static void index()
    {
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
    public static int cont_login = 0;
    public static int cont_register = 0;

    public static void Register(String n, String p)
    {
        USER u = USER.find("byName", n).first();

        if ((u == null) && (n != null) && (p != null))
        {
            USER user = new USER(n, p, 0, 0).save();
            User_Service = user;
            Home();
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
            User_Service = u;
            Home();
        }
    }

    public static void Home()
    {
        renderArgs.put("ConnectedUser", User_Service);
        renderTemplate("Application/home.html");    //equivalent to HomeHTML
    }


    public static void listSpaces(){
        List<SPACEMEETING> spaces = SPACEMEETING.findAll();
        render("Application/listspaces.html", spaces);

    }

    public static void Profile(boolean do_delete, boolean do_update, String new_password)
    {
        if(do_delete)
        {
            USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
            u.delete();
            renderTemplate("Application/init.html");    //equivalent to ProfileHTML
        }
        else if(do_update)
        {
            USER u = USER.find("byNameAndPassword", User_Service.Name, User_Service.Password).first();
            u.Password = new_password;
            u.save();
            renderTemplate("Application/init.html");    //equivalent to ProfileHTML
        }
        else
        {
            renderArgs.put("ConnectedUser", User_Service);
            renderTemplate("Application/profile.html");    //equivalent to ProfileHTML
        }
    }

}