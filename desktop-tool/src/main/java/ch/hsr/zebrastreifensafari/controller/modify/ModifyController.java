package ch.hsr.zebrastreifensafari.controller.modify;

import ch.hsr.zebrastreifensafari.exception.InvalidTimeException;
import ch.hsr.zebrastreifensafari.jpa.entities.User;
import ch.hsr.zebrastreifensafari.model.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public abstract class ModifyController {

    protected final Model model;

    protected ModifyController(Model model) {
        this.model = model;
    }

    public Vector<String> getUsernames() {
        return model.getUsers().stream().map(User::getName).collect(Collectors.toCollection(Vector::new));
    }

    public Date getCreationTime(Date creationDate, String creationTime) throws InvalidTimeException {
        if (creationDate == null) {
            return null;
        }

        if (creationTime.isEmpty()) {
            return creationDate;
        }

        int[] time = splitTime(creationTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(creationDate.getTime() + TimeUnit.HOURS.toMillis(time[0]) + TimeUnit.MINUTES.toMillis(time[1]));
        return calendar.getTime();
    }

    private int[] splitTime(String creationTime) throws InvalidTimeException {
        int[] splitTime = new int[2];
        String[] creationTimeParts = creationTime.split(":", 2);

        for (int i = 0; i < creationTimeParts.length; i++) {
            splitTime[i] = Integer.parseInt(creationTimeParts[i]);
        }

        validateTime(splitTime);

        return splitTime;
    }

    private void validateTime(int[] splitTime) throws InvalidTimeException {
        if (splitTime[0] > 23 || splitTime[0] < 0 || splitTime[1] > 59 || splitTime[1] < 0) {
            throw new InvalidTimeException();
        }
    }
}
