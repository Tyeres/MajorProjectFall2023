import java.io.File;

public final class HashTableMap implements ContactDirectory {

    private final int mapSize;


    // Names are not saved in the map. Instead, initials of names are.
    private String[] initialMap;

    // The largest array needed if only using alphabetical letters would be 89370. I went a little higher just in case.
    public HashTableMap() {
        this(100_000);
        buildInitialMap();
    }

    public HashTableMap(int mapSize) {
        this.mapSize = mapSize;
        this.initialMap = new String[mapSize];
    }
    /**
     * This searches to see if the name exists in the map.
     */
    public boolean search (String name) {
        // Convert the name into an initial
        String initials = getInitials(name);
        // Check if it exists at the designated index
        return initialMap[initials.hashCode()] != null;
    }
    /**
     * Add an initial to the map. Return true if added. Return false otherwise. (There's no point setting a duplicate name to an index).
     * The method is compatible with names in the form of FIRSTNAME LASTNAME and FIRSTNAME MIDDLENAME LASTNAME.
     */
    public boolean add(String name) {
        // Convert the name to an initial
        String initials = getInitials(name);

        // If there is no duplicate in the map (it's empty).
        // if !initials.equals(initialMap[initials.hashCode()])
        if ((initialMap[initials.hashCode()] == null)) {
            initialMap[initials.hashCode()] = initials;
            return true;
        }
        // A duplicate initial was found. Nothing happened (nothing needed to happen anyway).
        return false;
    }

    /**
     * Remove an initial from the map. Return true if removed. Return false if nothing was at the index in the first place.
     * The method is compatible with names in the form of FIRSTNAME LASTNAME and FIRSTNAME MIDDLENAME LASTNAME.
     */
    public boolean remove(String name) {
        // Convert the name to an initial
        String initials = getInitials(name);

        // If there is an element at the index
        if (initialMap[initials.hashCode()] != null) {
            initialMap[initials.hashCode()] = null;
            return true;
        }
        // The map did not contain the name in the first place
        return false;
    }

    /**
     * This method was meant to be called every time the ComboBox was updated.
     * However, this was inefficient for its purpose and is now unnecessary.
     */
    @Deprecated
    public void buildInitialMap(javafx.scene.control.ComboBox<String> comboBox) {
        // Clear the array
        initialMap = new String[mapSize];

        // Rebuild the array so that it reflects any new changes.
        for (String name: comboBox.getItems()) {
            // Convert the name to an initial
            String initials = getInitials(name);
            initialMap[initials.hashCode()] = initials;
        }
    }

    /**
     * This method creates the map from the ContactSaves folder when starting up the program
     */
    private void buildInitialMap() {
        File[] files = CONTACT_NAMES_FOLDER.listFiles();
        String[] namesOfFiles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            int lengthOfString = files[i].getName().length();
            // Get the substring of the name because I don't want the ".dat" part of the name.
            namesOfFiles[i] = files[i].getName().substring(0, lengthOfString - 1 - 3);
        }
        for (String str:namesOfFiles) {
            // Convert the name to an initial
            String initials = getInitials(str);
            initialMap[initials.hashCode()] = initials;
        }
    }
    private static String getInitials(String name) {
        // Convert the name to an initial
        char firstInitial;
        Character secondInitial = null; // Some contacts may not have a middle name. If not, middle initial is null.
        char lastInitial;

        firstInitial = name.charAt(0);

        // If middle name exists
        if (name.indexOf(' ') != name.lastIndexOf(' ')) {
            int indexOfMiddleInitial = name.indexOf(' ') + 1; // (Example: Billy Bob Joe)
            secondInitial = name.charAt(indexOfMiddleInitial);
        }
        // This code initializes the lastInitial either if the middle initial exists or not.
        int indexOfLastInitial = name.lastIndexOf(' ') + 1;
        lastInitial = name.charAt(indexOfLastInitial);

        String initials;

        if (secondInitial != null) {
            initials = firstInitial + String.valueOf(secondInitial) + lastInitial;
        } else initials = String.valueOf(firstInitial) + lastInitial;

        // The initials should be set to uppercase if names' are always assumed to be capitalized. Otherwise, do not
        // capitalize the initials. I chose not to set the initials to uppercase.
        return initials;
    }
}
