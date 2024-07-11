package tasks;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

/**
 * Describe a Task with its properties.
 */
public class Misura {

    // the unique id of the office
    private int id;

    private String type;

    private String measurement;

    private String dateTime;

    private int sensorId;

    private int localId;



    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     */
    public Misura(int id, String type, String measurement, String dateTime, int sensorId, int localId) {
        this.id = id;
        this.type = type;
        this.measurement = measurement;
        this.dateTime = dateTime;
        this.sensorId = sensorId;
        this.localId = localId;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     */
    public Misura(String type, String measurement, String dateTime, int sensorId, int localId) {
        this.id = 0;
        this.type = type;
        this.measurement = measurement;
        this.dateTime = dateTime;
        this.sensorId = sensorId;
        this.localId = localId;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getSensorId(){ return sensorId; }

    public int getLocalId(){ return localId; }
}