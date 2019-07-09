package gov.nasa.pds.tools.web.ui.utils;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.web.ui.containers.ExceptionContainer;
import gov.nasa.pds.tools.web.ui.containers.VolumeContainer;
import gov.nasa.pds.tools.web.ui.containers.dataSet.ValidationResults.NewValue;
import gov.nasa.pds.tools.web.ui.containers.tabularData.Column;

import java.util.Comparator;

public class Comparators {

	public static final Comparator<String> CASE_INSENSITIVE_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String left, String right) {
			// null goes to bottom when doing ascending order
			Integer nullVal = nullCompare(left, right);
			if (nullVal != null) {
				return nullVal;
			}

			// try to do numeric compare
			final Number leftNum = StrUtils.getNumber(left);
			final Number rightNum = StrUtils.getNumber(right);
			if (leftNum != null && rightNum != null) {
				return NUMERIC_COMPARATOR.compare(leftNum, rightNum);
			}

			// do a normal compare
			return String.CASE_INSENSITIVE_ORDER.compare(left, right);
		}
	};

	// note: something may exist that accomplishes this
	public static final Comparator<Number> NUMERIC_COMPARATOR = new Comparator<Number>() {
		@Override
		public int compare(Number left, Number right) {
			// push null to the bottom
			Integer nullVal = nullCompare(left, right);
			if (nullVal != null) {
				return nullVal;
			}

			if (left.equals(right)) {
				return 0;
			} else if (left.doubleValue() > right.doubleValue()) {
				return 1;
			} else {
				return -1;
			}
		}
	};

	public final static Comparator<Column> TABULAR_COLUMN_COMPARATOR = new Comparator<Column>() {
		@Override
		public int compare(Column left, Column right) {
			return NUMERIC_COMPARATOR
					.compare(left.getIndex(), right.getIndex());
		}
	};

	public final static Comparator<Column> TABULAR_COLUMN_NAME_COMPARATOR = new Comparator<Column>() {
		@Override
		public int compare(Column left, Column right) {
			return CASE_INSENSITIVE_COMPARATOR.compare(left.getName(), right
					.getName());
		}
	};

	public final static Comparator<Column> TABULAR_COLUMN_DATATYPE_COMPARATOR = new Comparator<Column>() {
		@Override
		public int compare(Column left, Column right) {
			return CASE_INSENSITIVE_COMPARATOR.compare(left.getDataType(),
					right.getDataType());
		}
	};

	public final static Comparator<NewValue> NEW_VALUE_COMPARATOR = new Comparator<NewValue>() {
		@Override
		public int compare(NewValue left, NewValue right) {
			return CASE_INSENSITIVE_COMPARATOR.compare(left.getKey(), right
					.getKey());
		}
	};

	public final static Comparator<VolumeContainer> VOLUME_CONTAINER_COMPARATOR = new Comparator<VolumeContainer>() {
		@Override
		public int compare(final VolumeContainer left,
				final VolumeContainer right) {
			return CASE_INSENSITIVE_COMPARATOR.compare(left.getVolId(), right
					.getVolId());
		}
	};

	public final static Comparator<ExceptionContainer> EXCEPTION_CONTAINER_COMPARATOR = new Comparator<ExceptionContainer>() {
		@Override
		public int compare(final ExceptionContainer left,
				final ExceptionContainer right) {
			// NOTE: compare is reversed
			return NUMERIC_COMPARATOR.compare(right.getId(), left.getId());
		}
	};

	public static Integer nullCompare(Object left, Object right) {
		if (left == null) {
			if (right == null) {
				return 0;
			}
			return 1;
		} else if (right == null) {
			return -1;
		}
		return null;
	}

}
