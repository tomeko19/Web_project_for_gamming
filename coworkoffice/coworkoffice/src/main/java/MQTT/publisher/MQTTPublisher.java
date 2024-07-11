package MQTT.publisher;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import tasks.Attuatore;
import tasks.Locale;
import tasks.Misura;
import tasksDao.GestioneAttuatore;
import tasksDao.GestioneLocale;
import tasksDao.GestionePrenotazione;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Sample publisher for MQTT. It uses the Eclipse Paho library and Mosquitto as a broker.
 * Mosquitto is expected to be installed and launched locally
 * (public test servers are available, however).
 *
 * It connects to the Mosquitto broker, set up a Last Will and Testament for the connection,
 * and publish a sample temperature value (i.e., a string equal to "20 C") on a specific topic.
 *
 * @author <a href="mailto:luigi.derussis@uniupo.it">Luigi De Russis</a>
 * @version 1.1 (21/05/2019)
 */
public class MQTTPublisher {
    GestioneAttuatore actuatorDao = new GestioneAttuatore();
    GestioneLocale localDao = new GestioneLocale();
    GestionePrenotazione reservationDao = new GestionePrenotazione();
    // init the client
    private MqttClient client;
    MemoryPersistence persistence = new MemoryPersistence();
    private final int HOT_TEMPERATURE_THRESHOLD = 20;
    private final int COLD_TEMPERATURE_THRESHOLD = 15;
    private final int BRIGHTNESS_THRESHOLD = 30;
    private final int GAS_THRESHOLD = 30;
    /**
     * Constructor. It generates a client id and instantiate the MQTT client.
     */
    public MQTTPublisher() {

        // the broker URL
        String brokerURL = "tcp://localhost:1883";

        // A randomly generated client identifier based on the user's login
        // name and the system time
        String clientId = MqttClient.generateClientId();

        try {
            // create a new MQTT client
            client = new MqttClient(brokerURL, clientId, persistence);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method to start the publisher. Currently, it sets a Last Will and Testament
     * message, open a non persistent connection, and publish a temperature value
     */
    public void start() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            // persistent, durable connection
            options.setCleanSession(false);
            options.setUserName("esp32");
            options.setPassword(new char[]{'2', '0', '1', '0', '2', '0', '1', '7', 't', 'o', 'm'});

            // connect the publisher to the broker
            client.connect(options);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws MqttException
     */
    public void publishMeasurement(Misura measure) throws MqttException {
        String topicRaw = "/locali/" + measure.getLocalId() + "/attuatori/" + measure.getType();
        Locale local = localDao.getLocal(measure.getLocalId());

        MqttTopic topic = client.getTopic(topicRaw);
        Attuatore actuator = actuatorDao.getActuatorOfLocal(measure.getLocalId(), measure.getType());

        String message = "";

        if(actuator.getManual().equals("manuale")){
            if(actuator.getDescription().equals("Climatizzatore.") && measure.getType().equals("temperatura")){
                try {
                    int needTemperature = Integer.valueOf(actuator.getState());
                    if(needTemperature < Integer.valueOf(measure.getMeasurement())) message = "raffreddamento";
                    else if(needTemperature > Integer.valueOf(measure.getMeasurement())) message = "riscaldamento";
                } catch(Exception e ){
                    message = "off";
                }
            } else if(actuator.getDescription().equals("Lampadina automatica.") || actuator.getDescription().equals("Allarme gas.")){
                if(actuator.getState().equals("off")) message = "off";
                else if(actuator.getState().equals("on")) message = "on";
            } else return; // Non ho bisogno di cambiare stato, esco

        } else { // Automatico

            if (measure.getType().equals("temperatura")) {
                if(local.getType().equals("ufficio") && !reservationDao.isBooked(measure.getLocalId(), Date.valueOf(LocalDate.now()).toString(), LocalDateTime.now().getHour())) {
                    actuatorDao.updateState("off", actuator.getId());
                    message = "off";
                } else if (Integer.valueOf(measure.getMeasurement()) > HOT_TEMPERATURE_THRESHOLD){
                    actuatorDao.updateState("Aria fredda.", actuator.getId());
                    message = "raffreddamento";
                }
                else if (Integer.valueOf(measure.getMeasurement()) < COLD_TEMPERATURE_THRESHOLD) {
                    actuatorDao.updateState("Aria calda.", actuator.getId());
                    message = "riscaldamento";
                }
                else {
                    actuatorDao.updateState("off", actuator.getId());
                    message = "off";
                }
            } else if (measure.getType().equals("luminosita")) {
                if (Integer.valueOf(measure.getMeasurement()) > BRIGHTNESS_THRESHOLD){
                    actuatorDao.updateState("off", actuator.getId());
                    message = "off";
                }
                else{
                    actuatorDao.updateState("on", actuator.getId());
                    message = "on";
                }
            } else if (measure.getType().equals("gas")) {
                if (Integer.valueOf(measure.getMeasurement()) > GAS_THRESHOLD) {
                    actuatorDao.updateState("on", actuator.getId());
                    message = "on";
                }
                else {
                    actuatorDao.updateState("off", actuator.getId());
                    message = "off";
                }
            } else return;
        }
        // publish the message on the given topic
        topic.publish(new MqttMessage(message.getBytes()));

        // debug
        System.out.println("Published message on topic '" + topic.getName() + "': " + message);
    }

}
