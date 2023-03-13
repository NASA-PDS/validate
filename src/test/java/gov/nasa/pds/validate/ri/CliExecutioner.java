package gov.nasa.pds.validate.ri;

import org.junit.jupiter.api.function.Executable;

public class CliExecutioner implements Executable {
  final private CommandLineInterface cli;
  final private String[] args;
  public CliExecutioner(String[] args) {
    this.args = args;
    this.cli = new CommandLineInterface();
  }
  @Override
  public void execute() throws Throwable {
    this.cli.process(this.args); 
  }
  public CommandLineInterface getCli() {
    return cli;
  }
}
