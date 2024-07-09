rootProject.name = "common.root"

// This allows us to use plugins from Metaborg Artifacts
pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
}

// This allows us to use the catalog in dependencies
dependencyResolutionManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
    versionCatalogs {
        create("libs") {
            from("org.metaborg.spoofax3:catalog:0.3.3")
        }
    }
}

fun includeProject(path: String, id: String = "common.${path.replace('/', '.')}") {
    include(id)
    project(":$id").projectDir = file(path)
}

include("common")
includeProject("depconstraints")
