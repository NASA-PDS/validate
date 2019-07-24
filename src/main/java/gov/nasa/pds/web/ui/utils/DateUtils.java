package gov.nasa.pds.web.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	public static final SimpleDateFormat MYSQL_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.US); //$NON-NLS-1$

	// millisecond values for common time elements
	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;
	public static final int DAY = 24 * HOUR;

	@SuppressWarnings("nls")
	private static SimpleDateFormat[] formats = {
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH'Z'"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH"),
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyy-MM"),

			// Must have day-of-year formats after formats with month,
			// so that yyyy-mm is not misinterpreted as a day-of-year.
			new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss.SSS'Z'"),
			new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss.SSS"),
			new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss'Z'"),
			new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss"),
			new SimpleDateFormat("yyyy-DDD'T'HH:mm'Z'"),
			new SimpleDateFormat("yyyy-DDD'T'HH:mm"),
			new SimpleDateFormat("yyyy-DDD'T'HH'Z'"),
			new SimpleDateFormat("yyyy-DDD'T'HH"),
			new SimpleDateFormat("yyyy-DDD") };

	static {
		for (int i = 0; i < formats.length; ++i) {
			formats[i].setLenient(false);
		}
	}

	public static java.sql.Timestamp toSQLTimeStamp(final Date date) {
		final String formattedDate = MYSQL_DATE_FORMAT.format(date);
		return java.sql.Timestamp.valueOf(formattedDate);
	}

	public static long toEpochDate(final String pdsDate) throws Exception {
		// take PDS date string and return int representation equivalent to UNIX
		// epoch time *10000 to hold milliseconds
		Date date = null;
		date = DateUtils.lenientParse(pdsDate);
		return date.getTime();
	}

	protected static Date lenientParse(final String dateTime) throws Exception {
		for (int i = 0; i < formats.length; ++i) {
			try {
				Date d = formats[i].parse(dateTime);
				return d;
			} catch (ParseException ex) {
				// ignore
			} catch (NumberFormatException ex) {
				// ignore
			}
		}

		// If we get here, no format matched.
		throw new RuntimeException("Not a recognized date/time format."); //$NON-NLS-1$
	}

	/*
	 * tests string value to find a pattern in DateUtils.formats that matches,
	 * if none found returns null
	 * 
	 * @param dateTime the date string to test
	 * 
	 * @return String the pds date pattern it matched, or null if none found
	 */
	public static String getPattern(final String dateTime) {
		for (int i = 0; i < formats.length; ++i) {
			try {
				@SuppressWarnings("unused")
				Date d = formats[i].parse(dateTime);
				return formats[i].toPattern();
			} catch (ParseException ex) {
				// ignore
			} catch (NumberFormatException ex) {
				// ignore
			}

		}

		// If we get here, no format matched.
		//throw new RuntimeException("Not a recognized date/time format."); //$NON-NLS-1$
		return null;
	}

	// convert milliseconds into HH:MM:SS
	// TODO: allow more formatting options
	public static String getMillisecondsToDuration(final long milliseconds) {
		String format = String.format("%%0%dd", 2); //$NON-NLS-1$
		long totalSeconds = milliseconds / SECOND;
		String seconds = String.format(format, totalSeconds % 60);
		String minutes = String.format(format, (totalSeconds % 3600) / 60);
		String hours = String.format(format, totalSeconds / 3600);
		String time = hours + ":" + minutes + ":" + seconds; //$NON-NLS-1$ //$NON-NLS-2$
		return time;
	}

	public static String toGMTString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US); //$NON-NLS-1$
		TimeZone tz = TimeZone.getTimeZone("GMT"); //$NON-NLS-1$
		formatter.setTimeZone(tz);
		return formatter.format(date);
	}

}
