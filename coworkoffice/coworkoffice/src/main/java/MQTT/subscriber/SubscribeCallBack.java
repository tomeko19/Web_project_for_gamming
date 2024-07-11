package MQTT.subscriber;

import MQTT.publisher.MQTTPublisher;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import tasks.Misura;
import tasksDao.GestioneMisura;
import tasksDao.GestioneSensore;

import java.time.LocalDateTime;

public class SubscribeCallBack implements MqttCallback {

    private MQTTPublisher publisher;

    GestioneMisura measureDao = new GestioneMisura();
    GestioneSensore sensorDao = new GestioneSensore();

    public SubscribeCallBack(MQTTPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void connectionLost(Throwable cause) {
        // what happens when the connection is lost. We could reconnect here, for example.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        manageMessage(topic, message);
    }

    private void manageMessage(String topic, MqttMessage message) throws Exception{
        System.out.println("Message arrived for the topic '" + topic + "': " + message.toString());

        int first = topic.indexOf('/', 1); // /locali/ <-
        int second = topic.indexOf('/', first + 1); // /locali/localId/ <-
        int third = topic.indexOf('/', second + 1); // /locali/localId/sensori/ <-

        String type = topic.substring(third + 1); // /locali/localId/sensori/ ??? <-

        String measurement = message.toString();
        String now = LocalDateTime.now().toString();
        int localId= getIdLocale(topic);
        int sensorId = sensorDao.getSensorOfLocal(localId, type).getId();
        Misura measure = new Misura(type, measurement, now, sensorId, localId);

        measureDao.addMeasure(measure);
        publisher.publishMeasurement(measure);
    }

    private int getIdLocale(String topic){
        int i = topic.indexOf("/", 1);
        int idLocale = Integer.valueOf(topic.substring(i+1, i+2));
        return idLocale;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // called when delivery for a message has been completed, and all acknowledgments have been received
        // no-op, here
    }
}