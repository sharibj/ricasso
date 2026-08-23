package io.dagger.modules.ricasso;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.Env;
import io.dagger.client.LLM;
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

  public Ricasso() {
    this.container = dag().container().from(Constants.BASE_IMAGE);
  }

  @Function(description = "Initialise the container with source directory.")
  public Ricasso init(Directory sourcePath) {
    this.container = this.container
        .withMountedDirectory(Constants.WORKING_DIR, sourcePath)
        .withWorkdir(Constants.WORKING_DIR);
    return this;
  }

  @Function(description = "Assign a new or followup task to the agentic harness.")
  public Ricasso task(String prompt) {
    List<String> execCommand = new ArrayList<>(Constants.HARNESS_EXEC);
    String scopedPrompt = Constants.TASK_PROMPT.formatted(Constants.WORKING_DIR, prompt);

    execCommand.add(scopedPrompt);

    this.container = this.container
        .withExec(execCommand);
    return this;
  }

  @Function(description = "Return underlying base container.")
  public Container base() {
    return this.container;
  }

  @Function(description = "Return the modified source directory.")
  public Directory source() {
    return this.container.directory(Constants.WORKING_DIR);
  }

  @Function(description = "Orchestrate a coding task to full completion using the agentic harness.")
  public Ricasso ask(String prompt)
      throws InterruptedException, ExecutionException, DaggerQueryException {

    Env env = dag()
        .env()
        .withCurrentModule()
        .withStringInput(
            "job",
            prompt,
            "The coding task that must be completed")
        .withStringInput(
            "workdir",
            Constants.WORKING_DIR,
            "Path to the working directory")
        .withContainerInput(
            "base",
            this.container,
            "The initial working container")
        .withContainerOutput(
            "result",
            "The final container after the task has been fully completed");

    LLM llm = dag()
        .llm()
        .withEnv(env)

        // Orchestrator role / system-like instructions.
        .withPrompt(Constants.ASK_SYSTEM_PROMPT)

        // Actual job.
        .withPrompt("""
            Job to complete:

            $job
            """)
        .loop();

    this.container = llm.env().output("result").asContainer();
    return this;
  }
}