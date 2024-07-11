package RESTService;

import com.google.gson.Gson;

import jwtToken.Utils;
import tasks.Misura;
import tasksDao.GestioneMisura;

import java.util.*;

import static spark.Spark.*;
import static spark.Spark.halt;

    public class RESTMisura {


        public static void REST(Gson gson, String baseURL){

            GestioneMisura measureDao = new GestioneMisura();


            // get all the tasks
            get(baseURL + "/measures", (request, response) -> {
                if(!Utils.getRole().equals("admin")) halt(401);
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                // get all tasks from the DB
                List<Misura> allMeasures = measureDao.getAllMeasures(request.queryMap());

                return allMeasures;
            }, gson::toJson);

            delete(baseURL + "/measures/:id", "application/json", (request, response) -> {
                if(!Utils.getRole().equals("admin")) halt(401);

                if(request.params(":id")!=null) {
                    // add the task into the DB
                    measureDao.deleteMeasure(Integer.parseInt(String.valueOf(request.params(":id"))));
                    response.status(201);
                }
                else {
                    halt(400);
                }
                return "";
            }, gson::toJson);
        }
    }

