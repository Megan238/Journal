package cs3500.pa05.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.PlannedItemJson;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * class Event
 */
public class Event extends PlannedItem {

  private LocalTime startTime;


  /**
   * Constructs an instance of an Event with a name, start time, and duration.
   *
   * @param name The name of the event.
   * @param duration The duration of the event.
   * @param startTime The start time of the event.
   */
  public Event(String name, int duration, LocalTime startTime) {
    super(name);
    this.duration = duration;
    this.startTime = startTime;
  }

  /**
   * Returns the start time of the event.
   *
   * @return The start time of the event.
   */
  public LocalTime getStartTime() {
    return startTime;
  }

  /**
   * Returns the duration of the event.
   *
   * @return The duration of the event.
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Returns a JSON representation of the Event object.
   *
   * @return A EventJson object representing the Event.
   */
  public PlannedItemJson toJson() {
    return new PlannedItemJson("event",
        new ObjectMapper().convertValue(new EventJson(this.name, this.description.toString(), this.category,
        this.startTime.toString(),
        this.duration), JsonNode.class));
  }

  @Override
  public int eventCount() {
    return 1;
  }
  @Override
  public boolean add(Day day) {
   return day.ableToAddTask();
  }
  public void setDuration(int duration) {
    this.duration = duration;
  }
  public void setStartTime(LocalTime time) {
    this.startTime = time;
  }
}
