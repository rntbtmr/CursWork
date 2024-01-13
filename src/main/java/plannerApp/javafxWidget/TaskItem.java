package plannerApp.javafxWidget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.SnapshotResult;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import plannerApp.controllers.TaskAction;

public class TaskItem extends AnchorPane{

	private RadioButton checkerField;
	private Button deleteField;
	
	private Label contentField;
	private Label dateField;
	private Label groupField;
	private String sourceGroupName;

	private LocalDate dateInfo;
	private long ID;

	public TaskItem(String content, LocalDate date, TaskAction deleteItem, long ID) {
		this(content, date, deleteItem);
		this.ID = ID;
	}

	public void hideCheckerField() {
		this.checkerField.setVisible(false);
	}

	public TaskItem(String content, LocalDate date, TaskAction action) {
		ID = System.currentTimeMillis();

		contentField = new Label(content);
		contentField.getStyleClass().add("taskContent");
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyy");
		dateField = new Label(date.format(format));
		dateField.getStyleClass().add("secondaryInfo");
		if (date.compareTo(LocalDate.now()) < 0) {
			dateField.getStyleClass().add("overdueDate");
		}
		
		groupField = new Label("Tasks");
		groupField.getStyleClass().add("secondaryInfo");
		
		dateInfo = date;
		deleteField = new Button("", new FontIcon("mdi-delete-forever"));
		deleteField.getStyleClass().add("deleteBtn");
		deleteField.setOnAction((e) -> {
			action.deleteItem(this);
		});

		checkerField = new RadioButton();
		checkerField.setOnAction((e) -> {
			action.completeItem(this);
		});

		this.getChildren().addAll(contentField, checkerField, dateField, groupField, deleteField);
		this.getStyleClass().add("taskItem");

		setTopAnchor(contentField, 0.0);
		setBottomAnchor(contentField, 10.0);
		setLeftAnchor(contentField, 20.0);
		setRightAnchor(contentField, 30.0);

		setTopAnchor(deleteField, 0.0);
		setBottomAnchor(deleteField, 0.0);
		setRightAnchor(deleteField, -10.0);

		setTopAnchor(checkerField, 0.0);
		setBottomAnchor(checkerField, 0.0);
		setLeftAnchor(checkerField, -2.5);
		
		setBottomAnchor(groupField, -5.0);
		setLeftAnchor(groupField, 20.0);

		setBottomAnchor(dateField, -5.0);
		setRightAnchor(dateField, 30.0);
	}
	
	public long getID() {
		return ID;
	}

	public LocalDate getDateField() {
		return dateInfo;
	}

	public RadioButton getCheckerField() {
		return checkerField;
	}

	public void setContent(String content) {
		this.contentField.setText(content);
	}
	
	public void setDateTask(String DateTask) {
		this.dateField.setText(DateTask);
	}

	public void setCheckerField(RadioButton checkBox) {
		this.checkerField = checkBox;
	}

	public Label getGroupField() {
		return groupField;
	}

	public void setGroup(GroupItem group) {
		this.groupField.setText(group.getName());
	}

	public void setSourceGroup(String groupName) {
		this.sourceGroupName = groupName;
	}

	public String getSourceGroup() {
		return sourceGroupName;
	}

	@Override
	public String toString() {
		return String.format("%s§%s§%s§%d§%s",
		contentField.getText(), this.dateField.getText(), this.groupField.getText(), this.ID, this.sourceGroupName);
	}
}
