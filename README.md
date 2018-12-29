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
This project generates a war file which can be deployed to an application server, typically Tomcat. The war file can be generated
using the `./gradlew build` command. The resulting file is placed in the `build/libs` folder

## Running with Docker
This project includes a Dockerfile which allows it to be executed in a Docker container without having to install an
application server.

```
> ./gradlew build
> docker build -t smartanalyzer .
> docker run -p 8888:8080 smartanalyzer
// Navigate to localhost:8888/pega-alertanalyzer-0.0.1-SNAPSHOT
```

## Usage

1. Deploy the war file on local application server viz. Tomcat and hit the URL http://localhost:8888/pega-alertanalyzer-0.0.1-SNAPSHOT (Hostname and port may vary based upon configuration)

![image](https://user-images.githubusercontent.com/83574/50541692-04bdb280-0b79-11e9-94a4-98b2c52a4e3b.png)

2. On the next screen upload PegaRULES Alert file and select appropriate timezone.

![image](https://user-images.githubusercontent.com/83574/50541693-0a1afd00-0b79-11e9-9405-49dc63d5479c.png)

![image](https://user-images.githubusercontent.com/83574/50541695-0dae8400-0b79-11e9-9ff3-cd96fb353fab.png)

3. You’ll be presented with a dashboard with Summary, Recommendation, Key indicators.

![image](https://user-images.githubusercontent.com/83574/50541696-11420b00-0b79-11e9-85c1-106c018c2c2d.png)

4. Drilldown into various categories and explore the aggregated data to your advantage.

![image](https://user-images.githubusercontent.com/83574/50541698-14d59200-0b79-11e9-99c4-099d94c7e7f1.png)

![image](https://user-images.githubusercontent.com/83574/50541690-f1124c00-0b78-11e9-8371-92e442966456.png)
