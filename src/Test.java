import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;

public class Test {

    public static void main(String[] args) {



        Contact testss = new Contact("Dora Flan", "21 Ironmaster Dr",
                new GregorianCalendar(2006, 9, 1),"Dora@gmail.com", 2405758304L, "");

        System.out.println(testss.getBirthDay().getTime());
        testss.setNotes("Hello!!!");
        testss.save();
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new BufferedInputStream( new FileInputStream("./src/ContactSaves/Dora Flan.dat"))))
        {
            Contact d = (Contact) inputStream.readObject();
            System.out.println(d.getName());
            System.out.println(d.getBirthdayFormat());
            System.out.println(d.getEmail());
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        catch (Exception ee) {
            System.out.println("Strange error.");
        }
    }

}
