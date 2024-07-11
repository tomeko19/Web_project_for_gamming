package tasks;

/**
 * Describe a Task with its properties.
 */
public class Locale {

    // the unique id of the office
    private int id;

    private String description;

    private String type;

    private int numSeats;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     * @param description the office description
     */
    public Locale(int id, String description, String type, int numSeats) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.numSeats = numSeats;
    }


    /**
     * Overloaded constructor. It create an office without a given id.
     *
     * @param description the task content
     */
    public Locale(String description, String type, int numSeats) {
        this.id = 0;
        this.description = description;
        this.type = type;
        this.numSeats = numSeats;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }
    public int getNumSeats() { return numSeats; }

}
