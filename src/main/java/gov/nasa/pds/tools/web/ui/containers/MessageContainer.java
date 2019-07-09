package gov.nasa.pds.tools.web.ui.containers;

// considering just storing key rather than message but may unnecessarily
// complicate things
public class MessageContainer {

	// optional foreign id, often useful in batch processes where you need to
	// differentiate by items
	private String id;

	// message code
	private int code;

	// message
	private String message;

	// error/warning/notice
	private int type = -1;

	public MessageContainer(final int code, final int type,
			final String message, final String id) {
		this.code = code;
		this.type = type;
		this.message = message;
		this.id = id;
	}

	public MessageContainer(final String message) {
		this.message = message;
	}

	public String getId() {
		return this.id;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public int getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.message;
	}

}
