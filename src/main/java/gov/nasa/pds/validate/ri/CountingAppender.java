package gov.nasa.pds.validate.ri;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

class CountingAppender implements Appender {
  private int err = 0, fatal = 0, warn = 0;
  private ErrorHandler handler = null;
  private Filter filter = null;
  private Layout layout = null;
  private String name = "";
  @Override
  public void addFilter(Filter newFilter) {
    this.filter = newFilter;
  }
  @Override
  public Filter getFilter() {
    return this.filter;
  }
  @Override
  public void clearFilters() {
    this.filter = null;
  }
  @Override
  public void close() {
  }
  @Override
  public void doAppend(LoggingEvent event) {
    Level level = event.getLevel();
    if (Level.ERROR.equals(level)) this.err++;
    if (Level.FATAL.equals(level)) this.fatal++;
    if (Level.WARN.equals(level)) this.warn++;
  }
  @Override
  public String getName() {
    return this.name;
  }
  @Override
  public void setErrorHandler(ErrorHandler errorHandler) {
    this.handler = errorHandler;
  }
  @Override
  public ErrorHandler getErrorHandler() {
    return this.handler;
  }
  @Override
  public void setLayout(Layout layout) {
    this.layout = layout;
  }
  @Override
  public Layout getLayout() {
    return this.layout;
  }
  @Override
  public void setName(String name) {
    this.name = name;
  }
  @Override
  public boolean requiresLayout() {
    return false;
  }
  int getNumberOfErrors() {
    return this.err;
  }
  int getNumberOfFatals() {
    return this.fatal;
  }
  int getNumberOfWarnings() {
    return this.warn;
  }
}
