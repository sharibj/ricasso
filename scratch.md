init | base | terminal

init --source-path="runtime" | base | terminal



  ricasso-python dagger init --sdk=python --name=ricasso
[dagger x-release] running dagger from v0.21.4; using release v0.21.4
✔ connect 0.2s

✔ moduleSource(refString: ".", disableFindUp: true, allowNotExists: true, requireKind: LOCAL_SOURCE): ModuleSource! 0.0s
✔ .configExists: Boolean! 0.0s

✔ ModuleSource.localContextDirectoryPath: String! 0.0s

✔ ModuleSource.sourceRootSubpath: String! 0.0s

✔ ModuleSource.withName(name: "ricasso"): ModuleSource! 0.0s
✔ .withSDK(source: "python"): ModuleSource! 3.6s
✔ .withSourceSubpath(path: "."): ModuleSource! 0.0s
✔ .withEngineVersion(version: "latest"): ModuleSource! 0.0s
✔ .generatedContextDirectory: Directory! 6.1s
✔ .export(path: "/Users/i768344/tmp/ricasso-python"): String! 0.1s

14:15:42 WRN no LICENSE file found; generating one for you, feel free to change or remove license=Apache-2.0
Initialized module ricasso in /Users/i768344/tmp/ricasso-python

A new release of dagger is available: v0.21.4 → v0.21.7
To upgrade, see https://docs.dagger.io/install
https://github.com/dagger/dagger/releases/tag/v0.21.7
➜  ricasso-python

