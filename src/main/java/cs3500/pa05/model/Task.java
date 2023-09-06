package cs3500.pa05.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.json.PlannedItemJson;
import cs3500.pa05.model.json.TaskJson;
import java.util.ArrayList;
import java.util.List;

/**
 * class Task
 */
public class Task extends PlannedItem {

  private boolean complete;

  /**
   * Constructs a new Task with a specified name.
   * The completion status is initialized as false.
   *
   * @param name The name of the task.
   */
  public Task(String name) {
    super(name);
    this.complete = false;
  }

  /**
   * Converts this Task to a TaskJson object.
   *
   * @return A new TaskJson object representing this Task.
   */
  public PlannedItemJson toJson() {
    return new PlannedItemJson("task",
        new ObjectMapper().convertValue
            (new TaskJson(name, description.toString(), category, complete), JsonNode.class));
  }

  @Override
  public int taskCount() {
    return 1;
  }
  @Override
  public boolean add(Day day) {
    return day.ableToAddTask();
  }
  public void setComplete(boolean complete) {
    this.complete = complete;
  }
  public boolean getComplete() {
    return this.complete;
  }
}
