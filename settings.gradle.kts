rootProject.name = "common.root"

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
}

fun includeProject(path: String, id: String = "common.${path.replace('/', '.')}") {
    include(id)
    project(":$id").projectDir = file(path)
}

include("common")
includeProject("depconstraints")
