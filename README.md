# Ricasso
An SDLC automation using Dagger

## Dagger

`brew install dagger/tap/dagger`

<!-- `dagger init` -->

`dagger init --sdk=java --name=ricasso`
### Toolchains

> Toolchains are Dagger modules that provide ready-to-use functions and checks for common development workflows. 
> Instead of importing it into your code, you install it and use its functions directly via dagger call or dagger check.

> You can install toolchains from:
> 
> GitHub repositories: github.com/user/repo/path
> Local paths: ./path/to/toolchain or /absolute/path
> Git URLs: Any valid Git URL with optional version tags

### Checks
> A check is a function that validates your code without requiring any arguments.
> Checks can accept optional arguments to customize behavior, such as filtering which tests to run or adjusting validation strictness.

> Checks can return an error (for pass/fail validation) or a container (for tools that produce exit codes).

### Functions

> They're units of computation that accept inputs, perform operations (typically in containers), and return outputs.
> Functions can be combined together to create complex workflows.


### Lazy Work
Dagger can describe work before it runs it.

A function can return a Container, Directory, File, service, object, or changeset. That value can keep flowing through the graph.

Dagger runs work when the caller needs a concrete result: a string, exported file, synced container, applied changeset, live service, or check status.

Return composable values until the user needs a final answer.

---

## Current Status

- Read up on dagger core concepts
- Create a Docker file with pi agentic harness
- Connected it to local ollama qwen
- Created ricasso java module and prompt function
- Explored multiple ways to initialise function container image
- **Mount in dagger is one way**


```
 ▶ ✔ c=$(prompt . "write a haiku about love to love.md") 0.0s
  ▶ ✔ $c |
  file /app/love.md |
  contents 0.0s
Whispers of the breeze                                                                                                                                 
Stars dance in the night's embrace                                                                                                                     
Love blooms without end                                                                                                                        ```

`init . | task "what is the capital of france" | output | export ./output`

`pi install npm:pi-web-access`

