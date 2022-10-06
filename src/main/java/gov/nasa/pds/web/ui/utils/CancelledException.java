package gov.nasa.pds.web.ui.utils;

/**
 * Action cancelled event. Used in processes spun off into separate threads so as to inform the
 * launching thread that the process was cancelled.
 *
 * @author jagander
 */
public class CancelledException extends Exception {

  private static final long serialVersionUID = 1L;

}
