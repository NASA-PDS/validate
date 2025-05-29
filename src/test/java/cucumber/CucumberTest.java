package cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty, summary", "html:target/cucumber.html"},
    features = "src/test/resources/features/", glue = "cucumber")
public class CucumberTest {
  public CucumberTest() {}
}
