package plannerApp.controllers;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import plannerApp.Builder;
import plannerApp.Launcher;
import plannerApp.javafxWidget.GroupItem;
import plannerApp.javafxWidget.TaskItem;
import plannerApp.javafxWidget.calendar.CalendarBox;

public class AppController implements TaskAction, GroupAction {
    ScreenController controller;
    CalendarBox calendarBox;

    @FXML
    private HBox centerView, addGroupBox;
	@FXML
	private Button toTimer, addButton, addGroupButton;
    @FXML
    private AnchorPane taskForm;
	@FXML
	private VBox taskBox, groupBox;
    private ArrayList<TaskItem> taskList, completeList;
    private ArrayList<GroupItem> groupList;
    @FXML
    private TextArea contentField, addGroupField;

    private GroupItem chosenGroup;

    private GroupItem mainGroup, completeGroup;

    private boolean TimerIsRunning = false;

    private TimerHandler timerHandler = new TimerHandler();

    AppController(ScreenController controller) {
        this.controller = controller; 
    }
    
	@FXML
	private void initialize() {
		calendarBox = new CalendarBox();
        centerView.getChildren().add(calendarBox);

        addGroupField = new TextArea();
        Pattern pattern = Pattern.compile(".{0,15}");
        TextFormatter<Change> formatter = new TextFormatter<Change>((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        addGroupField.setTextFormatter(formatter);
        addGroupField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            {   if (!newPropertyValue) // true then field lose Focus 
                { addGroup(); }
            }
        });

        taskList = new ArrayList<>();
        groupList = new ArrayList<>();
        
        mainGroup = new GroupItem("Tasks", "none", this);
        groupBox.getChildren().add(groupBox.getChildren().size() - 1, mainGroup);
        
        completeGroup = new GroupItem("Complete", "none", this); 
        groupBox.getChildren().add(groupBox.getChildren().size() - 1, completeGroup);
       
        // TODO::Fill complete group from file

        // TODO::Class TaskForm

        chooseItem(mainGroup);
        for (GroupItem group : FileHelper.ReadGroupList(this)) {
            System.out.println(group.getTaskList());
            appendGroup(group);
        }

        for (TaskItem task : FileHelper.ReadCompleteTaskList(this)) {
            task.hideCheckerField();
            taskList.add(task);
            completeGroup.addTaskID(task.getID());
        }

        for (TaskItem task : FileHelper.ReadTaskList(this)) {    
            for (GroupItem group : groupList)
                if (group.getTaskList().contains(task.getID())) {
                    task.setGroup(group);
                    break;
                }
            appendTask(task);
        }
    }

    @FXML
    private void toTimerClicked() {
        if (TimerIsRunning) {
            return;
        } else {
            Stage secondStage = new Stage();
            secondStage.setOnHidden(event -> {
                TimerIsRunning = false;
                timerHandler = new TimerHandler();
            });
            Parent root = FileHelper.loadView(timerHandler, "timerView");
            Scene scene = new Scene(root, 400, 300);
            secondStage.setTitle("Countdown Timer");
            secondStage.setScene(scene);
            secondStage.show();
            TimerIsRunning = true;
        }
    }

    @FXML
    void addTask() {
        TaskItem task = createTask();

        appendTask(task);
        chosenGroup.addTaskID(task.getID());

        FileHelper.UpdateGroupList(groupList);

        contentField.setText("");
    }

    @FXML
    void addGroup() {
        if (addGroupBox.getChildrenUnmodifiable().contains(addGroupButton)) {
            addGroupBox.getChildren().remove(addGroupButton);
            addGroupBox.getChildren().add(addGroupField);
            addGroupField.requestFocus();
        }
        else {
            addGroupBox.getChildren().remove(addGroupField);
            addGroupBox.getChildren().add(addGroupButton);
            
            String groupName = addGroupField.getText();
            if (groupName.length() <= 0) return;

            GroupItem group = createGroup(groupName, "none");
            appendGroup(group);
            FileHelper.UpdateGroupList(groupList);

            addGroupField.setText("");
        }
    }
    private GroupItem createGroup(String groupName, String style) {
        GroupItem newGroup = new GroupItem(groupName, style, this);
        return newGroup; 
    }

    private TaskItem createTask() {
        TaskItem taskItem = new TaskItem(
            contentField.getText(),
            calendarBox.getActiveDate(),
            this
            );
        taskItem.setGroup(chosenGroup);
        taskItem.setSourceGroup(chosenGroup.getName());
        FileHelper.SaveTask(taskItem);
        
        return taskItem;
    }

    private void appendTask(TaskItem task) {
        taskList.add(task);
        taskBox.getChildren().add(task);
        mainGroup.addTaskID(task.getID());
    }

    private void appendGroup(GroupItem group) {
        groupList.add(group);
        groupBox.getChildren().add(groupBox.getChildren().size() - 1, group);
    }

    public void chooseItem(GroupItem currentGroup) {
        chosenGroup = currentGroup;
        
        mainGroup.setStyle("-fx-background-color: transparent;");
        completeGroup.setStyle("-fx-background-color: transparent;");
        for (GroupItem group : groupList)
            group.setStyle("-fx-background-color: transparent;");    

        currentGroup.setStyle("-fx-background-color: rgba(235,235,235, 0.7);");
        updateTaskList();
    }

    public void chooseItem(TaskItem item) {
        // TODO::Highlight task
    }

    public void completeItem(TaskItem item){
        if (item.getCheckerField().isSelected()){
            findGroupByName(item.getSourceGroup()).deleteTask(item.getID());
            mainGroup.deleteTask(item.getID());
            FileHelper.DeleteTask(item.getID());
            item.setSourceGroup("Complete");
            completeGroup.addTaskID(item.getID());
            item.hideCheckerField();
            FileHelper.SaveComleteTask(item);
        }
        updateTaskList();
    }

    public void deleteItem(TaskItem item) {
        if (findGroupByName(item.getSourceGroup()) == completeGroup) {
            completeGroup.deleteTask(item.getID());
            FileHelper.DeleteCompletedTask(item.getID());
        } else {
            findGroupByName(item.getSourceGroup()).deleteTask(item.getID());
            mainGroup.deleteTask(item.getID());
            FileHelper.DeleteTask(item.getID());
        }
        updateTaskList();
    }

    public GroupItem findGroupByName(String groupName) {
        for (GroupItem item: groupList) {
            if (item.getName().equals(groupName)) {
                return item;
            }
        }
        if (mainGroup.getName().equals(groupName)) {
            return mainGroup;
        }
        if (completeGroup.getName().equals(groupName)) {
            return completeGroup;
        }
        return null;
    }

    public void deleteGroupItem(GroupItem item){
        groupBox.getChildren().remove(item);
        ArrayList<Long> temp = item.deleteAllTasks();
        for (long elem: temp) {
            mainGroup.deleteTask(elem);
        }
        FileHelper.DeleteGroup(item);
        updateTaskList();
    }

    private void updateTaskList() {
        taskBox.getChildren().clear();
        for (TaskItem item : taskList)
            if (chosenGroup.getTaskList().contains(item.getID()))
                taskBox.getChildren().add(item);
    }
}
