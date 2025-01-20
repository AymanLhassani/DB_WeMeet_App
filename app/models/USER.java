package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class USER extends Model {
    @Id
    public int iden;

    public String Name;
    public String Password;
    public int numberTimesRent; //numero de veces que alquila un espacio de alguien
    public int numberTimesTenant; //numero de veces que alquila su espacio
    public boolean admin;

    @OneToMany(mappedBy = "User_Renter")
    public List<SPACEMEETING> spaces = new ArrayList<>();

    @OneToMany(mappedBy = "User_Reserve")
    public List<MEETING> meetings  = new ArrayList<>();

    public USER() {}

    public USER(int iden, String name, String password, int numberRent, int numberR, boolean admin) {
        this.iden = iden;
        this.Name = name;
        this.Password = password;
        this.numberTimesRent = numberRent;
        this.numberTimesTenant = numberR;
        this.admin = admin;
    }

    public static USER getUser(String name_password){
        String[] params = name_password.split(",");
        USER u = USER.find("byNameAndPassword", params[0], params[1]).first();
        return u;
    }

    public static void UpdateOnDataBase(String Directory, int identifier, String newParameter, String newUser, int mod){

        try (BufferedReader reader = new BufferedReader(new FileReader(Directory))) {
            StringBuilder Text = new StringBuilder();
            String text = "";
            String space_info = null;
            String line = reader.readLine();
            while (!Objects.equals(line, "USER")) { //busquem on updatejar l' space
                Text.append(line).append("\n");
                text += line + "\n";
                line = reader.readLine();
            }
            text += line + "\n";
            line = reader.readLine();

            if(mod == 0){   //new user
                while(!Objects.equals(line,"*")){

                    String[] params = line.split("~");
                    if(Integer.parseInt(params[0]) == identifier - 1){  //si és aquest user, l'afegim
                        text += line + "\n";
                        text += newUser + "\n";
                        Text.append(newUser).append("\n");
                    }
                    else {
                        text += line + "\n";
                        Text.append(line).append("\n");
                    }
                    line = reader.readLine();
                }
            }
            else if((mod == 1 || (mod == 2) || (mod == 3))){  //update info
                while(!Objects.equals(line,"*")){

                    String[] params = line.split("~");
                    if(Integer.parseInt(params[0]) == identifier){  //si es aquest space, updategem la schedule
                        if(mod == 1){
                            params[3] = newParameter;
                        }
                        else if(mod == 2){
                            params[4] = newParameter;
                        }
                        else {
                            params[2] = newParameter;
                        }
                        space_info = params[0] + "~" + params[1] + "~" + params[2] + "~" + params[3] + "~" + params[4] + "~" + params[5];
                        text += space_info + "\n";
                        Text.append(space_info).append("\n");
                    }
                    else {
                        text += line + "\n";
                        Text.append(line).append("\n");
                    }
                    line = reader.readLine();
                }
            }
            else{   //delete
                while(!Objects.equals(line,"*")){
                    String[] params = line.split("~");
                    if(Integer.parseInt(params[0]) != identifier){  //si es aquest user, no l'escribim
                        text += line + "\n";
                        Text.append(line).append("\n");
                    }
                    line = reader.readLine();
                }
            }

            while(line != null){
                Text.append(line).append("\n");
                text += line + "\n";
                line = reader.readLine();
            }

            System.out.println(Text);

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(Directory))){
                writer.write(text);
            }catch (IOException e) {
                System.out.println("Error writing to the file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }

    }
}