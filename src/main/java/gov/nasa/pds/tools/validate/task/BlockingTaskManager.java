package gov.nasa.pds.tools.validate.task;

import gov.nasa.pds.tools.validate.task.Task.Status;

/**
 * Implements a simple task manager suitable for non-Swing
 * applications that runs a task as soon as it is submitted
 * and does not return until the task is complete.
 */
public class BlockingTaskManager implements TaskManager {

	@Override
	public void submit(Task task) {
		TaskAdvisor advisor = new TaskAdvisor() {
			@Override
			public boolean cancelRequested() {
				return false;
			}

			@Override
			public void setStatus(Status status) {
				// Ignore, since we are running tasks synchronously.
			}
			
		};
		
		task.execute(advisor);
	}

	@Override
	public void remove(Task task) {
		// Not applicable, since tasks run as soon as they are submitted,
		// and we don't keep track of tasks persistently.
	}

	@Override
	public Status getStatus(Task task) {
		// The task must already be complete.
		return Status.COMPLETE;
	}

	@Override
	public void cancel(Task task) {
		// Not applicable, since tasks run as soon as they are submitted,
		// and we don't keep track of tasks persistently.
	}

}
