package tasksDao;

import spark.QueryParamsMap;
import tasks.Locale;
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
public class GestioneLocale {

    public List<Locale> getAllLocals(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, descrizione, tipo, num_posti FROM locali";

        List<Locale> locals = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String type = rs.getString("tipo").substring(0, 1).toUpperCase() + rs.getString("tipo").substring(1) + '.';
                if(type.equals("Salaattesa.")) type = "Sala d'attesa.";
                Locale t = new Locale(rs.getInt("id"), rs.getString("descrizione"), type, rs.getInt("num_posti"));
                locals.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return where(queryParamsMap, locals);
    }
    /**
     * Get all offices from the DB
     * @return a list of office, or an empty list if no offices are available
     * @param queryParamsMap
     */
    public List<Locale> getAllOffices(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, descrizione, tipo, num_posti FROM locali WHERE tipo = 'ufficio'";

        List<Locale> offices = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Locale t = new Locale(rs.getInt("id"), rs.getString("descrizione"), rs.getString("tipo"), rs.getInt("num_posti"));
                offices.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return where(queryParamsMap, offices);
    }

    private List<Locale> where(QueryParamsMap queryParamsMap, List<Locale> locals){
        String description = "";
        String type = "";
        String numSeats = "";
        if(queryParamsMap.hasKey("description")) description = queryParamsMap.get("description").value().toLowerCase();
        if(queryParamsMap.hasKey("type")) type = queryParamsMap.get("type").value().toLowerCase();
        if(queryParamsMap.hasKey("numSeats")) numSeats = queryParamsMap.get("numSeats").value().toLowerCase();

        for(int i = 0; i<locals.size(); i++) {
            if (!locals.get(i).getDescription().toLowerCase().contains(description) ||
                    !locals.get(i).getType().toLowerCase().contains(type) ||
                    !String.valueOf(locals.get(i).getNumSeats()).contains(numSeats)) {

                locals.remove(i);
                i--;
            }
        }

        return locals;
    }

    public Locale getLocal(int id) {
        final String sql = "SELECT id, descrizione, tipo, num_posti FROM locali WHERE id = ?";

        Locale local = null;

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                local = new Locale(rs.getInt("id"), rs.getString("descrizione"), rs.getString("tipo"), rs.getInt("num_posti"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return local;
    }
}
