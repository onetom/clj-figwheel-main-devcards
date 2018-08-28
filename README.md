# Clojure CLI + `figwheel.main` + `devcards` with `:extra-main-files`

## Clojure with Nix

First to have a more reproducible Clojure environment across macOS and Linux,
without polluting your host OS, we can harness the power of the
[Nix package manager](https://nixos.org/nix/), so install that first.

Then, if we create a `shell.nix` file:

```
with import <nixpkgs> {};
mkShell {
    buildInputs = [ clojure ];
}
```

and run
```
nix-shell
```

it will download a stable Java environment and Clojure CLI tools and open a
`bash` process where `clojure` and `clj` are available:

```
[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj
Clojure 1.9.0
user=>
```


## Clojure/ClojureScript

```
echo '{:deps {org.clojure/clojure       {:mvn/version "1.9.0"}' > deps.edn
echo '        org.clojure/clojurescript {:mvn/version "1.10.339"}}}' >> deps.edn
```

Latest ClojureScript version can be obtained from
https://github.com/clojure/clojurescript/blob/master/changes.md

Latest Clojure version can be found at
[https://search.maven.org/search?q=g:org.clojure%20AND%20a:clojure&core=gav]

Verify that it worked:

```
[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj -e '*clojure-version*'
{:major 1, :minor 9, :incremental 0, :qualifier nil}

[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj -m cljs.main
ClojureScript 1.10.339
cljs.user=> *clojurescript-version*
"1.10.339"
```

## figwheel.main

Detailed instructions for various environments can be found via
https://figwheel.org, but here come the specifics for our setup.

Add `com.bhauman/figwheel-main {:mvn/version "0.1.8"}` to `deps.edn`
and start a ClojureScript REPL using an alias for a convenience:

```
{:deps    {org.clojure/clojure       {:mvn/version "1.9.0"}
           org.clojure/clojurescript {:mvn/version "1.10.339"}
           com.bhauman/figwheel-main {:mvn/version "0.1.8"}}
 :aliases {:dev {:main-opts ["--main" "figwheel.main"]}}}
```

The result should be:
```
[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj -A:dev
[Figwheel] Compiling build figwheel-default-repl-build to "/var/folders/sp/dp9j0mxs3b3c_3g6sfs_7qwm0000gn/T/figwheel7528594557452178121repl/public/cljs-out/main.js"
[Figwheel] Successfully compiled build figwheel-default-repl-build to "/var/folders/sp/dp9j0mxs3b3c_3g6sfs_7qwm0000gn/T/figwheel7528594557452178121repl/public/cljs-out/main.js" in 0.705 seconds.
[Figwheel] Starting Server at http://localhost:9500
[Figwheel] Starting REPL
Prompt will show when REPL connects to evaluation environment (i.e. a REPL hosting webpage)
Figwheel Main Controls:
          (figwheel.main/stop-builds id ...)  ;; stops Figwheel autobuilder for ids
          (figwheel.main/start-builds id ...) ;; starts autobuilder focused on ids
          (figwheel.main/reset)               ;; stops, cleans, reloads config, and starts autobuilder
          (figwheel.main/build-once id ...)   ;; builds source one time
          (figwheel.main/clean id ...)        ;; deletes compiled cljs target files
          (figwheel.main/status)              ;; displays current state of system
Figwheel REPL Controls:
          (figwheel.repl/conns)               ;; displays the current connections
          (figwheel.repl/focus session-name)  ;; choose which session name to focus on
In the cljs.user ns, controls can be called without ns ie. (conns) instead of (figwheel.repl/conns)
    Docs: (doc function-name-here)
    Exit: :cljs/quit
 Results: Stored in vars *1, *2, *3, *e holds last exception object
2018-08-28 15:58:08.432:INFO::main: Logging initialized @6049ms
Opening URL http://localhost:9500
ClojureScript 1.10.339
cljs.user=>
```

and browser window being opened at http://localhost:9500.
