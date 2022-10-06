package gov.nasa.pds.tools.validate.task;

/**
 * Defines the operations of a service that can run tasks in the background. The task manager will
 * generate events when tasks are run, paused, canceled, or complete.
 */
public interface TaskManager {

  /**
   * Submits a task to run in the background.
   *
   * @param task the task to run
   */
  void submit(Task task);

  /**
   * Removes a task from the task manager. The task manager will attempt to cancel the task if it is
   * running. Once a task is removed, further operations on the task using this task manager may
   * result in exceptions.
   *
   * @param task the task to remove
   */
  void remove(Task task);

  /**
   * Gets the task status.
   *
   * @param task the task
   * @return the task status
   */
  Task.Status getStatus(Task task);

  /**
   * Requests to cancel the task.
   *
   * @param task the task to cancel
   */
  void cancel(Task task);

}
