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

`$c | task "what is the current project in your working directory about?" | container | combined-output`

```
[dagger x-release] running dagger from v1.0.0-beta.10; using release v1.0.0-beta.10
▶ ✔ connect 0.2s
▶ ✔ detect module: . 1.2s
  ▶ ✔ init_c=$(init .) 0.2s
  ● ✔ intro=$($init_c | task "I am Sharib") 0.0s
  ▶ ✔ $intro | container | combined-output 34.5s
Hello, Sharib! Welcome. How can I assist you today? 😊
  ● ✔ intro=$($init_c | task "I am Sharib") 0.0s
  ● ✔ intro=$($intro | followup "who am I") 0.0s
  ▶ ✔ $intro | container | combined-output 5.8s
You are Sharib! 😊 How can I assist you today?
  ● ✔ weather=$($intro | task "who am I") 0.0s
  ▶ ✔ $weather | container | combined-output 11.6s
I don't have access to your personal information or account details. If you're asking about your identity in the context of using this system, you would need to check your account settings or login information if
ou're logged into a service. Would you like help with something else?
  ● ✔ weather=$($weather | task "I am jeff") 0.0s
  ▶ ✔ $weather | container | combined-output 6.4s
Hello, Jeff! How can I assist you today? If you have any questions or need help with something, feel free to let me know! 😊
  ● ✔ weather=$($weather | followup "who am I") 0.0s
  ▶ ✔ $weather | container | combined-output 6.5s
You are Jeff! 😊 How can I assist you today? If you have any questions or need help with something, feel free to let me know!

```

<!-- Initialise -->
init_c=$(ricasso | init .)

<!-- Copy to self reference later -->
context=$init_c

<!-- Start a new task -->
context=$($context | task "gather all context for this project")

<!-- Trigger lazy execution -->
$context | container | combined-output

<!-- Start a followup tas -->
context=$($context | followup "the project is in the current directory. go through it to gather any and all required context")

<!-- Trigger lazy execution again-->
$context | container | combined-output


----