package gov.nasa.pds.tools.validate.task;

/**
 * Defines an interface all tasks must implement.
 */
public interface Task {

  /**
   * Defines the states a task can be in.
   */
  public enum Status {

    /** The task is not yet started. */
    NOT_STARTED,

    /** The task is running. */
    RUNNING,

    /** The task was canceled before completion. */
    CANCELED,

    /** The task completed normally. */
    COMPLETE;

  }

  /**
   * Executes the task with a given advisor to detect cancel requests.
   *
   * @param advisor the task advisor
   */
  void execute(TaskAdvisor advisor);

}
