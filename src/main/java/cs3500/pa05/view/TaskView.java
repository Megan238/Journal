package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerImp;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Theme;
import cs3500.pa05.model.json.TaskJson;
import java.time.LocalTime;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * class TaskView
 */
public class TaskView extends PlannedItemView {
  private Text completeBox = new Text();

  /**
   * constructor for TaskView
   *
   * @param name
   * @param description
   * @param category
   * @param controllerImp imp
   * @param complete
   */
  public TaskView(String name, String description, String category, ControllerImp
      controllerImp, boolean complete) {
    super(name, description, category, controllerImp);
    HBox hbox = new HBox();
    this.changeTheme(theme);
    hbox.getChildren().add(icon);
    this.label.setFont(new Font(18));
    hbox.getChildren().add(label);
    this.getChildren().add(hbox);
    this.completeBox.setText("Completion Status: " + complete);
    this.getChildren().add(this.completeBox);
    this.getChildren().add(this.descriptionBox);
    this.getChildren().add(this.categoryBox);
    this.getChildren().add(detail);
  }

  /**
   * Changes the icon of the task view based on the specified theme.
   *
   * @param theme the theme to be applied to the task view.
   */
  public void changeTheme(Theme theme) {
    switch (theme) {
      case NIGHTTIME -> icon.setImage(new Image("nightTime/nightTimeTask.png"));
      case HOLIDAY -> icon.setImage(new Image("holiday/holidayTask.png"));
      default -> icon.setImage(new Image("default/dayTimeTask.png"));
    }
  }
  public void setComplete(boolean complete) {
    this.completeBox.setText("Completion Status: " + complete);
  }

  @Override
  public void presentDetail() {
    this.controllerImp.miniViewerTask(this);
  }

}
