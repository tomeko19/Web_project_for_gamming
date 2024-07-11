package tasksDao;

import spark.QueryParamsMap;
import taskModelsJSGrid.AttuatoreJs;
import tasks.Attuatore;
import tasks.Locale;
import tasks.Misura;
import tasks.Sensore;
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
public class GestioneAttuatore {

    GestioneSensore sensorDao = new GestioneSensore();
    GestioneMisura measureDao = new GestioneMisura();
    /**
     * Get all Actuators from the DB
     * @return a list of Actuator, or an empty list if no Actuators are available
     * @param queryParamsMap
     */
    public List<AttuatoreJs> getAllActuators(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, descrizione, tipo, stato, manuale, locale_id FROM attuatori";

        List<AttuatoreJs> actuators = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String state = rs.getString("stato");
                if(state.equals("off")) state = "Spento.";
                else if(state.equals("on")) state = "Acceso.";

                try{
                    Integer.valueOf(state); // Se non crea eccezioni, si tratta di una temperatura
                    state = "Impostato su " + state + "°C";
                } catch (NumberFormatException e) {}

                Sensore sensor = sensorDao.getSensorOfLocal(rs.getInt("locale_id"), rs.getString("tipo"));
                Misura measure = measureDao.getLastMeasureOfSensor(sensor);
                String measurement = "";
                try {
                    measurement = measure.getMeasurement();
                    if(measure.getType().equals("temperatura"))
                        measurement += "°C";
                    else measurement += "%";
                } catch(NullPointerException e) {}

                AttuatoreJs t = new AttuatoreJs(rs.getInt("id"), rs.getString("descrizione"), rs.getString("tipo"), state, rs.getString("manuale"), measurement, rs.getInt("locale_id"));
                actuators.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return where(queryParamsMap, actuators);
    }

    private List<AttuatoreJs> where(QueryParamsMap queryParamsMap, List<AttuatoreJs> actuators){
        String description = "";
        String manual = "";
        String localId = "";
        if(queryParamsMap.hasKey("description")) description = queryParamsMap.get("description").value().toLowerCase();
        if(queryParamsMap.hasKey("manual")) manual = queryParamsMap.get("manual").value().toLowerCase();
        if(queryParamsMap.hasKey("localId")) localId = queryParamsMap.get("localId").value().toLowerCase();

        for(int i = 0; i<actuators.size(); i++) {
            if (!actuators.get(i).getDescription().toLowerCase().contains(description) ||
                    !String.valueOf(actuators.get(i).getLocalId()).contains(localId) ||
                    !actuators.get(i).getManual().toLowerCase().contains(manual)) {

                actuators.remove(i);
                i--;
            }
        }

        return actuators;
    }

    public Attuatore getActuator(int id) {
        final String sql = "SELECT id, descrizione, tipo, stato, manuale, locale_id FROM attuatori WHERE id = ?";

        Attuatore actuator = null;

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                String state = rs.getString("stato");
                if(state.equals("off")) state = "Spento.";
                else if(state.equals("on")) state = "Acceso.";
                actuator = new Attuatore(id, rs.getString("descrizione"), rs.getString("tipo"), state, rs.getString("manuale"), rs.getInt("locale_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actuator;
    }

    public Attuatore getActuatorOfLocal(int localId, String type) {
        if(type.equals("temperatura") || type.equals("umidita")) type = "temperatura,umidita";
        final String sql = "SELECT id, descrizione, tipo, stato, manuale, locale_id FROM attuatori WHERE locale_id = ? AND tipo = ?";

        Attuatore actuator = null;

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, localId);
            st.setString(2, type);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                actuator = new Attuatore(rs.getInt("id"), rs.getString("descrizione"), type, rs.getString("stato"), rs.getString("manuale"), localId);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actuator;
    }

    public Attuatore updateActuator(String manual, int id) {
        final String sql = "UPDATE attuatori SET manuale = ?, stato = 'off' WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, manual);
            st.setInt(2, id);

            st.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Attuatore actuator = getActuator(id);

        return actuator;
    }

    public Attuatore updateState(String state, int id) {
        final String sql = "UPDATE attuatori SET stato = ? WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, state);
            st.setInt(2, id);

            st.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Attuatore actuator = getActuator(id);

        return actuator;
    }
}