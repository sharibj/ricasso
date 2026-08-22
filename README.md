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

## Steps

### Install
`dagger install https://github.com/sharibj/ricasso.git`

### Run
`dagger shell`

`c=$(ricasso | init .)`

`$c | task "what is the current project in your working directory about?" | container | stdout`