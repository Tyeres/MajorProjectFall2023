import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Test {

    public static void main(String[] args) {

        ArrayList<String> test = new ArrayList<>();
        for (int i = 'Z'; i >= 'A'; i--) {
            char c = (char) i;
            test.add(c + "");
        }
        QuickSort.quickSort(test);
        System.out.println(test);
        
        
        
        Contact testss = new Contact("Noelle Snyder", "21 Ironmaster Dr",
                new GregorianCalendar(2003, 4, 18),"aidan@gmail.com", 2405758304L, "");

        System.out.println(testss.getBirthDay().getTime());
        testss.setNotes("Hello!!!");
        testss.save();
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new BufferedInputStream( new FileInputStream("./src/ContactSaves/Noelle Snyder.dat"))))
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
