plugins {
    `java-platform`
    `maven-publish`
}

dependencies {
    constraints {
        api(libs.spoofax3.resource)
        api(libs.checkerframework.android) // Use android version: annotation retention policy is class instead of runtime.
        api(libs.derive4j)
        api(libs.derive4j.annotation)
    }
}

publishing {
    publications {
        create<MavenPublication>("JavaPlatform") {
            from(components["javaPlatform"])
        }
    }
}
