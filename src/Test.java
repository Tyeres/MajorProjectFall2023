import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {

        ArrayList<String> test = new ArrayList<>();
        for (int i = 'Z'; i >= 'A'; i--) {
            char c = (char) i;
            test.add(c + "");
        }
        QuickSort.quickSort(test);
        System.out.println(test);
        
        
        
//        Contact test = new Contact("Aidan Snyder", "21 Ironmaster Dr",
//                new GregorianCalendar(2003, 3, 18),"aidan@gmail.com", 2405758304L, "");
//
//        System.out.println(test.getBirthDay().getTime());
//        test.setNotes("Hello");
//        test.save();
//        try (ObjectInputStream inputStream = new ObjectInputStream(
//                new BufferedInputStream( new FileInputStream("./src/ContactSaves/Aidan Snyder.dat"))))
//        {
//            Contact d = (Contact) inputStream.readObject();
//            System.out.println(d.getName());
//            System.out.println(d.getBirthDayFormat());
//            System.out.println(d.getEmail());
//        }
//        catch (FileNotFoundException e) {
//            System.out.println("File not found.");
//        }
//        catch (Exception ee) {
//            System.out.println("Strange error.");
//        }
    }

}
