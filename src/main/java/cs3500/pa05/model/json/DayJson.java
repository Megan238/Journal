package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.model.Weekday;
import java.util.List;

/**
 * This record represents a JSON object for a day's data.
 *
 * @param day The day of the week.
 * @param date The date of the day.
 * @param plannedItems The list of planned items occurring on that day.
 */
public record DayJson(
    @JsonProperty("day") Weekday day,
    @JsonProperty("date") String date,
    @JsonProperty("plannedItems") List<PlannedItemJson> plannedItems) {

}