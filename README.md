# Wordish

Wordish is a JavaFX implementation based on the popular word-guessing game Wordle.

## Build and Run using JVM

Uses the GluonFX maven plugin

```bash
$ mvn gluonfx:run
```

## Build and Run using native compile / desktop

Uses the GluonFX maven plugin/GraalVM

```bash
$ mvn gluonfx:build
$ mvn gluonfx:package
$ mvn gluonfx:nativerun

```
## Build and Run on connected ios device

```bash
$ mvn -Pios gluonfx:build
$ mvn -Pios gluonfx:package
$ mvn -Pios gluonfx:install
$ mvn -Pios gluonfx:nativerun
```

## Build and Run on connected Android device

```bash
$ mvn -Pandroid gluonfx:build
$ mvn -Pandroid gluonfx:package
$ mvn -Pandroid gluonfx:install
$ mvn -Pandroid gluonfx:nativerun
```
## License
[GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/)
