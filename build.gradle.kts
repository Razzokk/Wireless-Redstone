plugins {
	id("fabric-loom") version("1.2-SNAPSHOT")
}

val javaVersion: Int = JavaLanguageVersion.of(property("javaVersion").toString()).asInt()
val minecraftVersion: String by project
val modId: String by project
val modVersion: String by project
val modGroup: String by project
val modArchivesBaseName: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project
val clothConfigVersion: String by project
val modMenuVersion: String by project

version = modVersion
group = modGroup

base {
	archivesName.set("${modArchivesBaseName}-fabric")
}

repositories {
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
}

loom {
    splitEnvironmentSourceSets()

	mods {
		register(modId) {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}
}

dependencies {
	minecraft("com.mojang", "minecraft", minecraftVersion)
	mappings("net.fabricmc", "yarn", yarnMappings, classifier = "v2")

	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricApiVersion)

	modApi("me.shedaniel.cloth", "cloth-config-fabric", clothConfigVersion)
	modApi("com.terraformersmc", "modmenu", modMenuVersion)
}

tasks {
	processResources {
		// this will ensure that this task is redone when the versions change.
		inputs.property("version", version)

		filesMatching("fabric.mod.json") {
			expand(mapOf(
				"version" to modVersion,
				"loader_version" to loaderVersion,
				"fabric_api_version" to fabricApiVersion,
				"mcversion" to minecraftVersion,
				"java_version" to javaVersion,
				"cloth_config_version" to clothConfigVersion,
				"modmenu_version" to modMenuVersion
			))
		}
	}

	withType<JavaCompile> {
		options.release.set(javaVersion)
	}
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)
}
