plugins {
    `java-library`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
    id("org.metaborg.convention.junit")
}

dependencies {
    api(platform(libs.metaborg.platform)) { version { require("latest.integration") } }
    api(libs.metaborg.resource.api)

    compileOnly(libs.derive4j.annotation)
    compileOnly(libs.checkerframework.android) // Use android version: annotation retention policy is class instead of runtime.

    annotationProcessor(libs.derive4j)

    testCompileOnly(libs.checkerframework.android)
    testImplementation(libs.equalsverifier)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
