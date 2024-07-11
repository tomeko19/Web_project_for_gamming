package tasksDao;

import tasks.Utente;
import utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The DAO for the {@code Task} class.
 */
public class GestioneUtente {

    /**
     * Get all Users from the DB
     * @return a list of User, or an empty list if no Users are available
     */
    public List<Utente> getAllUsers() {
        final String sql = "SELECT id, username, email, nome, cognome, ruolo FROM utenti";

        List<Utente> users = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Utente t = new Utente(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("nome"), rs.getString("cognome"), rs.getString("ruolo"));
                users.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}