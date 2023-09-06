package cs3500.pa05.model;

import cs3500.pa05.model.json.PlannedItemJson;
import java.time.LocalTime;

/**
 * class PlannedItem
 */
public abstract class PlannedItem {

  /**
   * the name for this PlannedItem
   */
  protected String name;

  /**
   * the description for this PlannedItem
   */
  protected String description;

  /**
   * the category for this PlannedItem, can be empty
   */
  protected String category;
  protected int duration;

  /**
   * constructor for PlannedItem
   *
   * @param name item name
   */
  protected PlannedItem(String name) {
    this.name = name;
    this.description = "";
    this.category = "";
    this.duration = 0;
  }

  /**
   * Returns the name of the planned item.
   *
   * @return The name of the planned item.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Appends to the description of the planned item.
   *
   * @param s The string to be appended to the description.
   */
  public void setDescription(String s) {
    description = s;
  }

  /**
   * Returns the description of the planned item.
   *
   * @return The description of the planned item.
   */
  public String getDescription() {
    return description.toString();
  }

  /**
   * Sets the category of the planned item.
   *
   * @param category The category to be set.
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Returns the category of the planned item.
   *
   * @return The category of the planned item.
   */
  public String getCategory() {
    return category;
  }

  /**
   * Checks if the planned item is in a specific category.
   *
   * @param c The category to check.
   * @return true if the planned item is in the category, false otherwise.
   */
  public Boolean inCategory(String c) {
    return this.category.equals(c);
  }

  /**
   * Compares this planned item with another planned item based on name.
   *
   * @param item The planned item to compare with.
   * @return The comparison result as an integer.
   */
  public int compareName(PlannedItem item) {
    return item.name.compareTo(this.name);
  }

  public int taskCount() {
    return 0;
  }
  public int eventCount() {
    return 0;
  }
  public abstract boolean add(Day day);
  public void setName(String name) {
    this.name = name;
  }
  public abstract PlannedItemJson toJson();
  public void setComplete(boolean complete) {}
  public void setDuration(int duration) {}
  public void setStartTime(LocalTime time) {}
  public int compareByDuration(PlannedItem that) {
    return Integer.compare(this.duration, that.duration);
  }
  public int compareByName(PlannedItem that) {
    return this.name.compareTo(that.name);
  }
  public boolean containName(String s) {
    return this.name.toLowerCase().contains(s);
  }
  public boolean getComplete() { return false;};
  public LocalTime getStartTime() {
    return LocalTime.now();
  }
  public int getDuration() {
    return this.duration;
  }
}
