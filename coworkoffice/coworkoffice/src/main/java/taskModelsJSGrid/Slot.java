package taskModelsJSGrid;

import java.sql.Date;

public class Slot {

    private String schedule;
    private String date;
    private boolean free;

    public Slot(String schedule, String date, boolean free){
        this.schedule = schedule;
        this.date = date;
        this.free = free;
    }
}
