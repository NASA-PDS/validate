package cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")

public class CucumberTest {
  public CucumberTest() {}
}

