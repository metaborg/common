rootProject.name = "common.root"

pluginManagement {
  repositories {
    maven("https://artifacts.metaborg.org/content/groups/public/")
  }
}

if(org.gradle.util.VersionNumber.parse(gradle.gradleVersion).major < 6) {
  enableFeaturePreview("GRADLE_METADATA")
}

fun includeProject(path: String, id: String = "common.${path.replace('/', '.')}") {
  include(id)
  project(":$id").projectDir = file(path)
}

include("common")
includeProject("depconstraints")
