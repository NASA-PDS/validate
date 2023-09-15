package gov.nasa.pds.validate.ri;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

class CountingAppender implements Appender {
  private int err = 0, fatal = 0, warn = 0;
  private ErrorHandler handler = null;
  private Filter filter = null;
  private Layout layout = null;
  private String name = "";

  public void addFilter(Filter newFilter) {
    this.filter = newFilter;
  }

  public Filter getFilter() {
    return this.filter;
  }

  public void clearFilters() {
    this.filter = null;
  }

  public void close() {}

  public void append(LogEvent event) {
    Level level = event.getLevel();
    if (Level.ERROR.equals(level))
      this.err++;
    if (Level.FATAL.equals(level))
      this.fatal++;
    if (Level.WARN.equals(level))
      this.warn++;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setHandler(ErrorHandler errorHandler) {
    this.handler = errorHandler;
  }

  @Override
  public ErrorHandler getHandler() {
    return this.handler;
  }

  @Override
  public Layout getLayout() {
    return this.layout;
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

  @Override
  public State getState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub

  }

  @Override
  public void start() {
    // TODO Auto-generated method stub

  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isStarted() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isStopped() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean ignoreExceptions() {
    // TODO Auto-generated method stub
    return false;
  }
}
