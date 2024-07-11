package taskModelsJSGrid;

public class SensoreJs {

    private int id;

    private String description;

    private String type;

    private String lastMeasure;

    private int localId;

    public SensoreJs(int id, String description, String type, String lastMeasure, int localId) {
        this.id = id;
        this.description = description;
        this.type = type;
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
    public String getLastMeasure() { return lastMeasure; }
    public int getLocalId() {
        return localId;
    }
}
