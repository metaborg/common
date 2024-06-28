plugins {
  id("org.metaborg.gradle.config.java-library")
  id("org.metaborg.gradle.config.junit-testing")
}

dependencies {
  api(platform(project(":common.depconstraints")))
  annotationProcessor(platform(project(":common.depconstraints")))
  testImplementation(platform(project(":common.depconstraints")))

  api(libs.spoofax3.resource)

  compileOnly(libs.derive4j.annotation)


  compileOnly(libs.checkerframework.android) // Use android version: annotation retention policy is class instead of runtime.

  annotationProcessor(libs.derive4j)

  testCompileOnly(libs.checkerframework.android)
  testImplementation(libs.equalsverifier)
}
