package plannerApp.javafxWidget.calendar;

import java.time.LocalDate;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.event.ActionEvent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CalendarBox extends StackPane {
    
    private VBox content;

    private HBox weekDays;

    private HBox topBar;
    private Label MonthYear;
    private Button prevMonth, nextMonth;
	
    // Calendar containers
    private VBox calendarContainer;
    private VBox nextCalendarContainer;

    private Button grid[][];

    private static final int rowCount = 6, colCount = 7;
    private static String activeDateClass = "activeDate", todayID = "today", passiveDayID = "passiveDay";

    private LocalDate activeDate;
    private Button activeButton;

    private LocalDate calendar;
    
    public CalendarBox() {
        weekDays = new HBox();
        weekDays.getStyleClass().add("calendar-row");
        
        fillWeekDays();
        
        prevMonth = createArrow(new FontIcon("mdi-arrow-up"));
        prevMonth.setOnAction((ActionEvent e)-> {setPrevMonth();});
        nextMonth = createArrow(new FontIcon("mdi-arrow-down"));
        nextMonth.setOnAction((ActionEvent e)-> {setNextMonth();});
        
        MonthYear = new Label();
        MonthYear.setId("calendarLabel");
        
        topBar = new HBox();
        topBar.getStyleClass().add("calendar-top-bar");
        topBar.getChildren().addAll(MonthYear, prevMonth, nextMonth);
        
        calendarContainer = new VBox();
        calendarContainer.getStyleClass().add("calendar-row-container");
        calendarContainer.getChildren().add(weekDays);
        
        content = new VBox();
        content.getStyleClass().add("calendar-container");
        content.getChildren().addAll(topBar, calendarContainer);
        this.getChildren().add(content);

        grid = new Button[rowCount][colCount];

        calendar = LocalDate.now();
        activeDate = LocalDate.now();

        fillCallendarBox(calendarContainer);
    }
    
    public void setNextMonth() {
        calendar = calendar.plusMonths(1);
        fillCallendarBox(calendarContainer);
    }

    public void setPrevMonth() {
        calendar = calendar.plusMonths(-1);
        fillCallendarBox(calendarContainer);
    }

    public LocalDate getActiveDate() {
        return activeDate;
    }

    private void onSwitchAnimation(final Pane prevPane, final Pane nextPane) {
        nextPane.translateYProperty().set(prevPane.getHeight());
		this.getChildren().add(nextPane);
        
		Timeline timeline = new Timeline();
        float timeDelay = 0.1f;
		KeyValue prevPaneMovement = new KeyValue(prevPane.translateYProperty(), -prevPane.getHeight(), Interpolator.LINEAR);
		KeyFrame prevPaneKeyFrame = new KeyFrame(Duration.seconds(timeDelay), prevPaneMovement);
		KeyValue nextPaneMovement = new KeyValue(nextPane.translateYProperty(), 40, Interpolator.LINEAR);
		KeyFrame nextPaneKeyFrame = new KeyFrame(Duration.seconds(timeDelay), nextPaneMovement);
		timeline.getKeyFrames().addAll(prevPaneKeyFrame, nextPaneKeyFrame);
        timeline.onFinishedProperty().set((ActionEvent e)->{
            prevPane.getChildren().clear();
            calendarContainer = nextCalendarContainer;  // Can't 
        });
		timeline.play();
    }

    private void fillWeekDays() {
        Button monday = new Button("Пн");
        monday.getStyleClass().add("weekDays");

        Button tuesday = new Button("Вт");
        tuesday.getStyleClass().add("weekDays");

        Button wednesday = new Button("Ср");
        wednesday.getStyleClass().add("weekDays");

        Button thursday = new Button("Чт");
        thursday.getStyleClass().add("weekDays");

        Button friday = new Button("Пт");
        friday.getStyleClass().add("weekDays");
        
        Button saturday = new Button("Сб");
        saturday.getStyleClass().add("weekDays");
        
        Button sunday = new Button("Вс");
        sunday.getStyleClass().add("weekDays");

        weekDays.getChildren().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }
    
    private Button createArrow(FontIcon content) {
        Button btnArrow = new Button("", content);
        btnArrow.getStyleClass().add("btnArrow");
        return btnArrow;
    }
    
    private void fillCallendarBox(VBox container) {
        container.getChildren().clear();
        container.getChildren().add(weekDays);
        updateCalendarLabel();
        setMonthDays(container);
    }

    private void updateCalendarLabel() {
        String text = String.format("%s %d", CalendarDict.month.get(calendar.getMonthValue()), calendar.getYear());
        MonthYear.setText(text);
    }

    private void setMonthDays(VBox container) {
        int dayCount = calendar.lengthOfMonth();
        int firstDayOfWeek = calendar.withDayOfMonth(1).getDayOfWeek().getValue();

        int lastMonthNum = calendar.getMonthValue() > 1 ? calendar.getMonthValue() - 1 : 12; 
        int daysCountBefore = calendar.withMonth(lastMonthNum).lengthOfMonth();
        int lastFillCellNum = dayCount + firstDayOfWeek - 1;

        fillDaysBeforeOfGridMassive(daysCountBefore, firstDayOfWeek);
        fillDaysOfGridMassive(dayCount, firstDayOfWeek);
        fillEndOfGridMassive(lastFillCellNum);
        
        updateGridPane(container);
    }

    private void fillDaysBeforeOfGridMassive(int daysCountBefore, int firstDayOfWeek) {
        for (int i = 0; i < firstDayOfWeek - 1; i++) {
            String dayText = Integer.toString(daysCountBefore + i - firstDayOfWeek + 2);
            Button dateGrid = new Button(dayText);
            dateGrid.setId(passiveDayID);
            grid[0][i] = dateGrid;
        }
    }

    private void fillDaysOfGridMassive(int dayCount, int firstDayOfWeek) {
        for (int i = 0; i < dayCount; i++) {
            String dayText = Integer.toString(i + 1);
            Button dateGrid = new Button(dayText);
            int row = (i + firstDayOfWeek - 1) / 7;
            int col = (i + firstDayOfWeek - 1) % 7;
            grid[row][col] = dateGrid;
        }
    }
    
    private void fillEndOfGridMassive(int lastFillCellNum) {
        int cellsCount = countFreeCellsAtEndOfMassive(lastFillCellNum);
        for (int i = 0; i < cellsCount; i++) {
            String dayText = Integer.toString(i + 1);
            Button dateGrid = new Button(dayText);
            dateGrid.setId(passiveDayID);
            
            
            int row = rowCount - (cellsCount - i - 1) / 7 - 1;
            int col = (rowCount * colCount - cellsCount + i) % 7;
            grid[row][col] = dateGrid;
        }
    }

    private int countFreeCellsAtEndOfMassive(int lastFillCellNum) {
        return rowCount * colCount - lastFillCellNum;
    }

    private void updateGridPane(VBox container) {
        for (int i = 0; i < rowCount; i++) {
            HBox row = new HBox();
            row.getStyleClass().add("calendar-row");
            for (int k = 0; k < colCount; k++) {
                Button date = grid[i][k];
                row.getChildren().add(date);
                
                if (date.getId() == passiveDayID) {
                    date.setOnAction((ActionEvent e) -> { passiveHandle(e); });
                    continue;
                }
                else {
                    date.setOnAction((ActionEvent e) -> { activeHandle(e); });
                }

                if (calendar.withDayOfMonth(Integer.parseInt(date.getText())).compareTo(activeDate) == 0)
                    { setActiveButton(date); }

                if (calendar.withDayOfMonth(Integer.parseInt(date.getText())).compareTo(LocalDate.now()) == 0)
                    { date.setId(todayID); }

            }
            container.getChildren().add(row);
        }
    }

    private void passiveHandle(ActionEvent event) {
        Button eventItem = (Button)event.getSource();
        if (Integer.parseInt(eventItem.getText()) < 15) setNextMonth();
        else setPrevMonth();
    }

    private void activeHandle(ActionEvent event) {
        Button eventItem = (Button)event.getSource();
        setActiveButton(eventItem);
    }

    private void setActiveButton(Button btn) {
        if (activeButton == btn) return;

        activeDate = calendar.withDayOfMonth(Integer.parseInt(btn.getText()));
        if (activeButton != null) activeButton.getStyleClass().remove(activeDateClass);
        activeButton = btn;
        activeButton.getStyleClass().add(activeDateClass);
    }
}