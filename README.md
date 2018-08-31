# Clojure CLI + `figwheel.main` + `devcards` with `:extra-main-files`

## Clojure setup with the Nix package manager

First to have a more reproducible Clojure environment across macOS and Linux,
without polluting your host OS, we can harness the power of the
[Nix package manager](https://nixos.org/nix/), so go and install that first.

Then, if we create a `shell.nix` file:

```nix
with import <nixpkgs> {};
mkShell {
    buildInputs = [ clojure ];
}
```

and run
```bash
nix-shell
```

it will download the latest stable Java environment and Clojure CLI tools and open a
`bash` process where `clojure` and `clj` are available:

```
[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj
Clojure 1.9.0
user=>
```

To upgrade your environment you can update your system
[channel](https://nixos.org/nixpkgs/download.html):

```bash
sudo -i nix-channel --update
```

Then restarting your `nix-shell` process should download newer versions
of Clojure and its dependencies.

## Clojure/ClojureScript setup

```bash
echo '{:deps {org.clojure/clojure       {:mvn/version "1.9.0"}' > deps.edn
echo '        org.clojure/clojurescript {:mvn/version "1.10.339"}}}' >> deps.edn
```

The version number of the latest ClojureScript an be obtained from
https://github.com/clojure/clojurescript/blob/master/changes.md

Latest Clojure version can be found at
[https://search.maven.org/search?q=g:org.clojure%20AND%20a:clojure&core=gav]

Download and verify that we indeed run the right dependencies:

```
[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj -e '*clojure-version*'
{:major 1, :minor 9, :incremental 0, :qualifier nil}

[nix-shell:~/github.com/onetom/clj-figwheel-main-devcards]$ clj -m cljs.main
ClojureScript 1.10.339
cljs.user=> *clojurescript-version*
"1.10.339"
```

## `figwheel.main` for automatic ClojureScript recompilation, hot-reload and REPL

Detailed instructions for various environments can be found via
https://figwheel.org, but here come the specifics for our setup.

Add `com.bhauman/figwheel-main {:mvn/version "0.1.8"}` to `deps.edn`
and start a ClojureScript REPL using an alias for convenience:

```edn
{:deps    {org.clojure/clojure       {:mvn/version "1.9.0"}
           org.clojure/clojurescript {:mvn/version "1.10.339"}
           com.bhauman/figwheel-main {:mvn/version "0.1.8"}}
 :aliases {:dev {:main-opts ["--main" "figwheel.main"]}}}
```

The result should be something like this:
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

and a browser window should be opened pointing to http://localhost:9500.

In an actual application we want to have our custom `index.html` and
hot-reloading `app.css` and we also want to control which namespace should
kickstart our application. To achieve this we can create a `dev.cljs.edn`
configuration file, containing
[ClojureScript compiler options](http://www.clojurescript.org/reference/compiler-options).
We can tell `figwheel.main` to use these compiler settings by appending
the `--build dev` command-line option to the `:main-opts` of our `:dev` alias.

`dev.cljs.edn`:

```edn
^{:watch-dirs ["src" "test"]
  :css-dirs   ["resources/public"]}
{:main app.core}
```

`deps.edn`:

```edn
{:deps    {org.clojure/clojure       {:mvn/version "1.9.0"}
           org.clojure/clojurescript {:mvn/version "1.10.339"}
           com.bhauman/figwheel-main {:mvn/version "0.1.8"}}
 :aliases {:dev {:main-opts ["--main" "figwheel.main" "--build" "dev"]}}}
```

The `<body>` of `index.html`:

```html
<div id="app"> </div>
<script src="cljs-out/dev-main.js"></script>
```


## `devcards`, the "interactive visual REPL"

Documentation and source code: https://github.com/bhauman/devcards

Add `devcards {:mvn/version "0.2.5"}}` to the `deps.edn`.

Modify the `dev.cljs.edn` to signal figwheel the presence of `devcards`
and also use the `:extra-main-files` option to define an additional build
and call it `cards`.

```edn
^{:watch-dirs       ["src" "test"]
  :css-dirs         ["resources/public"]
  :extra-main-files {:cards {:main app.cards}}}
{:main     app.core
 :devcards true}
```

We also need an main entry point for devcards, where we can initialize devcards.
To this end we can create an `app.cards` namespace:

```clojure
(ns app.cards
  (:require
    [devcards.core]
    ; Load namespaces with `defcard` or `deftest` definitions
    [app.core]))

(devcards.core/start-devcard-ui!)
```

Restarting our `clj -A:dev` process and it should download the `devcards`
dependency and build an extra entry-point for devcards.

There is an auto-generated HTML page hosting this devcards entry point under
http://localhost:9500/figwheel-extra-main/cards by default, as documented here:
https://figwheel.org/docs/extra_mains.html.

## Troubleshooting

If something doesn't work as expected, make sure you check the browser console,
because it's likely to contain errors.
