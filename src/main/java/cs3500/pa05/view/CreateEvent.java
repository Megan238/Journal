package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerImp;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.Weekday;
import cs3500.pa05.model.json.EventJson;
import java.time.LocalTime;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * class CreateEvent
 */
public class CreateEvent extends CreatePlannedItem {
  private final TextArea descriptionContent = new TextArea();
  private final TextField hour = new TextField();
  private final TextField minute = new TextField();
  private final TextField durationTime = new TextField();

  /**
   * constructor for CreateEvent
   *
   * @param weekday    which weekday to create event
   * @param controllerImp
   * @param categories the category of the event being created
   */
  public CreateEvent(List<String> categories, Weekday weekday,
                     ControllerImp controllerImp) {
    super(categories, weekday, controllerImp);
    HBox description = new HBox();
    description.getChildren().add(new Text("Description: "));
    descriptionContent.setPromptText("Optional");
    description.getChildren().add(descriptionContent);
    hour.setAccessibleRoleDescription("hour");
    minute.setAccessibleRoleDescription("minute");
    minute.setTextFormatter(this.limitInput());
    HBox startTime = new HBox();
    startTime.getChildren().add(new Text("Start Time : "));
    startTime.getChildren().add(hour);
    startTime.getChildren().add(minute);
    HBox duration = new HBox();
    duration.getChildren().add(new Text("Duration: "));
    this.durationTime.setPromptText("In Minutes");
    duration.getChildren().add(durationTime);
    this.vbox.getChildren().add(duration);
    this.vbox.getChildren().add(startTime);
    this.vbox.getChildren().add(description);
  }
  public CreateEvent(List<String> categories,
                     String name, String description, int duration, LocalTime startTime,
                     Weekday weekday, ControllerImp controllerImp, PlannedItemView view) {
    this(categories, weekday, controllerImp);
    this.descriptionContent.setText(description);
    this.nameFile.setText(name);
    this.durationTime.setText(String.valueOf(duration));
    this.hour.setText(String.valueOf(startTime.getHour()));
    this.minute.setText(String.valueOf(startTime.getMinute()));
    this.view = view;
  }



  /**
   * A helper method that is used by the create() method.
   * It handles the actual creation of the Event object and validates the user input.
   *
   * @return true if the event is successfully created; false otherwise.
   */
  protected boolean createHelper() {
    try {
      LocalTime time = LocalTime.of(Integer.parseInt(hour.getText()),
          Integer.parseInt(minute.getText()));
      int duration1 = Integer.parseInt(durationTime.getText());
      String name = this.nameFile.getText();
      if (name.length() == 0) {
        nameFile.setBorder(Border.stroke(Paint.valueOf("RED")));
        board.setText("Name cannot be empty");
      } else {
        this.item = new Event(name, duration1, time);
        item.setDescription(descriptionContent.getText());
        item.setCategory(this.category);
        this.view = new EventView(name,
            descriptionContent.getText(), this.category,
            controller, duration1, time);
        this.hide();
        return true;
      }
    } catch (Exception e) {
      hour.setBorder(Border.stroke(Paint.valueOf("RED")));
      minute.setBorder(Border.stroke(Paint.valueOf("RED")));
      board.setText("Invalid start time");
      return false;
    }
    return false;
  }

  @Override
  protected void edit() {
    try {
      LocalTime time = LocalTime.of(Integer.parseInt(hour.getText()),
          Integer.parseInt(minute.getText()));
      int duration1 = Integer.parseInt(durationTime.getText());
      String name = this.nameFile.getText();
      if (name.length() == 0) {
        nameFile.setBorder(Border.stroke(Paint.valueOf("RED")));
        board.setText("Name cannot be empty");
      } else {
        this.controller.editEvent(name, descriptionContent.getText(),this.category,
            duration1,time, view);
        this.hide();
      }
    } catch (Exception e) {
      hour.setBorder(Border.stroke(Paint.valueOf("RED")));
      minute.setBorder(Border.stroke(Paint.valueOf("RED")));
      board.setText("Invalid start time");
    }
  }

  /**
   * @return the text formatter that only allow number with two digit
   */
  private TextFormatter<String> limitInput() {
    return new TextFormatter<>((TextFormatter.Change change) -> {
      String newText = change.getControlNewText();
      if (newText.length() > 2) {
        return null;
      } else {
        try {
          Integer.parseInt(newText);
          return change;
        } catch (Exception e) {
          return null;
        }
      }
    });
  }



}