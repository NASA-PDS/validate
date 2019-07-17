package gov.nasa.pds.web.ui.containers;

public class ExceptionContainer implements BaseContainerInterface {

	private final String date;

	private final String targetUrl;

	private final String message;

	private final String stack;

	private final int id;

	public ExceptionContainer(final int id, final String date,
			final String targetUrl, final String message, final String stack) {
		this.stack = stack;
		this.id = id;
		this.date = date;
		this.targetUrl = targetUrl;
		this.message = message;
	}

	public ExceptionContainer(final int id, final String date,
			final String targetUrl, final String message) {
		this(id, date, targetUrl, message, null);
	}

	public int getId() {
		return this.id;
	}

	public String getDate() {
		return this.date;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	public String getMessage() {
		return this.message;
	}

	public String getStack() {
		return this.stack;
	}

}
