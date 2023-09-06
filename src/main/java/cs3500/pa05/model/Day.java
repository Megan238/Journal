package cs3500.pa05.model;

import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.PlannedItemJson;
import cs3500.pa05.model.json.TaskJson;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * class Day
 */
public class Day {
  private final Weekday weekday;
  private final List<PlannedItem> items;
  private LocalDate date;
  private int maxEvents;
  private int maxTask;

  /**
   * constructor for Day
   *
   * @param weekday which day of weekday the day is
   */
  public Day(Weekday weekday) {
    this.weekday = weekday;
    this.items = new LinkedList<>();
    this.date = LocalDate.of(1, 1, 1);
  }


  /**
   * set new date for day
   *
   * @param date new date
   */
  public void setDate(LocalDate date) {

    this.date = date;
  }


  /**
   *Returns the date of the day.
   *
   *@return the date of the day
   */
  public LocalDate getDate() {
    return date;
  }


  /**
   * determines whether the existing event less than or equals to max number of event
   *
   * @param max number of item
   * @return existing event max
   */
  public boolean setMaxEvent(int max) {
    if (max >= this.eventCount()) {
      this.maxEvents = max;
      return true;
    } else {
      return false;
    }
  }

  /**
   * determine whether the total number of task reaches the max number
   *
   * @param max max number allowed
   * @return if task number less than or equal to max number
   */
  public boolean setMaxTask(int max) {
    if (max >= this.taskCount()) {
      this.maxTask = max;
      return true;
    } else {
      return false;
    }
  }

//  /**
//   * add event to a day
//   *
//   * @param item event
//   * @param day  which day
//   * @return if there is a space to add new event
//   */
//  public boolean addEvent(Event item, Weekday day) {
//    if (this.maxEvents > this.events.size() && this.weekday == day) {
//      events.add(item);
//      return true;
//    } else {
//      return false;
//    }
//  }
//
//  /**
//   * add task to a day
//   *
//   * @param item task
//   * @param day  which day
//   * @return if there is a space to add new task
//   */
//  public boolean addTask(Task item, Weekday day) {
//    if (this.maxTask > this.tasks.size() && this.weekday == day) {
//      tasks.add(item);
//      return true;
//    } else {
//      return false;
//    }
//  }
    /**
   * add item to a day
   *
   * @param item task
   * @param day  which day
   * @return if there is a space to add new task
   */
  public boolean addItem(PlannedItem item, Weekday day) {
    if (this.weekday == day) {
      if (item.add(this)) {
        items.add(item);
      }
      return item.add(this);
    } else {
      return false;
    }
  }

  /**
   * convert itemJson to dayJson
   *
   * @return dayJson
   */
  public DayJson toJson() {
    ArrayList<PlannedItemJson> jsons = new ArrayList<>();
    for (PlannedItem e : this.items) {
      if (e != null) {
        jsons.add(e.toJson());
      }
    }
    return new DayJson(this.weekday, this.date.toString(), jsons);
  }

  /**
   * delete same task
   *
   * @param t   task
   * @param day weekday
   */
  public void delete(PlannedItem t, Weekday day) {
    if (this.weekday == day) {
      items.remove(t);
    }
  }

  public boolean ableToAddEvent() {
    boolean ableToAdd = this.maxEvents > this.eventCount();
    return ableToAdd;
  }
  public boolean ableToAddTask() {
    boolean ableToAdd = this.maxTask > this.taskCount();
    return ableToAdd;
  }

  private int eventCount() {
    int result = 0;
    for (PlannedItem item : this.items) {
      result += item.eventCount();
    }
    return result;
  }
  private int taskCount() {
    int result = 0;
    for (PlannedItem e : this.items) {
      result += e.taskCount();
    }
    return result;
  }
}