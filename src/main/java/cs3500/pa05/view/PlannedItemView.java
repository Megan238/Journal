package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerImp;
import cs3500.pa05.model.Day;
import cs3500.pa05.model.PlannedItem;
import cs3500.pa05.model.Theme;
import cs3500.pa05.model.json.PlannedItemJson;
import java.time.LocalTime;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * class PlannedItemView
 */
public abstract class PlannedItemView extends VBox {
  /**
   * the theme for this PlannedItemView
   */
  protected Theme theme;
  /**
   * the label for this PlannedItemView
   */
  protected Label label = new Label();
  /**
   * the icon for this PlannedItemView
   */
  protected ImageView icon = new ImageView();
  /**
   * the edit button for this PlannedItemView
   */
  protected Button edit = new Button("edit");
  /**
   * the detail button for this PlannedItemView
   */
  protected Button detail = new Button("detail");
  /**
   * the controller for this view
   */
  protected ControllerImp controllerImp;
  protected Text descriptionBox = new Text();
  protected Text categoryBox = new Text();



  /**
   * Constructs a new PlannedItemView object with the specified planned item and controller.
   *
   * @param controllerImp the controller object responsible for handling user interactions.
   */
  PlannedItemView(String name,
                  String description,
                  String category,
                  ControllerImp controllerImp) {
    this.controllerImp = controllerImp;
    this.theme = controllerImp.getTheme();
    this.setBorder(Border.stroke(Color.BLACK));
    this.label.setText(name);
    descriptionBox.setWrappingWidth(200);
    this.descriptionBox.setText("Description : " + description);
    this.categoryBox.setText("Category : " + category);
    this.label.setFont(new Font(18));
    this.label.setStyle("-fx-text-fill: black");
    this.edit.setOnAction(e -> controllerImp.editPlannedItem(this));
    this.getChildren().add(edit);
    this.detail.setOnAction(e -> presentDetail());
  }

  /**
   * Changes the icon of the planned item view based on the specified theme.
   *
   * @param theme the theme to be applied to the planned item view.
   */
  public abstract void changeTheme(Theme theme);
  public void setDescription(String s) {
    this.descriptionBox.setText("Description : " + s);
  }

  /**
   * Sets the category of the planned item.
   *
   * @param category The category to be set.
   */
  public void setCategory(String category) {
    this.categoryBox.setText("Category : " + category);
  }
  public void setName(String name) {
    this.label.setText(name);
  }
  public void setComplete(boolean complete) {}
  public void setDuration(int duration) {}
  public void setStartTime(LocalTime time) {}
  public abstract void presentDetail();

}
