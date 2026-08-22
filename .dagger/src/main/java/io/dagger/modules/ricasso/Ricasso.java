package io.dagger.modules.ricasso;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

import java.util.ArrayList;
import java.util.List;

/** Ricasso main object */
@Object
public class Ricasso {

  public Container container;
  private static final  List<String> HARNESS_EXEC = List.of("pi", "-p");
  private static final String WORKING_DIR = "/app";

  public Ricasso() {
    this.container = dag().container();
  }

  @Function(description="Initialise the container with source directory.")
  public Ricasso init(Directory directoryArg){
    this.container = dag().container().from("jafarisharib/pi-harness")
    .withMountedDirectory(WORKING_DIR, directoryArg)
    .withWorkdir(WORKING_DIR);
    return this;
  }

  @Function(description="Assign a task to the agentic harness.")
  public Ricasso task(String prompt){
    List<String> execCommand = new ArrayList<>(HARNESS_EXEC);
    execCommand.add(prompt);
    this.container = this.container
    .withExec(execCommand);
    return this;
  }

  @Function(description = "Return underlying container.")
  public Container container() {
    return this.container;
  }

  @Function(description = "Return the modified source directory.")
  public Directory output() {
    return this.container.directory(WORKING_DIR);
  }
}
