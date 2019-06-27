package gov.nasa.pds.web.ui.constants;

import java.util.EnumSet;

@SuppressWarnings("nls")
public class TabularManagementConstants {
	public static final String TABULARDATA_TABLE_PREFIX = "tabdata";

	public enum FileType {
		TAB("enterDownloadFormat.fileType.tab", "tab", "text/plain", "ascii"), //
		// enterDownloadFormat.fileType.tab = fixed width - PDS table
		CSV("enterDownloadFormat.fileType.csv", "csv", "text/csv", "both"); //
		// enterDownloadFormat.fileType.csv = comma separated - CSV

		private final String key;

		private final String extension;

		private final String contentType;

		private final String application;

		private FileType(final String key, final String extension,
				final String contentType, final String application) {
			this.key = key;
			this.extension = extension;
			this.contentType = contentType;
			this.application = application;
		}

		public String getKey() {
			return this.key;
		}

		public String getExtension() {
			return this.extension;
		}

		public String getContentType() {
			return this.contentType;
		}

		public String getApplication() {
			return this.application;
		}
	}

	public enum ConditionType {
		// LAB 04/12/10 stored in db as char(10)
		STRING, NUMERIC, BOOLEAN, DATE_TIME, ORDER
	}

	public enum Condition {
		// LAB 04/12/10 stored in db as char(20)
		// BETWEEN ... AND ... For numbers and dates

		// IS_NULL
		// IS_NOT_NULL

		ASC("orderRows.option.orderAscending", ConditionType.ORDER,
				"`column` ASC"), //
		DESC("orderRows.option.orderDescending", ConditionType.ORDER,
				"`column` DESC"), //
		IS("selectRows.option.is", ConditionType.STRING,
				"trim(`column`) = trim('value')"), //
		IS_NOT("selectRows.option.isNot", ConditionType.STRING,
				"trim(`column`) != trim('value')"), //
		CONTAINS("selectRows.option.contains", ConditionType.STRING,
				"`column` LIKE '%value%'"), //
		DOES_NOT_CONTAIN("selectRows.option.notContains", ConditionType.STRING,
				"`column` NOT LIKE '%value%'"), // 
		STARTS_WITH("selectRows.option.startsWith", ConditionType.STRING,
				"`column` LIKE 'value%'"), // 
		DOES_NOT_START_WITH("selectRows.option.doesNotStartWith",
				ConditionType.STRING, "`column` NOT LIKE 'value%'"), // 
		ENDS_WITH("selectRows.option.endsWith", ConditionType.STRING,
				"`column` LIKE '%value'"), //
		DOES_NOT_END_WITH("selectRows.option.doesNotEndWith",
				ConditionType.STRING, "`column` NOT LIKE '%value'"), //
		EQUALS("selectRows.option.equals", ConditionType.NUMERIC,
				"`column` = value"), //
		DOES_NOT_EQUAL("selectRows.option.notEquals", ConditionType.NUMERIC,
				"`column` != value"), //
		GREATER_THAN("selectRows.option.greaterThan", ConditionType.NUMERIC,
				"`column` > value"), //
		LESS_THAN("selectRows.option.lessThan", ConditionType.NUMERIC,
				"`column` < value"), //
		IS_TRUE("selectRows.option.true", ConditionType.BOOLEAN,
				"`column` IS TRUE"), //
		IS_FALSE("selectRows.option.false", ConditionType.BOOLEAN,
				"`column` IS FALSE"), //
		IS_BEFORE("selectRows.option.isBefore", ConditionType.DATE_TIME,
				"`column` < value"), //
		IS_AFTER("selectRows.option.isAfter", ConditionType.DATE_TIME,
				"`column` > value"), //
		DATE_IS("selectRows.option.dateIs", ConditionType.DATE_TIME,
				"`column` = value"), //
		DATE_IS_NOT("selectRows.option.dateIsNot", ConditionType.DATE_TIME,
				"`column` != value"), ;//

		private final String key;

		private ConditionType type;

		private final String mySqlPattern;

		private Condition(final String key, ConditionType type,
				final String mySqlPattern) {
			this.key = key;
			this.type = type;
			this.mySqlPattern = mySqlPattern;

		}

		public String getKey() {
			return this.key;
		}

		public ConditionType getType() {
			return this.type;
		}

		public String getMySqlPattern() {
			return this.mySqlPattern;
		}

	}

	// TODO more elegant way to do this?
	public static EnumSet<Condition> stringGroup;
	public static EnumSet<Condition> numericGroup;
	public static EnumSet<Condition> booleanGroup;
	public static EnumSet<Condition> dateTimeGroup;
	public static EnumSet<Condition> orderGroup;

	// Initialize condition groups
	static {
		for (Condition condition : Condition.values()) {
			if (condition.getType().equals(ConditionType.STRING)) {
				TabularManagementConstants.stringGroup.add(condition);
			}
			if (condition.getType().equals(ConditionType.NUMERIC)) {
				TabularManagementConstants.numericGroup.add(condition);
			}
			if (condition.getType().equals(ConditionType.BOOLEAN)) {
				TabularManagementConstants.booleanGroup.add(condition);
			}
			if (condition.getType().equals(ConditionType.DATE_TIME)) {
				TabularManagementConstants.dateTimeGroup.add(condition);
			}
			if (condition.getType().equals(ConditionType.ORDER)) {
				TabularManagementConstants.orderGroup.add(condition);
			}

		}
	}

}
