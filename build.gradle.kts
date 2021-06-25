plugins {
    id("fabric-loom")
    id("maven-publish")
    kotlin("jvm")
}

val minecraftVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project

val archivesBaseName: String by project
val modVersion: String by project
val mavenGroup: String by project

val fabricVersion: String by project
val fabricKotlinVersion: String by project

version = modVersion
group = mavenGroup

dependencies {
    minecraft("com.mojang", "minecraft", minecraftVersion)
    mappings("net.fabricmc", "yarn", yarnMappings, classifier="v2")
    modImplementation("net.fabricmc", "fabric-loader", loaderVersion)

    modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricVersion)
    modImplementation("net.fabricmc", "fabric-language-kotlin", fabricKotlinVersion)
}

tasks.withType<ProcessResources> {
    inputs.property("version", modVersion)
    filesMatching("fabric.mod.json") {
        expand("version" to modVersion)
    }
}

java {
    withSourcesJar()
}

tasks.withType<Jar> {
    from("LICENSE") {
        rename { "${it}_${archivesBaseName}" }
    }
}

val remapJar = tasks.getByName<net.fabricmc.loom.task.RemapJarTask>("remapJar")
val remapSourcesJar = tasks.getByName<net.fabricmc.loom.task.RemapSourcesJarTask>("remapSourcesJar")

publishing {
    publications.withType<MavenPublication> {
        artifact(remapJar) {
            builtBy(remapJar)
        }
        artifact(remapSourcesJar) {
            builtBy(remapSourcesJar)
        }
    }
}
