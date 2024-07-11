package RESTService;

import com.google.gson.Gson;
import jwtToken.Utils;
import tasks.Locale;
import tasksDao.GestioneLocale;

import java.util.List;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTLocale {

    public static void REST(Gson gson, String baseURL) {

        GestioneLocale localDao = new GestioneLocale();

        // get all the tasks
        get(baseURL + "/locals", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Locale> allLocals = localDao.getAllLocals(request.queryMap());

            return allLocals;
        }, gson::toJson);

        // get all the tasks
        get(baseURL + "/offices", (request, response) -> {
            if(!Utils.getRole().equals("user") && !Utils.getRole().equals("admin")) halt(401);
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Locale> allOffices = localDao.getAllOffices(request.queryMap());

            return allOffices;
        }, gson::toJson);
    }
}
