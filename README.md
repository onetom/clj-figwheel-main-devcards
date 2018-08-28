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
