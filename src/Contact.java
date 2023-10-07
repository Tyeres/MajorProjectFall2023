import java.io.*;
import java.util.GregorianCalendar;

public class Contact implements Serializable {
    private String name;
    private String address;
    private GregorianCalendar birthDay;
    private String email;
    private long phoneNumber;
    private String phoneNumberFormat;
    private String notes;
    public Contact() {

    }
    public Contact(String name, String address, GregorianCalendar birthDay, String email, long phoneNumber, String notes) {
        this.name = name;
        this.address = address;
        this.birthDay = birthDay;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.phoneNumberFormat = getPhoneNumberFormat(phoneNumber);
        this.notes = notes;
    }

    private String getPhoneNumberFormat(long phoneNumber) {
        String phoneNumberStr = String.valueOf(phoneNumber);
        String areaCode = phoneNumberStr.substring(0, 3);
        String telephonePrefix = phoneNumberStr.substring(3, 6);
        String lineNumber = phoneNumberStr.substring(6, 10);
        return "(" + areaCode + ") " + telephonePrefix + "-" + lineNumber;

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public GregorianCalendar getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberFormat() {
        return phoneNumberFormat;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDay(GregorianCalendar birthDay) {
        this.birthDay = birthDay;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumberFormat(String phoneNumberFormat) {
        this.phoneNumberFormat = phoneNumberFormat;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // This returns a String in a regular birthday format.
    public String getBirthDayFormat() {
        String date = String.valueOf(this.birthDay.getTime());
        int index = date.indexOf(':'); // Date format: Fri Jan 10 00:00:00 EDT 2000
        String firstHalfDate = date.substring(4, index - 3);
        String secondHalfDate = date.substring(date.length() - 1 - 3);
        return firstHalfDate + ' ' + secondHalfDate;
    }

    // This method returns a String in Year Month Day format
    public String getBirthdayFileFormat() {
        // This array cannot be static because this is a Serializable class.
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String month = this.getBirthDayFormat().substring(0, 3);

        int monthIndex = 0; // Initiate
        for (int i = 0; i < months.length; i++) {
            if (month.equals(months[i]))
                monthIndex = i;
        }

        String year = String.valueOf(this.birthDay.getWeekYear());
        String day = this.birthDay.getTime().toString().substring(8, 10);
        return year + " " + (monthIndex + 1) + " " + day;
    }

    public void save() {
        String fileDir = "./src/ContactSaves/" + this.name + ".dat"; // I need to validate that this.name can be a file name later

        try (ObjectOutputStream output = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileDir))))
        {
            // Save the current Contact object
            output.writeObject(this);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.err.println("Issue with saving.");
            System.exit(-555);
        }

    }
}
