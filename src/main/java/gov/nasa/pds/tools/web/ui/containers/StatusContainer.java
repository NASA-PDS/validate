package gov.nasa.pds.tools.web.ui.containers;

public class StatusContainer {

	private int step = 0;

	private int numSteps = 0;

	private String messageKey;

	private boolean cancelled = false;

	private boolean done = false;

	// some process is running that should prevent you from moving on to use the
	// assembled data
	private boolean blocked = false;

	// is the change major? used to determine if status should be pushed to page
	private boolean major = true;

	public StatusContainer() {
		// noop
	}

	public StatusContainer(final int numSteps) {
		this.numSteps = numSteps;
	}

	public void setStatus(final String key) {
		this.messageKey = key;
	}

	public int incrementStep() {
		this.step++;
		this.major = true;
		return this.step;
	}

	public int getStep() {
		return this.step;
	}

	public int getNumSteps() {
		return this.numSteps;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	public void setCancelled() {
		this.cancelled = true;
	}

	public boolean isCancelled() {
		return this.cancelled || Thread.interrupted();
	}

	public boolean isDone() {
		return this.done && this.blocked == false;
	}

	public void setDone() {
		this.done = true;
	}

	public void seen() {
		this.major = false;
	}

	public boolean isMajor() {
		return this.major;
	}

	public void setBlocked(final boolean blocked) {
		this.blocked = blocked;
	}

	public boolean getBlocked() {
		return this.blocked;
	}
}
