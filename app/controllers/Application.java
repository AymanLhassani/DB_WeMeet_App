package controllers;

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

    public static void startDB()
    {
        USER ob1 = new USER("ayman", "1234", 3, 1);
        ob1.save();
        SPACEMEETING ob3 = new SPACEMEETING(4, "Monday-9h:21h","Castelldefels",  true);
        ob3.save();
        MEETING ob2 = new MEETING(  3, "4/10/24-12h00");
        ob2.save();
    }

    public static void Register(String n, String p)
    {
        USER u = USER.find("byNameAndPassword", n, p).first();

        if (u == null)
        {
            USER user = new USER(n, p, 0, 0).save();
            renderArgs.put("ConnectedUser", user);
            HomeHTML();
        }
        else
        {
            RegisterHTML();
        }
    }

    public static void Login(String n, String p)
    {
        USER u = USER.find("byNameAndPassword", n, p).first();

        if (u == null)
        {
            String er = "true";
            renderArgs.put("error", er);
            LoginHTML();
        }
        else
        {
            renderArgs.put("ConnectedUser", u);
            HomeHTML();
        }
    }

    public static void Home()
    {

    }

    public static  void InitLog()
    {
        String er = "false";
        renderArgs.put("error", er);
        LoginHTML();
    }
    public static  void InitReg()
    {
        RegisterHTML();
    }

    public static void listSpaces(){
        List<SPACEMEETING> spaceses = SPACEMEETING.findAll();
        render("Application/listSpace.html", spaceses);

    }

}