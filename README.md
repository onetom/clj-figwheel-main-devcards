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
