package io.dagger.modules.ricasso;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.Env;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import io.dagger.client.exception.DaggerQueryException;

/** Ricasso main object */
@Object
public class Ricasso {

  public Container container;
  private static final  List<String> HARNESS_EXEC = List.of("pi", "-a", "-p");
  private static final  List<String> HARNESS_CONTINUE = List.of("pi", "-a", "-p", "--continue");
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

  @Function(description="Assign a new task to the agentic harness.")
  public Ricasso task(String prompt){
    return append_and_execute(HARNESS_EXEC, prompt);
  }


  @Function(description="Continue a previous task in the agentic harness.")
  public Ricasso followup(String prompt){
    return append_and_execute(HARNESS_CONTINUE, prompt);
  }

  @Function(description = "Return underlying container.")
  public Container container() {
    return this.container;
  }

  @Function(description = "Return the modified source directory.")
  public Directory output() {
    return this.container.directory(WORKING_DIR);
  }

  @Function
  public String ask(String prompt) throws InterruptedException, ExecutionException, DaggerQueryException{
    Env env = dag()
        .env();
    return dag()
        .llm()
        .withEnv(env)
        .withPrompt(prompt)
        .loop()
        .lastReply();
  }


  private Ricasso append_and_execute(List<String> command, String postfix) {
     List<String> execCommand = new ArrayList<>(command);
    execCommand.add(postfix);
    this.container = this.container
    .withExec(execCommand);
    return this;
  }
}
