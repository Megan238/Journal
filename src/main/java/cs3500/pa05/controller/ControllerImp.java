package cs3500.pa05.controller;

import cs3500.pa05.model.Event;
import cs3500.pa05.model.PlannedItem;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Theme;
import cs3500.pa05.model.Week;
import cs3500.pa05.model.Weekday;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.view.CreateEvent;
import cs3500.pa05.view.CreatePlannedItem;
import cs3500.pa05.view.CreateTask;
import cs3500.pa05.view.EventView;
import cs3500.pa05.view.PlannedItemView;
import cs3500.pa05.view.TaskView;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * class ControllerImp
 */
public class ControllerImp implements Controller {
//  private Popup searchWindow = new Popup();
  private final Week week;
  private final Stage st;
  private final ProxyController proxyController;
  private final HashMap<PlannedItemView, PlannedItem> itemHashSet;
  private final HashMap<PlannedItemView, CreatePlannedItem> itemCreateHashSet;
  private VBox[] days;
  @FXML
  private VBox all;
  @FXML
  private VBox sunday;
  @FXML
  private VBox monday;
  @FXML
  private VBox tuesday;
  @FXML
  private VBox friday;
  @FXML
  private VBox saturday;
  @FXML
  private VBox wednesday;
  @FXML
  private VBox thursday;
  @FXML
  private MenuButton task;
  @FXML
  private MenuButton event;
  @FXML
  private TextField tasksMax;
  @FXML
  private TextField eventsMax;
  @FXML
  private Button save;
  @FXML
  private MenuButton theme;
  @FXML
  private Button enterMax;
  @FXML
  private Button category;
  @FXML
  private DatePicker date;
  @FXML
  private MenuButton sort;
  @FXML
  private MenuButton filter;
  @FXML
  private MenuItem recover;
  @FXML
  private Button search;
  @FXML
  private TextField searchField;
  @FXML
  private Button editName;
  @FXML
  private Button saveName;
  @FXML
  private TextField weekName;
  @FXML
  private VBox searchBox;
  private boolean nameEditable;
  private final ObservableList<PlannedItemView> itemList = FXCollections.observableArrayList();
  private FilteredList<PlannedItemView> filterList = new FilteredList<>(itemList);
  private ListView<PlannedItemView> listView = new ListView<PlannedItemView>(filterList);


  /**
   * constructor for ControllerImp
   *
   * @param week            the week
   * @param stage           the stage being presented
   * @param proxyController the proxyController
   */
  public ControllerImp(Week week, Stage stage, ProxyController proxyController) {
    this.week = week;
    this.st = stage;
    this.proxyController = proxyController;
    this.itemHashSet = new HashMap<>();
    this.itemCreateHashSet = new HashMap<>();
  }

  @FXML
  @Override
  public void run() {
    this.setTheme();
    this.setAddEvent();
    this.setAddTask();
    this.weekName.setEditable(false);
    this.editName.setOnAction(e -> this.weekName.setEditable(true));
    this.save.setOnAction(e -> saveFile());
    this.saveName.setOnAction(e -> changeName());
    this.tasksMax.setTextFormatter(onlyInputNum());
    this.eventsMax.setTextFormatter(onlyInputNum());
    this.enterMax.setOnAction(e -> setMax());
    this.date.setOnAction(e -> setDate(this.date.getValue()));
    this.category.setOnAction(e -> addCategory());
    this.setSort();
    this.recover.setOnAction(e -> recover());
    this.setFilter();
    this.searchBox.getChildren().add(listView);
    this.setSearch();
  }
  private void setSearch() {
    this.searchField
        .textProperty().addListener((observable, oldValue, newValue) ->
            searchTask(newValue));
    listView.setCellFactory(new Callback<ListView<PlannedItemView>, ListCell<PlannedItemView>>() {
      @Override
      public ListCell<PlannedItemView> call(ListView<PlannedItemView> param) {
        final ListCell<PlannedItemView> result = new ListCell<>() {
          @Override
          protected void updateItem(PlannedItemView item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("");
            } else {
              PlannedItem e = itemHashSet.get(item);
              setText(e.getName());
              setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                  item.presentDetail();
                }
              });
            }
          }
        };
        return result;
      }
    });
  }

  /**
   * set filter button
   */
  private void setFilter() {
    for (String c : this.week.getCategories()) {
      MenuItem item = new MenuItem(c);
      this.filter.getItems().add(item);
      item.setOnAction(e -> filter(c));
    }
  }

  /**
   * @return the text formatter that only present number
   */
  private TextFormatter<String> onlyInputNum() {
    return new TextFormatter<>((TextFormatter.Change change) -> {
      String newText = change.getControlNewText();
      try {
        Integer.parseInt(newText);
        return change;
      } catch (Exception e) {
        return null;
      }
    });
  }

  /**
   * This method disables the ability to edit the week name text field
   * and updates the name of the week to the current text in the text field.
   */
  private void changeName() {
    this.weekName.setEditable(false);
    this.week.changeName(this.weekName.getText());
  }


  /**
   * This method filters tasks and events based on their category.
   * Only items that belong to the specified category remain visible.
   * All other items are hidden.
   *
   * @param category The name of the category to filter by.
   */
  private void filter(String category) {
    for (Map.Entry<PlannedItemView, PlannedItem> item1 : this.itemHashSet.entrySet()) {
      Boolean visible = item1.getValue().inCategory(category);
      item1.getKey().setVisible(visible);
    }
  }

  /**
   * This method searches for items that contain a given string and displays them in a popup.
   *
   * @param newValue The string to search for in items
   */
  private void searchTask(String newValue) {
    filterList.setPredicate(new Predicate<PlannedItemView>() {
      @Override
      public boolean test(PlannedItemView plannedItemview) {
        PlannedItem e = itemHashSet.get(plannedItemview);

        return e.containName(newValue);
      }
    });

  }

  /**
   * This method restores the visibility of all TaskViews and EventViews
   * stored in the 'taskHashSet' and 'eventHashSet' respectively.
   */
  private void recover() {
    for (Map.Entry<PlannedItemView, PlannedItem> item : this.itemHashSet.entrySet()) {
      item.getKey().setVisible(true);
    }
  }

  /**
   * Sets the date of the week and updates the view to display the days with the respective dates.
   *
   * @param startDate The starting date of the week.
   */
  @FXML
  public void setDate(LocalDate startDate) {
    this.days = new VBox[] {
        sunday, monday, tuesday, wednesday, thursday, friday, saturday
    };
    this.week.setDate(startDate);
    for (int i = 0; i < 7; i++) {
      LocalDate today = startDate.plusDays(i);
      Text n = (Text) this.days[i].getChildren().get(1);
      n.setText(today.toString());
      n.setId(today.toString());
    }
  }

  /**
   * Sets the maximum number of events and tasks for the week. If the user tries to set the maximum
   * below the current number of tasks/events, a warning is displayed.
   */
  private void setMax() {
    try {
      int eventMax = Integer.parseInt(this.eventsMax.getText());
      int taskMax = Integer.parseInt(this.tasksMax.getText());
      if (!this.week.setMax(eventMax, taskMax)) {
        this.warning("Your current tasks or events is more than Max, "
            + "please re-enter the Max");
      } else {
        this.warning("You successful change the Max for task and event for every day");
      }
    } catch (Exception e) {
      this.warning(e.getMessage());
    }
  }

  /**
   * Displays a warning popup with a given message.
   *
   * @param warningContent The message to be displayed in the warning popup.
   */
  private void warning(String warningContent) {
    Popup warning = new Popup();
    VBox v = new VBox();
    v.setStyle("-fx-background-color:white;-fx-border-color: black;-fx-border-width:2;"
        + "-fx-border-radius:3;-fx-hgap:3;-fx-vgap:5;");
    warning.getContent().add(v);
    Button close = new Button("Close");
    close.setOnAction(e -> warning.hide());
    v.getChildren().add(new Text((warningContent)));
    v.getChildren().add(close);
    warning.show(st);
  }

  /**
   * Persists the week to a file and closes the application.
   */
  private void saveFile() {
    this.proxyController.writeFile(this.week);
    st.close();
  }

  @FXML
  public void setAddEvent() {
    for (MenuItem item : event.getItems()) {
      Weekday weekday = Weekday.valueOf(item.getId().substring(0, 3));
      CreatePlannedItem createPage =
          new CreateEvent(this.week.getCategories(), weekday, this);
      item.setOnAction(e -> createPage.show(st));
    }
  }

  private void setAddTask() {
    for (MenuItem item : task.getItems()) {
      Weekday weekday = Weekday.valueOf(item.getId());
      CreatePlannedItem createPage = new CreateTask(this.week.getCategories(), weekday,
          this);
      item.setOnAction(e ->createPage.show(st));
    }
  }

  /**
   * Sets up the sorting functionality for the sort menu items.
   */
  public void setSort() {
    sort.getItems().get(0).setOnAction(e -> sortBy(false));
    sort.getItems().get(1).setOnAction(e -> sortBy(true));
  }

  /**
   * This method sorts the contents of each day's VBox by either name or duration.
   * The specific sorting behavior is determined by the boolean parameter passed to it.
   *
   * @param duration A boolean value that determines the sorting behavior.
   *                 If true, the method sorts by duration.
   *                 If false, the method sorts by name.
   */
  private void sortBy(Boolean duration) {
    for (VBox day : this.days) {
      FXCollections.sort(day.getChildren(), (o1, o2) -> {
        if (o1 instanceof PlannedItemView view1 && o2 instanceof PlannedItemView view2) {
          PlannedItem item1 = this.itemHashSet.get(view1);
          PlannedItem item2 = this.itemHashSet.get(view2);
          if (duration) {

            return item1.compareByDuration(item2);
          } else {
            return item1.compareByName(item2);
          }
        } else {
          return 0;
        }
      });
    }
  }

  /**
   * Adds a new event to the week. If the maximum number of tasks for the weekday has been reached,
   * a warning is displayed and the event is not added.
   *
   * @param item   The item to be added.
   * @param weekday The weekday the event is to be added to.
   * @param view    The EventView associated with the event.
   * @param create
   */
  @FXML
  public void addPlannedItem(PlannedItem item,
                             Weekday weekday,
                             PlannedItemView view,
                             CreatePlannedItem create) {
    if (!this.week.addItem(weekday, item)) {
      this.warning("You cannot add task for " + weekday + "\n"
          + "you all ready reach the Maximum task");
    } else {
      this.getDay(weekday).getChildren().add(view);
      this.itemHashSet.put(view, item);
      this.itemCreateHashSet.put(view, create);
      this.itemList.add(view);
    }
  }


  /**
   * Sets up the theme menu items to change the theme when selected.
   */
  public void setTheme() {
    for (MenuItem item : theme.getItems()) {
      Theme theme = Theme.valueOf(item.getId());
      item.setOnAction(e -> this.chooseTheme(theme));
    }
  }

  /**
   * Gets the current theme of the week.
   *
   * @return The current theme.
   */
  public Theme getTheme() {
    return this.week.getTheme();
  }

  /**
   * Changes the theme of the week.
   *
   * @param theme The new theme.
   */
  @FXML
  public void chooseTheme(Theme theme) {
    this.week.setTheme(theme);
    all.getScene().getRoot().getStylesheets().clear();
    switch (this.getTheme()) {
      case NIGHTTIME -> all.getScene().getRoot().getStylesheets()
          .add(Objects.requireNonNull(
              getClass().getClassLoader().getResource("nightTime/nightTime.css")).toString());
      case HOLIDAY -> all.getScene().getRoot().getStylesheets()
          .add(
              Objects.requireNonNull(getClass().getClassLoader().getResource("holiday/holiday.css"))
                  .toString());
      default -> all.getScene().getRoot().getStylesheets()
          .add(
              Objects.requireNonNull(getClass().getClassLoader().getResource("default/dayTime.css"))
                  .toString());
    }
    for (VBox b : this.days) {
      for (Node n : b.getChildren()) {
        if (n instanceof EventView) {
          PlannedItemView view = (PlannedItemView) n;
          view.changeTheme(theme);
        }
        if (n instanceof TaskView) {
          PlannedItemView view = (PlannedItemView) n;
          view.changeTheme(theme);
        }
      }
    }
  }


  /**
   * Displays a pop-up allowing the user to add a new category to the week.
   */
  @FXML
  public void addCategory() {
    Popup addCategory = new Popup();
    VBox v = new VBox();
    v.setStyle("-fx-background-color:white;-fx-border-color: black;-fx-border-width:2;"
        + "-fx-border-radius:3;-fx-hgap:3;-fx-vgap:5;");
    addCategory.getContent().add(v);
    Button add = new Button("Add");
    TextField text = new TextField();
    add.setOnAction(e -> {
          addCategory.hide();
          week.addCategory(text.getText());

          MenuItem item = new MenuItem(text.getText());
          this.filter.getItems().add(item);
          item.setOnAction(a -> this.filter(text.getText()));
        }

    );
    v.getChildren().add(new Text("Please enter a new category:"));
    v.getChildren().add(text);
    v.getChildren().add(add);
    addCategory.show(st);
  }

  /**
   * Edits an existing item.
   *
   * @param view The PlannedItemView associated with the event to be edited.
   */
  public void editPlannedItem(PlannedItemView view) {
    CreatePlannedItem c = this.itemCreateHashSet.get(view);
    c.show(st);
  }

  public void editEvent(String name, String description, String category,
                        int duration, LocalTime startTime, PlannedItemView view) {
    PlannedItem item = this.itemHashSet.get(view);
    item.setCategory(category);
    item.setDescription(description);
    item.setDuration(duration);
    item.setName(name);
    item.setStartTime(startTime);
    view.setCategory(category);
    view.setDescription(description);
    view.setDuration(duration);
    view.setName(name);
    view.setStartTime(startTime);
  }

  public void editTask(String name, String description, String category,
                       boolean complete, PlannedItemView view) {
    PlannedItem item = this.itemHashSet.get(view);
    item.setCategory(category);
    item.setDescription(description);
    item.setName(name);
    item.setComplete(complete);
    view.setCategory(category);
    view.setDescription(description);
    view.setName(name);
    view.setComplete(complete);
  }


  /**
   * Displays a pop-up with details about a task.
   *
   * @param view The TaskView associated with the task.
   */
  public void miniViewerTask(TaskView view) {
    PlannedItem item = this.itemHashSet.get(view);
    Weekday weekday = Weekday.valueOf(view.getParent().getId().substring(0, 3).toUpperCase());
    VBox dayView = this.getDay(weekday);
    String t = dayView.getChildren().get(1).getId();
    Popup mini = new Popup();
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("taskMini.fxml"));
    Controller c = new Controller() {
      @FXML
      private Label nameLabel;
      @FXML
      private Text descriptionText;
      @FXML
      private Text dateText;
      @FXML
      private CheckBox completeBox;
      @FXML
      private Button close;

      @Override
      public void run() {
        this.nameLabel.setText(item.getName());
        this.descriptionText.setText(item.getDescription());
        this.dateText.setText(t);
        completeBox.setSelected(item.getComplete());
        this.close.setOnAction(e -> mini.hide());
      }
    };
    loader.setController(c);
    try {
      Scene s = loader.load();
      mini.getContent().add(s.getRoot());
      c.run();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    mini.show(this.st);
  }

  /**
   * Displays a pop-up with details about an event.
   *
   * @param view  The EventView associated with the event.
   */
  public void miniViewerEvent(EventView view) {
    PlannedItem item = this.itemHashSet.get(view);
    Weekday weekday = Weekday.valueOf(view.getParent().getId().substring(0, 3).toUpperCase());
    VBox dayView = this.getDay(weekday);
    String t = dayView.getChildren().get(1).getId();
    Popup mini = new Popup();
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("miniEvent.fxml"));
    Controller c = new Controller() {
      @FXML
      private Label nameLabel;
      @FXML
      private Text descriptionText;
      @FXML
      private Text dateText;
      @FXML
      private Text startText;
      @FXML
      private Text durationText;
      @FXML
      private Button close;

      @Override
      public void run() {
        this.nameLabel.setText(item.getName());
        this.descriptionText.setText(item.getDescription());
        this.dateText.setText(t);
        this.startText.setText(String.valueOf(item.getStartTime()));
        this.durationText.setText(String.valueOf(item.getDuration()));
        this.close.setOnAction(e -> mini.hide());
      }
    };
    loader.setController(c);

    try {
      Scene s = loader.load();
      mini.getContent().add(s.getRoot());
      c.run();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    mini.show(this.st);
  }

  /**
   * Retrieves the VBox associated with a specific weekday.
   *
   * @param weekday The weekday.
   * @return The VBox associated with the weekday.
   */
  private VBox getDay(Weekday weekday) {
    VBox dayView;
    switch (weekday) {
      case FRI -> dayView = friday;
      case MON -> dayView = monday;
      case SAT -> dayView = saturday;
      case SUN -> dayView = sunday;
      case TUE -> dayView = tuesday;
      case WED -> dayView = wednesday;
      case THU -> dayView = thursday;
      default -> throw new IllegalStateException("Unexpected value: " + weekday);
    }
    return dayView;
  }

  /**
   * assign name for week both in model and view
   *
   * @param name the name user choose
   */
  public void setName(String name) {
    this.week.setName(name);
    this.weekName.setText(name);
  }
}



