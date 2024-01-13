package plannerApp.controllers;

import plannerApp.javafxWidget.TaskItem;

public interface TaskAction {
    public void chooseItem(TaskItem item);
    public void completeItem(TaskItem item);
    public void deleteItem(TaskItem item);
}