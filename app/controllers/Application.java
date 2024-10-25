package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }


    public static void startDB() {
        USUARI ob1 = new USUARI("ayman", "1234", 3, 1);
        ob1.save();
        SPACEMEETING ob3 = new SPACEMEETING(4, "Monday-9h::21h","Castelldefels",  true);
        ob3.save();
        MEETING ob2 = new MEETING(  3, "4/10/24-12h00");
        ob2.save();

    }

    public static void RegisterC(String n, String p){
        //los dos parametros restantes sera 0 por defecto, ya que el cliente es nuevo
        USUARI c = USUARI.find("byNameAndPassword", n, p).first();

        if (c == null) {
            new USUARI(n, p, 0, 0).save();
            renderText("Usuario dado de alta");//envia mensaje al cliente
        }else{
            renderText("Usuario existente");
        }
    }

    public static void Login(String n, String p){
        USUARI c = USUARI.find("byPassword", p).first();

        if (c == null) {
            renderText("Contraseña Incorrecta");
        }else{
            renderText("Login correctamente");
        }
    }

    public static void Hole(){

    }

}