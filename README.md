# pega-alertanalyzer

AlertAnalyzer is a tool that can be used to analyze PegaRULES ALert log files and render insights into performance issues

## Status

| Questions | CI | Codecov | Docs | Release |
| :---: | :---: | :---: | :---: | :---: |
| [![Stack Overflow](https://img.shields.io/badge/stack-overflow-4183C4.svg)](https://stackoverflow.com/questions/tagged/pega-alertanalyzer) | [![Build Status](https://travis-ci.org/pegasystems/pega-alertanalyzer.svg?branch=master)](https://travis-ci.org/pegasystems/pega-alertanalyzer) | [![codecov](https://codecov.io/gh/pegasystems/pega-alertanalyzer/branch/master/graph/badge.svg)](https://codecov.io/gh/pegasystems/pega-alertanalyzer) | [![Docs](https://img.shields.io/badge/docs-latest-blue.svg)](http://htmlpreview.github.io/?https://github.com/pegasystems/pega-alertanalyzer/blob/pega-alertanalyzer-gh-pages/docs/index.html) | [![pega-alertanalyzer](https://api.bintray.com/packages/pegasystems/libs-release-local/pega-alertanalyzer/images/download.svg) ](https://bintray.com/pegasystems/libs-release-local/pega-alertanalyzer/_latestVersion) |

## Latest Release

Can be sourced from Artifactory/Bintray like so:
```
<dependency>
    <groupId>com.pega.gsea</groupId>
    <artifactId>pega-alertanalyzer</artifactId>
    <version>X.Y.Z</version>
    <classifier>sources|docs</classifier> (Optional)
</dependency>
```

## Additional Resources

* [Release Process](https://github.com/pegasystems/pega-alertanalyzer/blob/master/docs/RELEASE_PROCESS.md)

## Building
This project generates a war file which can be deployed to an application server. The war file can be generated
using the `./gradlew build` command. The resulting file is placed in the `build/libs` folder

## Running with Docker
This project includes a Dockerfile which allows it to be executed in a Docker container without having to install an
application server.

```
> docker build -t smartanalyzer .
> docker run -p 8080:8888 smartanalyzer
// Navigate to localhost:8888/pega-alertanalyzer-0.0.1-SNAPSHOT
```