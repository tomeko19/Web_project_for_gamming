package RESTService;

import com.google.gson.Gson;
import jwtToken.Utils;
import tasks.Prenotazione;
import taskModelsJSGrid.Slot;
import tasksDao.GestionePrenotazione;

import java.util.*;

import static spark.Spark.*;
import static spark.Spark.halt;

import jwtToken.jwtlib.*;

public class RESTPrenotazione {

        public static void REST(Gson gson, String baseURL){
            GestionePrenotazione reservationDao = new GestionePrenotazione();

            // get all the tasks
            get(baseURL + "/reservations", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                String userId = Utils.getUserId();

                List<Prenotazione> allReservations = new LinkedList<>();

                if(Utils.getRole().equals("admin"))
                    allReservations = reservationDao.getAllReservations(request.queryMap());
                else if(Utils.getRole().equals("user"))
                    allReservations = reservationDao.getAllReservationsUser(userId, request.queryMap());
                else
                    halt(401);

                return allReservations;

            }, gson::toJson);

            delete(baseURL + "/reservations/:id", "application/json", (request, response) -> {
                if(Utils.getRole().equals("admin")) {}
                else if(Utils.getRole().equals("user")){
                    if(!reservationDao.getReservation(Integer.valueOf(request.params(":id"))).getUserId().equals(Utils.getUserId()))
                        halt(401);
                } else halt(401);

                if(request.params(":id")!=null) {
                    // add the task into the DB
                    reservationDao.deleteReservation(Integer.valueOf(request.params(":id")));
                    response.status(201);
                }
                else {
                    halt(400);
                }
                return "";
            });

            put(baseURL + "/reservations/updateclients/:id", "application/json", (request, response) -> {
                if(Utils.getRole().equals("admin")) {}
                else if(Utils.getRole().equals("user") || Utils.getRole().equals("admin")){
                    if(!reservationDao.getReservation(Integer.valueOf(request.params(":id"))).getUserId().equals(Utils.getUserId()))
                        halt(401);
                } else halt(401);

                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);
                Prenotazione reservation = null;
                // check whether everything is in place
                if(request.params(":id")!=null && addRequest!=null && addRequest.containsKey("clients") && String.valueOf(addRequest.get("clients")).length() != 0) {
                    int clients = (int) Math.round((Double) addRequest.get("clients"));
                    if(clients > 5 || clients < 1)
                        halt(400);

                    reservation = reservationDao.updateReservation(clients, Integer.parseInt(String.valueOf(request.params(":id"))));
                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(400);
                }

                return reservation;
            }, gson::toJson);


            // get all slots available
            get(baseURL + "/slots/:officeId", (request, response) -> {
                // set a proper response code and type
                response.type("application/json");
                response.status(200);

                List<Slot> slots = reservationDao.getSlots(Integer.valueOf(request.params(":officeId")));

                return slots;
            }, gson::toJson);

            post(baseURL + "/slots/:officeId/:clients", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map addRequest = gson.fromJson(request.body(), Map.class);

                // check whether everything is in place
                if(addRequest!=null && addRequest.containsKey("schedule") && addRequest.containsKey("date")) {
                    String schedule = String.valueOf(addRequest.get("schedule"));
                    String date = String.valueOf(addRequest.get("date"));

                    int i = schedule.indexOf(':');
                    int startHour = Integer.valueOf(schedule.substring(0, i));
                    int finalHour = Integer.valueOf(schedule.substring(i+4, schedule.length()-3));
                    String userId = Utils.getUserId();
                    int clients = Integer.valueOf(request.params(":clients"));
                    if(clients > 5 || clients < 1)
                        halt(400);
                    int officeId = Integer.valueOf(request.params(":officeId"));

                    Prenotazione reservation = new Prenotazione(date, startHour, finalHour, clients, officeId, userId);
                    reservationDao.addReservation(reservation);

                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(400);
                }

                return "";
            }, gson::toJson);


            delete(baseURL + "/slots/:officeId", "application/json", (request, response) -> {
                // get the body of the HTTP request
                Map deleteRequest = gson.fromJson(request.body(), Map.class);

                if(deleteRequest!=null && deleteRequest.containsKey("schedule") && deleteRequest.containsKey("date")) {
                    String schedule = String.valueOf(deleteRequest.get("schedule"));
                    String date = String.valueOf(deleteRequest.get("date"));

                    int officeId = Integer.valueOf(request.params(":officeId"));
                    int i = schedule.indexOf(':');
                    int startHour = Integer.valueOf(schedule.substring(0, i));

                    String userId = Utils.getUserId();
                    reservationDao.deleteReservation(date, startHour, officeId, userId);
                    // if success, prepare a suitable HTTP response code
                    response.status(201);
                }
                else {
                    halt(400);
                }
                return "";
            }, gson::toJson);
        }
    }





