
init_c=$(dagger -c 'init_c=$(init .)')
context=$init_c

context=$(dagger -c "${context} | task \"gather all context for this project\"")



run_tests=$($run_tests | followup "run all tests")
$run_tests | container | combined-output

----

init=$(ricasso | init .)
task=$($init | task "create a task.md file and write 2 lines of lorem ipsum in it" )
$task | container | terminal
follow=$task
follow=$($follow | followup "add one more line to it" )
$follow | container |terminal

---------

STATE=$(
  dagger call \
    ricasso \
    init . \
    task "create a task.md file and write 2 lines of lorem ipsum in it" \
    id
)



 dagger api call ricasso init --directoryArg=. task --prompt="create api1.md"


 TASK_ID=$(dagger call \
    ricasso \
    init . \
    task "create a task.md file and write 2 lines of lorem ipsum in it" \
    id)


    init_c=$(dagger -c 'ricasso | init . | id')


dagger api <<'EOF'
init_c=$(init .)
$init_c |
  task "write a small essay on AI at draft/ai.md"
EOF