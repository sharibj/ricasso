package io.dagger.modules.ricasso;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.Env;
import io.dagger.client.LLM;
import io.dagger.module.annotation.DefaultPath;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;
import io.dagger.module.annotation.Default;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import io.dagger.client.exception.DaggerQueryException;

/** Ricasso main object */
@Object
public class Ricasso {

  public Ricasso() {
  }

  @Function(description = "Initialise the container with source directory.")
  public Container init(@DefaultPath(".") Directory sourcePath) {
    return dag().container().from(Constants.BASE_IMAGE)
        .withMountedDirectory(Constants.WORKING_DIR, sourcePath)
        .withWorkdir(Constants.WORKING_DIR);
  }

  @Function(description = "Assign a new or followup task to the agentic harness.")
  public Container task(Container base, String prompt) {
    List<String> execCommand = new ArrayList<>(Constants.HARNESS_EXEC);
    String scopedPrompt = Constants.TASK_PROMPT.formatted(Constants.WORKING_DIR, prompt);

    execCommand.add(scopedPrompt);

    return base
        .withExec(execCommand);
  }

  @Function(description = "Return the modified source directory.")
  public Directory source(Container base) {
    return base.directory(Constants.WORKING_DIR);
  }

  // Disabled: orchestration is now driven externally by a coding harness via MCP
  // (see the ricasso-orchestrator skill). Re-add @Function to expose it again.
  // @Function(description = "Orchestrate a coding task to full completion using the agentic harness.")
  public Container ask(String prompt, @DefaultPath(".") Directory sourcePath, @Default(".wt") String wtPath)
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
            this.init(sourcePath),
            "The initial working container")
        .withContainerOutput(
            "result",
            "The final container after the task has been fully completed");

    LLM llm = dag()
        .llm()
        .withEnv(env)
        // Orchestrator role / system-like instructions.
        .withSystemPrompt(Constants.ASK_SYSTEM_PROMPT)
        .withPrompt("""
          The requested job is: 
          "
          $job.
          "
          """)
        .loop();

    Container container = llm.env().output("result").asContainer();
    String exportName = Long.toHexString(System.currentTimeMillis());
    source(container).export(wtPath + "/" + exportName);
    return container;
  }
}