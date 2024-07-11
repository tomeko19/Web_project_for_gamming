package tasks;


import java.sql.Date;


/**
 * Describe a Task with its properties.
 */
public class Prenotazione {

    // the unique id of the office
    private int id;

    private String date;

    private int startHour;

    private int finalHour;

    private int clients;

    private int officeId;

    private String userId;







    /**
     * Waiting room main constructor.
     *
     * @param id represents the reservation unique identifier
     */
    public Prenotazione(int id, String date, int startHour, int finalHour, int clients, int officeId, String userId) {
        this.id = id;
        this.date = date;
        this.startHour = startHour;
        this.finalHour = finalHour;
        this.clients = clients;
        this.officeId = officeId;
        this.userId = userId;
    }


    /**
     * Overloaded constructor. It create a reservation without a given id.
     *
     */
    public Prenotazione(String date, int startHour, int finalHour, int clients, int officeId, String userId) {
        this.id = id;
        this.date = date;
        this.startHour = startHour;
        this.finalHour = finalHour;
        this.clients = clients;
        this.officeId = officeId;
        this.userId = userId;
    }


    /*** Getters **/

    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getFinalHour() {
        return finalHour;
    }

    public int getClients(){
        return clients;
    }

    public int getOfficeId() {
        return officeId;
    }

    public String getUserId(){
        return userId;
    }
}