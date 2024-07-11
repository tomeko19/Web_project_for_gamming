package tasks;


import java.sql.Date;
import java.sql.Time;


/**
 * Describe a Task with its properties.
 */
public class Utente {

    private String id;

    private String username;

    private String email;

    private String name;

    private String surname;

    private String role;


    /**
     * Waiting room main constructor.
     *
     */
    public Utente(String id, String username, String email, String name, String surname, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }


    /*** Getters **/

    public String getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }


}