A sample project for passing system environment variable to the jvm

```
gradle run -DGapp=path/to/credential.json --args "path/to/audiofilename"
```

For debugging, add `--info` or `--scan` to `gradle run` command