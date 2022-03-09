[![GitHub license](https://img.shields.io/github/license/metaborg/common)](https://github.com/metaborg/common/blob/master/LICENSE)
[![GitHub actions](https://img.shields.io/github/workflow/status/metaborg/common/Build?label=GitHub%20actions)](https://github.com/metaborg/common/actions/workflows/build.yml)
[![Jenkins](https://img.shields.io/jenkins/build/https/buildfarm.metaborg.org/job/metaborg/job/common/job/master?label=Jenkins)](https://buildfarm.metaborg.org/job/metaborg/job/common/job/master/lastBuild)
[![Jenkins Tests](https://img.shields.io/jenkins/tests/https/buildfarm.metaborg.org/job/metaborg/job/common/job/master?label=Jenkins%20tests)](https://buildfarm.metaborg.org/job/metaborg/job/common/job/master/lastBuild/testReport/)
[![common](https://img.shields.io/maven-metadata/v?label=common&metadataUrl=https%3A%2F%2Fartifacts.metaborg.org%2Fcontent%2Frepositories%2Freleases%2Forg%2Fmetaborg%2Fcommon%2Fmaven-metadata.xml)](https://mvnrepository.com/artifact/org.metaborg/common?repo=metaborg-releases)

# MetaBorg Commons

Common and utility classes for `org.metaborg` projects.
See `CHANGELOG.md` for a list of releases and notable changes to this repository.

## Development

### Building

The `master` branch of this repository can be built in isolation.
However, the `develop` branch must be built via the [devenv repository](https://github.com/metaborg/devenv), due to it depending on development versions of other projects.

This repository is built with Gradle, which requires a JDK of at least version 8 to be installed. Higher versions may work depending on [which version of Gradle is used](https://docs.gradle.org/current/userguide/compatibility.html).

To build this repository, run `./gradlew buildAll` on Linux and macOS, or `gradlew buildAll` on Windows.

### Automated Builds

This repository is built on:
- [GitHub actions](https://github.com/metaborg/common/actions/workflows/build.yml) via `.github/workflows/build.yml`. Only the `master` branch is built here.
- Our [Jenkins buildfarm](https://buildfarm.metaborg.org/view/Devenv/job/metaborg/job/common/) via `Jenkinsfile` which uses our [Jenkins pipeline library](https://github.com/metaborg/jenkins.pipeline/).

### Publishing

This repository is published via Gradle and Git with the [Gitonium](https://github.com/metaborg/gitonium) and [Gradle Config](https://github.com/metaborg/gradle.config) plugins.
It is published to our [artifact server](https://artifacts.metaborg.org) in the [releases repository](https://artifacts.metaborg.org/content/repositories/releases/).

First update `CHANGELOG.md` with your changes, create a new release entry, and update the release links at the bottom of the file.

Then, commit your changes and merge them from the `develop` branch into the `master` branch, and ensure that you depend on only released versions of other projects (i.e., no `SNAPSHOT` or development versions).
All dependencies are managed in the `depconstraints/build.gradle.kts` file.

To make a new release, create a tag in the form of `release-*` where `*` is the version of the release you'd like to make.
Then first build the project with `./gradlew buildAll` to check if building succeeds.

If you want our buildfarm to publish this release, just push the tag you just made, and our buildfarm will build the repository and publish the release.

If you want to publish this release locally, you will need an account with write access to our artifact server, and tell Gradle about this account.
Create the `./gradle/gradle.properties` file if it does not exist.
Add the following lines to it, replacing `<username>` and `<password>` with those of your artifact server account:
```
publish.repository.metaborg.artifacts.username=<username>
publish.repository.metaborg.artifacts.password=<password>
```
Then run `./gradlew publishAll` to publish all built artifacts.
You should also push the release tag you made such that this release is reproducible by others.

## Copyright and License

Copyright © 2018-2022 Delft University of Technology

The files in this repository are licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
You may use the files in this repository in compliance with the license.
