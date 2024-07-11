package tasks;

/**
 * Describe a Task with its properties.
 */
public class Sensore {

    // the unique id of the sensor
    private int id;

    private String description;

    private String type;

    private int localId;


    /**
     * sensor main constructor.
     *
     * @param id represents the sensor unique identifier
     * @param description the sensor description
     */
    public Sensore(int id, String description, String type, int localId) {
        this.id = id;
        this.description = description;
        this.type = type;
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
    public int getLocalId() {
        return localId;
    }

}
