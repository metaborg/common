plugins {
  alias(libs.plugins.spoofax.gradle.rootproject)
  alias(libs.plugins.gitonium)
}

subprojects {
  metaborg {
    configureSubProject()
  }
}
