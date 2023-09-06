package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerImp;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Weekday;
import cs3500.pa05.model.json.TaskJson;
import java.time.LocalTime;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * class CreateTask
 */
public class CreateTask extends CreatePlannedItem {

  private final TextArea descriptionContent = new TextArea();
  private final CheckBox isComplete = new CheckBox("Completion status");
  private boolean complete = false;

  /**
   * constructor CreateTask
   *
   * * @param weekday    which weekday to create task
   * @param categories which category the task is
   */
  public CreateTask(List<String> categories, Weekday weekday, ControllerImp controllerImp) {
    super(categories, weekday, controllerImp);
    HBox description = new HBox();
    description.getChildren().add(new Text("Description: "));
    descriptionContent.setPromptText("Optional");
    description.getChildren().add(descriptionContent);
    this.vbox.getChildren().add(isComplete);
    this.vbox.getChildren().add(description);
    this.isComplete.setOnAction(e -> complete = !complete);
  }



  public CreateTask(List<String> categories,
                     String name, String description, boolean complete,
                    Weekday weekday, ControllerImp controllerImp, PlannedItemView view) {
    this(categories, weekday, controllerImp);
    this.descriptionContent.setText(description);
    this.nameFile.setText(name);
    this.complete = complete;
    this.view = view;
  }

  @Override
  protected void edit() {
    if (nameFile.getText().length() == 0) {
      nameFile.setBorder(Border.stroke(Paint.valueOf("RED")));
      board.setText("Name cannot be empty");
    } else {
      this.controller.editTask(nameFile.getText(),
          descriptionContent.getText(), this.category, complete, this.view);
      this.hide();
    }
  }

  /**
   * A helper method that is used by the create() method.
   * It handles the actual creation of the Event object and validates the user input.
   *
   * @return true if the event is successfully created; false otherwise.
   */
  protected boolean createHelper() {
    if (nameFile.getText().length() == 0) {
      nameFile.setBorder(Border.stroke(Paint.valueOf("RED")));
      board.setText("Name cannot be empty");
      return false;
    } else {
      this.item = new Task(nameFile.getText());
      item.setDescription(descriptionContent.getText());
      item.setCategory(this.category);
        item.setComplete(complete);
      this.view = new TaskView(nameFile.getText(),
          this.descriptionContent.getText(), this.category,this.controller, complete);
      this.hide();
      return true;
    }
  }
}