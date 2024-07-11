package RESTService;

import com.google.gson.Gson;
import jwtToken.Utils;
import taskModelsJSGrid.SensoreJs;
import tasksDao.GestioneSensore;
import java.util.List;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTSensore {


    public static void REST(Gson gson, String baseURL){

        GestioneSensore sensorDao = new GestioneSensore();

        // get all the tasks
        get(baseURL + "/sensors", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<SensoreJs> allSensors = sensorDao.getAllSensors(request.queryMap());

            return allSensors;
        }, gson::toJson);
    }
}


