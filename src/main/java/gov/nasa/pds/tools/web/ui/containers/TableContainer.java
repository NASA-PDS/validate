package gov.nasa.pds.tools.web.ui.containers;

import java.util.ArrayList;
import java.util.List;

public class TableContainer implements BaseContainerInterface {

	// TODO: columns?
	// TODO: determine if headers to be associated with columns or just the
	// table and hope order is correct
	// TODO: determine if we need to have a table at all or if a collection of
	// rows is fine
	// TODO: eventually have a tag that renders this?

	private String title;

	private List<RowContainer> rows = new ArrayList<RowContainer>();

	// TODO: should this just be a RowContainer?
	private RowContainer header;

	public TableContainer() {
		// noop
	}

	public TableContainer(final String title) {
		this.title = title;
	}

	public TableContainer(final String title, final RowContainer header) {
		this.title = title;
		this.header = header;
	}

	public void addRow(final Object... objects) {
		final RowContainer row = new RowContainer(objects);
		addRow(row);
	}

	public void addRow(final RowContainer row) {
		this.rows.add(row);
	}

	public void addRows(final List<RowContainer> newRows) {
		this.rows.addAll(newRows);
	}

	public void setHeader(final RowContainer header) {
		this.header = header;
	}

	public String getTitle() {
		return this.title;
	}

	public List<RowContainer> getRows() {
		return this.rows;
	}

	public RowContainer getHeader() {
		return this.header;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return this.title + " " + this.rows.size() + " rows";
	}

}
