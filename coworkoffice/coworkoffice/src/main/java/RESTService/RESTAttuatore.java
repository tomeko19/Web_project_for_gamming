package RESTService;

import com.google.gson.Gson;
import jwtToken.Utils;
import taskModelsJSGrid.AttuatoreJs;
import tasks.Attuatore;
import tasksDao.GestioneAttuatore;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTAttuatore {


    public static void REST(Gson gson, String baseURL){

        GestioneAttuatore actuatorDao = new GestioneAttuatore();

        // get all the tasks
        get(baseURL + "/actuators", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<AttuatoreJs> allActuators = actuatorDao.getAllActuators(request.queryMap());
            // prepare the JSON-related structure to return

            return allActuators;
        }, gson::toJson);

        put(baseURL + "/actuators/updatemanual/:id", "application/json", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // get the body of the HTTP request
            Map addRequest = gson.fromJson(request.body(), Map.class);
            Attuatore actuator = null;
            // check whether everything is in place
            if(request.params(":id")!=null && addRequest!=null && addRequest.containsKey("manual") && String.valueOf(addRequest.get("manual")).length() != 0) {
                String manual = String.valueOf(addRequest.get("manual"));
                actuator = actuatorDao.updateActuator(manual, Integer.parseInt(String.valueOf(request.params(":id"))));
                // if success, prepare a suitable HTTP response code
                response.status(201);
            }
            else {
                halt(400);
            }

            return actuator;
        }, gson::toJson);

        put(baseURL + "/actuators/updatestate/:id", "application/json", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // get the body of the HTTP request
            Map addRequest = gson.fromJson(request.body(), Map.class);
            Attuatore actuator = null;
            // check whether everything is in place
            if(request.params(":id")!=null && addRequest!=null && addRequest.containsKey("state")) {
                String state = String.valueOf(addRequest.get("state"));
                actuator = actuatorDao.updateState(state, Integer.parseInt(String.valueOf(request.params(":id"))));
                // if success, prepare a suitable HTTP response code
                response.status(201);
            }
            else {
                halt(400);
            }

            return actuator;
        }, gson::toJson);
    }
}


