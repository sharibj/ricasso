package io.dagger.modules.ricasso;

import io.dagger.client.Container;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

@Object
public class ExecResult {

  private Container container;
  private String output;

  public ExecResult() {
    // Required by Dagger
  }

  public ExecResult(Container container, String output) {
    this.container = container;
    this.output = output;
  }

  @Function
  public Container container() {
    return container;
  }

  @Function
  public String output() {
    return output;
  }
}