plugins {
  `java-platform`
  `maven-publish`
}

val resourceVersion = "0.11.0"
val checkerframeworkVersion = "3.6.0"
val derive4jVersion = "1.1.1"

dependencies {
  constraints {
    api("org.metaborg:resource:$resourceVersion")
    api("org.checkerframework:checker-qual-android:$checkerframeworkVersion") // Use android version: annotation retention policy is class instead of runtime.
    api("org.derive4j:derive4j:$derive4jVersion")
    api("org.derive4j:derive4j-annotation:$derive4jVersion")
  }
}

publishing {
  publications {
    create<MavenPublication>("JavaPlatform") {
      from(components["javaPlatform"])
    }
  }
}
