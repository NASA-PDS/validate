package gov.nasa.pds.web.ui.containers;

import gov.nasa.arc.pds.tools.util.StrUtils;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: build rendering rules into this? ie, if numeric, do number formatting?
public class RowContainer extends ArrayList<Object> implements
		BaseContainerInterface {

	private static final long serialVersionUID = 1L;

	public RowContainer(final Object... objects) {
		this.addAll(Arrays.asList(objects));
	}

	@Override
	public String toString() {
		return StrUtils.toSeparatedString(this);
	}
}
