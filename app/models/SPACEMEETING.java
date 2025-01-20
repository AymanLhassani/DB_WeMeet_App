package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SPACEMEETING extends Model {
    @Id
    public int iden;

    public int numberPeople;
    public String location;
    public String date;
    public int schedule_start;
    public int schedule_end;
    public String schedule;
    public Boolean tv;
    public String imagepath;

    @OneToMany (mappedBy = "Space_Reserve")
    public  List<MEETING> Listmeetings = new ArrayList<>();

    @ManyToOne
    public USER User_Renter;

    public SPACEMEETING(){}

    public SPACEMEETING(int iden, int numberPeople, String date, String location, int schedule_start, int schedule_end, String schedule, Boolean tv, String imagepath, USER user_Renter) {

        this.iden = iden;
        this.numberPeople = numberPeople;
        this.date = date;
        this.location = location;
        this.schedule_start = schedule_start;
        this.schedule_end = schedule_end;
        this.schedule = schedule;
        this.tv = tv;
        this.imagepath = imagepath;
        this.User_Renter = user_Renter;
    }

    public static SPACEMEETING getSpace(int identifier){
        SPACEMEETING space = SPACEMEETING.find("byIden", identifier).first();
        return space;
    }

    public static void UpdateOnDataBase(String Directory, int identifier, String newSchedule, String newSpace, int mod){

        try (BufferedReader reader = new BufferedReader(new FileReader(Directory))) {
            StringBuilder Text = new StringBuilder();
            String text = "";
            String space_info = null;
            String line = reader.readLine();
            while (!Objects.equals(line, "SPACEMEETING")) { //busquem on updatejar l' space
                Text.append(line).append("\n");
                text += line + "\n";
                line = reader.readLine();
            }
            text += line + "\n";
            line = reader.readLine();

            if(mod == 0){   //new space
                while(!Objects.equals(line,"*")){
                    String[] params = line.split("~");
                    if(Integer.parseInt(params[0]) == identifier - 1){  //si es aquest space, updategem la schedule
                        text += line + "\n";
                        text += newSpace + "\n";
                        Text.append(newSpace).append("\n");
                    }
                    else {
                        text += line + "\n";
                        Text.append(line).append("\n");
                    }
                    line = reader.readLine();
                }
            }
            else if(mod == 1){  //update space
                while(!Objects.equals(line,"*")){

                    String[] params = line.split("~");
                    if(Integer.parseInt(params[0]) == identifier){  //si es aquest space, updategem la schedule
                        if(Objects.equals(params[6], "null")){
                            params[6] = null;
                        }
                        params[6] = newSchedule;
                        System.out.println(" " + newSchedule + " " + params[6] + "o");
                        space_info = params[0] + "~" + params[1] + "~" + params[2] + "~" + params[3] + "~" + params[4] + "~" + params[5] + "~" + params[6] + "~" + params[7] + "~" + params[8] + "~" + params[9];
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
            else{   //delete space
                while(!Objects.equals(line,"*")){
                    String[] parts = line.split("~");
                    if(Integer.parseInt(parts[0]) != identifier){  //si es aquest space, no l'escribim
                        text += line + "\n";
                        Text.append(line).append("\n");

                    }
                    /*
                    else{
                        line = reader.readLine();
                        while(!Objects.equals(line,"*")){
                            String[] params = line.split("~");
                            params[0] = String.valueOf(Integer.parseInt(params[0]) - 1);
                            space_info = params[0] + "~" + params[1] + "~" + params[2] + "~" + params[3] + "~" + params[4] + "~" + params[5] + "~" + params[6] + "~" + params[7] + "~" + params[8] + "~" + params[9];
                            text += space_info + "\n";
                            line = reader.readLine();
                        }
                    }
                    */
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