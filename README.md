# Introduction

whaletunnel-benchmark is a benchmark tool for whaletunnel products include whalescheduler, whaletunnel.

# whaletunnel-benchmark-cli

whaletunnel-benchmark-cli is a command line tool for whaletunnel-benchmark.

whaletunnel-benchmark-cli is powered by spring-shell, more detail about how to build shell by spring you can see https://docs.spring.io/spring-shell/docs/3.1.1/docs/index.html.

## How to use

### Build Project

You need to install jdk 17+ and maven 3.6+.

```shell
mvn clean package.
```

After build success, you can find the jar file in target directory.

```shell
whaletunnel-benchmark-cli/target/whaletunnel-benchmark-cli-1.0-SNAPSHOT.jar
```

### run

```shell
java -jar whaletunnel-benchmark-cli-1.0.0-SNAPSHOT.jar
```

### hello

```shell
hello --n whaletunnel
```

### Initialize env

whaletunnel-benchmark-cli dependent on some env files, so you need to initialize whaletunnel-benchmark first.
You can use below command to initialize whaletunnel-benchmark-cli.

```shell
initialize-env 
```

### exit

```shell
quit
```

