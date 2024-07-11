import MQTT.GestioneMisureLocaliMQTT;
import RESTService.*;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.gson.Gson;
import jwtToken.jwtlib.JWTfun;
import tasksDao.GestioneMisura;

import static jwtToken.verifySignature.verifySignatureTokenJWT;
import static spark.Spark.*;

public class TasksService {
    public static void main(String[] args) {
        // init
        Gson gson = new Gson();
        String baseURL = "/api/v1.0";
        GestioneMisura measureDao = new GestioneMisura();

        // enable CORS
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });


        before((request, response) -> { // prima di qualunque richiesta (POST, GET, ...)
            response.header("Access-Control-Allow-Origin", "*"); // qualunque dominio
            response.header("Access-Control-Allow-Methods", "GET, POST"); // metodi accettati GET e POST
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization");

            //per mustache commentare qua ->

            // Gestione Autorizzazione
            String token = "";
            try {
                token = request.headers("Authorization").substring(7); // Leggo il token dopo la string Bearer
                if(!verifySignatureTokenJWT(token))
                    halt(401);
            } catch(NullPointerException e){
                if(!(request.requestMethod() == "OPTIONS"))
                    halt(401);
            }catch(JWTDecodeException e){
                if(!(request.requestMethod() == "OPTIONS"))
                    halt(401);
            }
        });    //  <-

        RESTLocale.REST(gson, baseURL);
        RESTPrenotazione.REST(gson, baseURL);
        RESTAttuatore.REST(gson, baseURL);
        RESTSensore.REST(gson, baseURL);
        RESTMisura.REST(gson, baseURL);
        RESTUtente.REST(gson, baseURL);

        measureDao.truncateTable();

        GestioneMisureLocaliMQTT gestioneMisureLocaliMQTT = new GestioneMisureLocaliMQTT();
        gestioneMisureLocaliMQTT.MQTTInit();
    }
}
