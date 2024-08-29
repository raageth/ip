package darwin.task;

import darwin.exception.IllegalTaskNumberException;
import darwin.storage.DbManager;

import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<Task> taskList;
    private final DbManager db;
    public TaskManager() {
        this.db = new DbManager();
        this.taskList = db.getTasks();
    }

    public int getTaskCount() {
        return this.taskList.size();
    }

    public Task addTask(Task task) {
        this.taskList.add(task);
        this.save();
        return task;
    }

    public Task deleteTask(int taskIdx) throws IllegalTaskNumberException {
        Task task = this.getTask(taskIdx);
        this.taskList.remove(taskIdx);
        this.save();
        return task;
    }

    public Task markTask(int taskIdx) throws IllegalTaskNumberException {
        Task task = this.getTask(taskIdx);
        task.markDone();
        this.save();
        return task;
    }

    public Task unmarkTask(int taskIdx) throws IllegalTaskNumberException {
        Task task = this.getTask(taskIdx);
        task.unmarkDone();
        this.save();
        return task;
    }

    private void save() {
        this.db.writeTasks(this.taskList);
    }

    private Task getTask(int taskIdx) throws IllegalTaskNumberException {
        if (taskIdx < 0 || taskIdx >= this.getTaskCount()) {
            throw new IllegalTaskNumberException(String.format("%d is not a valid task number", taskIdx));
        }
        return this.taskList.get(taskIdx);
    }

    /**
     * Finds tasks that contain the keyword.
     * @param keyword A string to search for in the task description.
     * @return A string of the tasks that contain the keyword.
     */
    public String findTasksStr(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : this.taskList) {
            if (task.getTaskInfo().contains(keyword)) {
                foundTasks.add(task);
            }
        }
        return this.getTaskListStr(foundTasks);
    }

    public String getTaskListStr() {
        return this.getTaskListStr(this.taskList);
    }

    private String getTaskListStr(ArrayList<Task> taskList) {
        StringBuilder taskListStr = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            String taskInfo = String.format("%d.%s", i + 1, task.getTaskInfo());
            taskListStr.append(taskInfo).append("\n");
        }
        return taskListStr.toString().trim();
    }
}
