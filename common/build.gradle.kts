plugins {
    `java-library`
    `maven-publish`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
}

dependencies {
    api(platform(libs.metaborg.platform))
    api(libs.metaborg.resource.api)

    compileOnly(libs.derive4j.annotation)
    compileOnly(libs.checkerframework.android) // Use android version: annotation retention policy is class instead of runtime.

    annotationProcessor(libs.derive4j)

    testCompileOnly(libs.checkerframework.android)
    testImplementation(libs.junit)
    testImplementation(libs.equalsverifier)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
