plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.4.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}
group = "fr.maxlego08.menuPlusPlugin"
version = "2.0"
description = "TemplatePlugin"
java.sourceCompatibility = JavaVersion.VERSION_25

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }

    maven {
        name = "PlaceholderAPI/ExtendedClip"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    maven {
        name = "Minecraft Libs"
        url = uri("https://libraries.minecraft.net/")
    }

    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        name = "tcoded-releases"
        url = uri("https://repo.tcoded.com/releases")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    implementation("com.github.Maxlego08:zTranslator:1.0.0.0")
    implementation("com.tcoded:FoliaLib:0.5.1")
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("com.mojang:authlib:3.11.50")
    implementation("org.bstats:bstats-bukkit:3.2.1")
}



publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    configurations = project.configurations.runtimeClasspath.map { setOf(it) }

    dependencies {
    }

    relocate("org.bstats", project.group.toString())
    relocate("com.tcoded.folialib", "fr.maxlego08.template.libs.folialib")
}



tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks {
    runServer {
        downloadPlugins {
        }
        minecraftVersion("26.1.2")
    }
    runPaper.folia.registerTask()
}