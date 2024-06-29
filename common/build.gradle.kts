plugins {
    id("org.metaborg.gradle.config.java-library")
    id("org.metaborg.gradle.config.junit-testing")
}

dependencies {
    api(platform(project(":common.depconstraints")))
    annotationProcessor(platform(project(":common.depconstraints")))
    testImplementation(platform(project(":common.depconstraints")))

    api("org.metaborg:resource")

    compileOnly("org.derive4j:derive4j-annotation")
    compileOnly("org.checkerframework:checker-qual-android")

    annotationProcessor("org.derive4j:derive4j")

    testCompileOnly("org.checkerframework:checker-qual-android")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.16.1")
}
