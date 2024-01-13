package plannerApp.javafxWidget;

import java.util.ArrayList;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import plannerApp.controllers.GroupAction;

public class GroupItem extends AnchorPane {

    String color;
    Label groupName;
    Button deleteGroup;

    ArrayList<Long> taskList;

    public GroupItem (String name, String color, GroupAction action) {
        this.onMouseClickedProperty().set((e) -> {
            action.chooseItem(this);
        });

        groupName = new Label(name);
        deleteGroup = new Button("", new FontIcon("mdi-delete-forever"));
        deleteGroup.getStyleClass().add("deleteBtn");
        deleteGroup.setOnAction((e) -> {
            action.deleteGroupItem(this);
        });

        this.color = color;
        taskList = new ArrayList<Long>();

        this.getChildren().addAll(groupName, deleteGroup);
        this.getStyleClass().add("groupItem");

        AnchorPane.setLeftAnchor(groupName, 0.0);
        AnchorPane.setTopAnchor(groupName, 0.0);
        AnchorPane.setBottomAnchor(groupName, 0.0);

        AnchorPane.setRightAnchor(deleteGroup, 0.0);
        AnchorPane.setTopAnchor(deleteGroup, 0.0);
        AnchorPane.setBottomAnchor(deleteGroup, 0.0);
    }

    public void addTaskID(long taskID) {
        taskList.add(taskID);
    }

    public ArrayList<Long> deleteAllTasks() {
        ArrayList<Long> temp = (ArrayList<Long>) taskList.clone();
        taskList.clear();
        return temp;
    }


    public void deleteTask(long taskID) {
        taskList.remove(taskID);
    }

    public String getName() {
        return groupName.getText();
    }

    public String getColor() {
        return color;
    }

    public ArrayList<Long> getTaskList() {
        return taskList;
    }

    @Override
    public String toString() {
        return groupName.getText();
    }
}