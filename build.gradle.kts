import org.metaborg.convention.Developer
import org.metaborg.convention.MavenPublishConventionExtension

// Workaround for issue: https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.metaborg.convention.root-project")
    alias(libs.plugins.gitonium)
}

rootProjectConvention {
    // This will add `publishAll` and `publish` tasks that call the `publish` task on each subproject and sub-build
    registerPublishTasks.set(true)
}

allprojects {
    apply(plugin = "org.metaborg.gitonium")

    // Configure Gitonium before setting the version
    gitonium {
        mainBranch.set("master")
    }

    version = gitonium.version
    group = "org.metaborg"

    pluginManager.withPlugin("org.metaborg.convention.maven-publish") {
        extensions.configure(MavenPublishConventionExtension::class.java) {
            repoOwner.set("metaborg")
            repoName.set("common")

            metadata {
                inceptionYear.set("2018")
                developers.set(listOf(
                    Developer("gohla", "Gabriel Konat", "g.d.p.konat@tudelft.nl"),
                    Developer("virtlink", "Daniel A. A. Pelsmaeker", "d.a.a.pelsmaeker@tudelft.nl"),
                ))
            }
        }
    }
}
