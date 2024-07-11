package tasks;

/**
 * Describe a Task with its properties.
 */
public class Attuatore {

    // the unique id of the office
    private int id;

    private String description;

    private String type;

    private String state;

    private String manual;

    private int localId;


    /**
     * Office main constructor.
     *
     * @param id represents the office unique identifier
     */
    public Attuatore(int id, String description, String type, String state, String manual, int localId) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.state = state;
        this.manual = manual;
        this.localId = localId;
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
    public String getState() {
        return state;
    }
    public String getManual() { return manual; }
    public int getLocalId() {
        return localId;
    }
}
