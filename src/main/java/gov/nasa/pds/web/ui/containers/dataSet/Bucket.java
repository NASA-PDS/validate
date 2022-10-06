package gov.nasa.pds.web.ui.containers.dataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import gov.nasa.pds.web.ui.containers.dataSet.ValidationResults.SimpleProblem;

public class Bucket implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final long DEFAULT_BUCKET_SIZE = 1000;

  private final long bucketSize;

  private boolean full = false;

  private List<SimpleProblem> problems = new ArrayList<>();

  public Bucket() {
    this(null);
  }

  public Bucket(final Long bucketSize) {
    this.bucketSize = bucketSize == null ? DEFAULT_BUCKET_SIZE : bucketSize;
  }

  public boolean isFull() {
    return this.full;
  }

  public boolean addProblem(final SimpleProblem problem) {
    if (this.full) {
      throw new RuntimeException("Bucket is full"); //$NON-NLS-1$
    }
    this.problems.add(problem);

    this.full = this.problems.size() >= this.bucketSize;
    return this.full;
  }

  public List<SimpleProblem> getProblems() {
    return this.problems;
  }

  /**
   * Gets a count of problems in this bucket.
   * 
   * @return the count of problems
   */
  public int getProblemCount() {
    return this.problems.size();
  }

}
