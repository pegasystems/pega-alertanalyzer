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

The project gets deployed as a WAR file in an application server , typically Apache Tomcat.
In order to generate the deployable war file , execute the gradle build command from the project root folder. Once the build completes , the pega-alertanalyzer-0.0.1-SNAPSHOT.war would be generated in the 
build/libs folder .In order to deploy this WAR file , one can directly drop it into the webapps folder inside Apache Tomcat or a docker image can be created and the container instance can be forked to deploy the WAR file.The docker file is placed on the root directory of the project.
