# Spoofax 3 Commons
[![Build][github-build-badge]][github-build]
[![Build][jenkins-build-badge]][jenkins-build]
[![Tests][jenkins-tests-badge]][jenkins-tests]
[![License][license-badge]][license]
[![GitHub Release][github-release-badge]][github-release]
[![Maven Release][maven-badge]][maven]

Common and utility classes for Spoofax 3 projects.

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


## License
Copyright 2018-2024 Delft University of Technology

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at <https://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an **"as is" basis, without warranties or conditions of any kind**, either express or implied. See the License for the specific language governing permissions and limitations under the License.


[github-build-badge]: https://img.shields.io/github/actions/workflow/status/metaborg/common/build.yaml
[github-build]: https://github.com/metaborg/common/actions
[jenkins-build-badge]: https://img.shields.io/jenkins/build/https/buildfarm.metaborg.org/job/metaborg/job/common/job/master?label=Jenkins
[jenkins-build]: https://buildfarm.metaborg.org/job/metaborg/job/common/job/master/lastBuild
[jenkins-tests-badge]: https://img.shields.io/jenkins/tests/https/buildfarm.metaborg.org/job/metaborg/job/common/job/master?label=Jenkins%20tests
[jenkins-tests]: https://buildfarm.metaborg.org/job/metaborg/job/common/job/master/lastBuild/testReport/
[license-badge]: https://img.shields.io/github/license/metaborg/common
[license]: https://github.com/metaborg/common/blob/master/LICENSE
[github-release-badge]: https://img.shields.io/github/v/release/metaborg/common
[github-release]: https://github.com/metaborg/common/releases
[maven-badge]: https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifacts.metaborg.org%2Fcontent%2Frepositories%2Freleases%2Forg%2Fmetaborg%2Fcommon%2Fmaven-metadata.xml
[maven]: https://artifacts.metaborg.org/#nexus-search;gav~org.metaborg~common~~~
