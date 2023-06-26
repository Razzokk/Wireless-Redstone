import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.jetbrains.changelog.Changelog

plugins {
	java
	id("fabric-loom") version "1.2-SNAPSHOT"
	id("com.modrinth.minotaur") version "2.+"
	id("net.darkhax.curseforgegradle") version "1.1.15"
	id("org.jetbrains.changelog") version "2.1.0"
}

val javaVersion: Int = JavaLanguageVersion.of(property("javaVersion").toString()).asInt()
val mcVersion: String by project
val modId: String by project
val modVersion: String by project
val modGroup: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project
val clothConfigVersion: String by project
val modMenuVersion: String by project
val jeiVersion: String by project

val modReleaseType: String =
	if (modVersion.lowercase().contains("beta")) "beta"
	else if (modVersion.lowercase().contains("alpha")) "alpha"
	else "release"

val curseforgeProjectId: String by project

version = modVersion
group = modGroup

base {
	archivesName.set("$modId-fabric")
}

repositories {
	// Cloth config
	maven("https://maven.shedaniel.me/")
	// Mod Menu
	maven("https://maven.terraformersmc.com/releases/")
	// JEI
	maven("https://maven.blamejared.com/")
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
	minecraft("com.mojang", "minecraft", mcVersion)
	mappings("net.fabricmc", "yarn", yarnMappings, classifier = "v2")

	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricApiVersion)

	modApi("me.shedaniel.cloth", "cloth-config-fabric", clothConfigVersion)
	modApi("com.terraformersmc", "modmenu", modMenuVersion)

	modLocalRuntime("mezz.jei", "jei-$mcVersion-fabric", jeiVersion)
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
				"mcversion" to mcVersion,
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

// Publishing

changelog {
	versionPrefix.set("")
	groups.empty()
	combinePreReleases.set(false)
}

modrinth {
	if (project.hasProperty("debug")) debugMode.set(true)
	token.set(System.getenv("MODRINTH_TOKEN"))

	projectId.set(modId)
	versionNumber.set("fabric-$modVersion")
	versionName.set("[Fabric $mcVersion] $modId-$modVersion")
	versionType.set(modReleaseType)
	uploadFile.set(tasks.remapJar)
	changelog.set(provider { project.changelog.renderItem(project.changelog.get(modVersion), Changelog.OutputType.MARKDOWN) })

	dependencies {
		required.project("fabric-api")
		optional.project("cloth-config")
		optional.project("modmenu")
	}
}

tasks.register<TaskPublishCurseForge>("curseforge") {
	if (project.hasProperty("debug")) debugMode = true
	apiToken = System.getenv("CURSEFORGE_TOKEN")

	val file = upload(curseforgeProjectId, tasks.remapJar)
	file.displayName = "[Fabric $mcVersion] $modId-$modVersion"
	file.releaseType = modReleaseType
	file.changelog = provider { project.changelog.renderItem(project.changelog.get(modVersion), Changelog.OutputType.MARKDOWN) }.get()
	file.changelogType = Constants.CHANGELOG_MARKDOWN
	file.addJavaVersion("Java $javaVersion")
	file.addRequirement("fabric-api")
	file.addOptional("cloth-config", "modmenu")
}
