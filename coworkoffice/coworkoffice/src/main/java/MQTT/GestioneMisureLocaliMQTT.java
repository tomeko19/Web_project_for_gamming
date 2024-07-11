package MQTT;

import MQTT.publisher.MQTTPublisher;
import MQTT.subscriber.MQTTSubscriber;
import MQTT.subscriber.SubscribeCallBack;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import tasks.Misura;


public class GestioneMisureLocaliMQTT {

    // init the client
    private MQTTPublisher publisher;
    private MQTTSubscriber subscriber;

    public GestioneMisureLocaliMQTT() {
        publisher = new MQTTPublisher();
        subscriber = new MQTTSubscriber(publisher);
    }

    /**
     * The method to start the subscriber. It listen to all the homestation-related topics.
     */
    public void MQTTInit() {
        subscriber.start();
        publisher.start();
    }
}