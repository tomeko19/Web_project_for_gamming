package RESTService;

import com.google.gson.Gson;
import jwtToken.Utils;
import tasks.Utente;
import tasksDao.GestioneUtente;

import java.util.List;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RESTUtente {


    public static void REST(Gson gson, String baseURL) {

        GestioneUtente UserDao = new GestioneUtente();

        // get all the tasks
        get(baseURL + "/users", (request, response) -> {
            if(!Utils.getRole().equals("admin")) halt(401);
            // set a proper response code and type
            response.type("application/json");
            response.status(200);

            // get all tasks from the DB
            List<Utente> allUsers = UserDao.getAllUsers();

            return allUsers;
        }, gson::toJson);
    }
}



