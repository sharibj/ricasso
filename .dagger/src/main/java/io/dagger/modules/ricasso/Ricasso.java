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
  private static final  List<String> HARNESS_EXEC = List.of("pi", "-a", "-p");
  private static final  List<String> HARNESS_CONTINUE = List.of("pi", "-a", "-p", "--continue");
  private static final String WORKING_DIR = "/app";

  public Ricasso() {
    this.container = this.container = dag().container().from("jafarisharib/pi-harness");
  }

  @Function(description="Initialise the container with source directory.")
  public Ricasso init(Directory directoryArg){
    this.container = this.container 
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
          WORKING_DIR,
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
      .withPrompt("""
          You are the orchestration agent for an autonomous coding harness.

          Your responsibility is NOT to perform the coding task yourself.
          Your responsibility is to drive the available agentic harness tools
          until the requested job is completely resolved.

          You have access to the current Ricasso module as tools.

          IMPORTANT TOOL POLICY

          - Use `task(prompt)` to start execution of the requested job.
          - Use `followup(prompt)` to continue, correct, verify, or finish work
            performed by the harness.
          - Do NOT call `ask`; that would recursively invoke yourself.
          - Do NOT directly edit files as the primary way of completing the job.
          - Do NOT stop merely because the first `task` invocation returned.
          - Treat the harness as the implementation agent and yourself as its
            supervisor/orchestrator.

          EXECUTION STRATEGY

          1. Understand the requested job and its success criteria.

          2. Call `task` with a clear, complete implementation request.
             Include relevant requirements and tell the harness to inspect the
             existing repository before making changes.

          3. Evaluate whether the work is actually complete.

          4. If anything remains incomplete, uncertain, broken, untested, or
             inconsistent with the requested job, call `followup` with concrete
             instructions describing what still needs to be done.

          5. Continue calling `followup` as many times as necessary to reach a
             fully resolved result.

          6. Make the harness verify its own work. Depending on the repository,
             this can include:
             - inspecting changed files,
             - compiling/building,
             - running tests,
             - running linters/type checks,
             - fixing failures,
             - checking that the requested behavior is actually implemented.

          7. Do not accept statements such as "this should work" when the result
             can reasonably be verified. Ask the harness to perform the
             verification.

          8. When failures occur, use `followup` to provide the failure context
             and require the harness to diagnose and fix the underlying problem.

          COMPLETION CRITERIA

          The job is complete only when:
          - all requested changes have been implemented,
          - obvious related issues introduced by the changes are resolved,
          - appropriate verification has succeeded,
          - no known required work remains.

          Avoid unnecessary unrelated refactoring.

          STATE

          The Ricasso object returned by `task` / `followup` contains the updated
          container. Continue operating on that updated object so that every
          follow-up sees all previous changes.

          FINAL OUTPUT

          Once the job is fully complete, return the final underlying Container
          through the declared environment output named `result`.

          Do not return an intermediate container.
          Do not finish before the harness has completed and verified the job.
          """)

      // Actual job.
      .withPrompt("""
          Job to complete:

          $job
          """)
      .loop();

  this.container = llm.env().output("result").asContainer();
  return this;
}

  private Ricasso append_and_execute(List<String> command, String prompt) {
     List<String> execCommand = new ArrayList<>(command);
     String scopedPrompt = """
      WORKSPACE CONSTRAINT

      Your project working directory is %s.

      Perform all project inspection and modification exclusively within this
      directory.

      Do not inspect, modify, or rely on project files outside this directory.
      Do not invent directories, files, architecture, or repository structure.
      Before describing the project, inspect the actual contents of the working
      directory and base your conclusions only on what exists there.

      User task:

      %s
      """.formatted(WORKING_DIR, prompt);

  execCommand.add(scopedPrompt);

    this.container = this
    .container
    .withExec(execCommand);
    return this;
  }
}
