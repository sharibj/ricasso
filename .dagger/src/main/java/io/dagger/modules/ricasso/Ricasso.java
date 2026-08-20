package io.dagger.modules.ricasso;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.exception.DaggerQueryException;
import io.dagger.client.Directory;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Ricasso main object */
@Object
public class Ricasso {
  /** Returns a container that prompts to pi */
  @Function
  public Container prompt(Directory directoryArg, String prompt) {
    // return dag().container()
    // .importTarball(directoryArg.file("pi-agent.tar")) // works
    // dag().container().from("pi-agent:latest") // doesn't work
    return directoryArg.dockerBuild()
    .withMountedDirectory("/app", directoryArg)
    .withWorkdir("/app")
    .withExec(List.of("pi", "-p" , prompt));
  }

  /** Returns lines that match a pattern in the files of the provided Directory */
  @Function
  public String grepDir(Directory directoryArg, String pattern)
      throws InterruptedException, ExecutionException, DaggerQueryException {
    return dag()
        .container()
        .from("alpine:latest")
        .withMountedDirectory("/mnt", directoryArg)
        .withWorkdir("/mnt")
        .withExec(List.of("grep", "-R", pattern, "."))
        .stdout();
  }
}
