package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerImp;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.Theme;
import cs3500.pa05.model.json.EventJson;
import java.time.LocalTime;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * class EventView
 */
public class EventView extends PlannedItemView {
  /**
   * the duration for this event, can be 0
   */
  private Text durationBox = new Text();
  private Text startTimeBox = new Text();

  /**
   * constructor for EventView
   *
   * @param description
   * @param category
   * @param controllerImp imp
   * @param duration
   */
  public EventView(String name, String description, String category, ControllerImp
      controllerImp, int duration, LocalTime startTime) {
    super(name, description, category, controllerImp);
    HBox hbox = new HBox();
    this.changeTheme(theme);
    hbox.getChildren().add(icon);
    this.label.setFont(new Font(18));
    hbox.getChildren().add(label);
    this.getChildren().add(hbox);
    durationBox.setText("Duration time : " + duration);
    startTimeBox.setText("startTime : " + startTime);
    this.getChildren().add(startTimeBox);
    this.getChildren().add(durationBox);
    this.getChildren().add(descriptionBox);
    this.getChildren().add(categoryBox);
    this.getChildren().add(detail);
  }

  public void setDuration(int duration) {

    this.durationBox.setText("Duration time : " + duration);
  }
  public void setStartTime(LocalTime time) {

    startTimeBox.setText("startTime : " + time);
  }

  @Override
  public void presentDetail() {
    this.controllerImp.miniViewerEvent(this);
  }


  /**
   * Changes the theme of the event view by updating the icon image based on the specified theme.
   *
   * @param theme the theme to be applied to the event view.
   */
  public void changeTheme(Theme theme) {
    switch (theme) {
      case NIGHTTIME -> icon.setImage(new Image("nightTime/nightTimeEvent.png"));
      case HOLIDAY -> icon.setImage(new Image("holiday/holidayEvent.png"));
      default -> icon.setImage(new Image("default/dayTimeEvent.png"));
    }
  }
}
