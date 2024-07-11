package tasksDao;

import spark.QueryParamsMap;
import tasks.Attuatore;
import tasks.Misura;
import tasks.Prenotazione;
import tasks.Sensore;
import utils.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * The DAO for the {@code Task} class.
 */
public class GestioneMisura {

    /**
     * Get all measures from the DB
     * @return a list of Measure, or an empty list if no measures are available
     * @param queryParamsMap
     */
    public List<Misura> getAllMeasures(QueryParamsMap queryParamsMap) {
        final String sql = "SELECT id, tipo, misurazione, data, sensore_id, locale_id FROM misure";

        List<Misura> measures = new LinkedList<>();

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String type = rs.getString("tipo").substring(0, 1).toUpperCase() + rs.getString("tipo").substring(1) + '.';
                String measurement = rs.getString("misurazione");
                if(type.equals("Temperatura."))
                    measurement += "°C";
                else {
                    if(type.equals("Umidita."))
                        type = "Umidità.";
                    else if(type.equals("Luminosita."))
                        type = "Luminosità.";
                    measurement += "%";
                }

                Misura t = new Misura(rs.getInt("id"), type, measurement, rs.getString("data"), rs.getInt("sensore_id"), rs.getInt("locale_id"));
                measures.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return where(queryParamsMap, measures);
    }

    private List<Misura> where(QueryParamsMap queryParamsMap, List<Misura> measures){
        String type = "";
        String measurement = "";
        String dateTime = "";
        String localId = "";
        if(queryParamsMap.hasKey("type")) type = queryParamsMap.get("type").value().toLowerCase();
        if(queryParamsMap.hasKey("measurement")) measurement = queryParamsMap.get("measurement").value().toLowerCase();
        if(queryParamsMap.hasKey("dateTime")) dateTime = queryParamsMap.get("dateTime").value().toLowerCase();
        if(queryParamsMap.hasKey("localId")) localId = queryParamsMap.get("localId").value().toLowerCase();

        for(int i = 0; i<measures.size(); i++) {
            if (!measures.get(i).getType().toLowerCase().contains(type) ||
                    !measures.get(i).getMeasurement().toLowerCase().contains(measurement) ||
                    !measures.get(i).getDateTime().toLowerCase().contains(dateTime) ||
                    !String.valueOf(measures.get(i).getLocalId()).contains(localId)) {
                measures.remove(i);
                i--;
            }
        }

        return measures;
    }

    /**
     * Add a new task into the DB
     * @param newMeasure the Measure to be added
     */
    public void addMeasure(Misura newMeasure) {
        final String sql = "INSERT INTO misure(tipo, misurazione, data, sensore_id, locale_id) VALUES (?,?,?,?,?)";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newMeasure.getType());
            st.setString(2, newMeasure.getMeasurement());
            st.setString(3, newMeasure.getDateTime());
            st.setInt(4, newMeasure.getSensorId());
            st.setInt(5, newMeasure.getLocalId());

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeasure(int id) {
        final String sql = "DELETE FROM misure WHERE id = ?";

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            st.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Misura getLastMeasureOfSensor(Sensore sensor){
        String sql = "";
        if(sensor.getType().equals("temperatura,umidita"))
            sql = "SELECT id, tipo, misurazione, data, sensore_id, locale_id FROM misure WHERE sensore_id = ? AND tipo = ? ORDER BY id DESC LIMIT 1";
        else
            sql = "SELECT id, tipo, misurazione, data, sensore_id, locale_id FROM misure WHERE sensore_id = ? ORDER BY id DESC LIMIT 1";

        Misura measure = null;

        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, sensor.getId());
            if(sensor.getType().equals("temperatura,umidita")) st.setString(2, "temperatura");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                measure= new Misura(rs.getInt("id"), rs.getString("tipo"), rs.getString("misurazione"), rs.getString("data"), sensor.getId(), rs.getInt("locale_id"));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return measure;
    }

    public void truncateTable(){
        final String sql = "TRUNCATE TABLE misure";
        try {
            Connection conn = DBConnect.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
