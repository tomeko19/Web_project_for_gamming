package taskModelsJSGrid;

/**
 * Describe a Task with its properties.
 */
public class AttuatoreJs {

    private int id;

    private String description;

    private String type;

    private String state;

    private String manual;

    private String lastMeasure;

    private int localId;


    /**
     * Office main constructor.
     *
     */
    public AttuatoreJs(int id, String description, String type, String state, String manual, String lastMeasure, int localId) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.state = state;
        this.manual = manual;
        this.lastMeasure = lastMeasure;
        this.localId = localId;
    }

    public int getId() { return id; }
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
    public String getLastMeasure(){ return lastMeasure; };
    public int getLocalId() {
        return localId;
    }
}
