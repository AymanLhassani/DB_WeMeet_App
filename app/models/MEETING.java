package models;
import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.*;
import java.util.Objects;

@Entity
public class MEETING extends Model {
    public int numberPeopleMeeting;
    public String schedule;

    @ManyToOne
    public SPACEMEETING Space_Reserve;

    @ManyToOne
    public USER User_Reserve;

    public MEETING() {
    }

    public MEETING(int numberPeopleMeeting, String schedule, SPACEMEETING space_Reserve, USER user_Reserve) {
        this.numberPeopleMeeting = numberPeopleMeeting;
        this.schedule = schedule;
        this.Space_Reserve = space_Reserve;
        this.User_Reserve = user_Reserve;
    }

    public static void UpdateOnDataBase(String Directory, int count, String delete_params, String newMeeting, int mod) {

        try (BufferedReader reader = new BufferedReader(new FileReader(Directory))) {
            StringBuilder Text = new StringBuilder();
            String text = "";
            String line = reader.readLine();
            while (!Objects.equals(line, "MEETING")) {
                Text.append(line).append("\n");
                text += line + "\n";
                line = reader.readLine();
            }
            text += line + "\n";
            line = reader.readLine();

            if (mod == 0) { //new space
                int i = 0;
                while(i < count){
                    text += line + "\n";
                    Text.append(line).append("\n");

                    line = reader.readLine();
                    i++;
                }
                text += newMeeting + "\n";
                text += line + "\n";
                Text.append(line).append("\n");

                line = reader.readLine();
            }
            else if (mod == 1){   //delete space
                while (!Objects.equals(line, "*")) {
                    String[] delete = delete_params.split("~");
                    String[] params = line.split("~");
                    if ((Objects.equals(delete[0], params[0])) && (Objects.equals(delete[1], params[1])) && (Objects.equals(delete[2], params[2])) && (Objects.equals(delete[3], params[3]))) {

                    }
                    else{
                        text += line + "\n";
                        Text.append(newMeeting).append("\n");
                    }
                    line = reader.readLine();
                }
            }

            while (line != null) {
                Text.append(line).append("\n");
                text += line + "\n";
                line = reader.readLine();
            }

            System.out.println(Text);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Directory))) {
                writer.write(text);
            } catch (IOException e) {
                System.out.println("Error writing to the file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
}
