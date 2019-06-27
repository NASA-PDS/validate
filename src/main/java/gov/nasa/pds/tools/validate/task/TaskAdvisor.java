package gov.nasa.pds.tools.validate.task;

/**
 * Defines an interface that a task may use to find out whether
 * it should exit early.
 */
public interface TaskAdvisor {

    /**
     * Tests whether the task is being asked to cancel early.
     *
     * @return true, if the task should cancel itself
     */
    boolean cancelRequested();

    /**
     * Sets the status of the task.
     *
     * @param status the new status
     */
    void setStatus(Task.Status status);

}
