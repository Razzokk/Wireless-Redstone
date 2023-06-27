import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.jetbrains.changelog.Changelog

plugins {
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

val modReleaseType =
	if (modVersion.lowercase().contains("beta")) "beta"
	else if (modVersion.lowercase().contains("alpha")) "alpha"
	else "release"

val curseforgeProjectId: String by project
val generatedResources = file("src/main/generated")

version = modVersion
group = modGroup

base {
	archivesName.set("$modId-fabric")
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)
}

repositories {
	maven("https://maven.shedaniel.me/")				// Cloth config
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
	maven("https://maven.blamejared.com/")				// JEI
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

loom {
    splitEnvironmentSourceSets()

	mods {
		register(modId) {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}

	runs {
		register("datagenClient") {
			inherit(get("client"))
			name("Data Generation")
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=$generatedResources")
			vmArg("-Dfabric-api.datagen.modid=$modId")
			runDir("build/datagen")
		}
	}
}

sourceSets {
	main {
		resources {
			srcDir(generatedResources)
		}
	}
}

tasks {
	processResources {
		// this will ensure that this task is redone when the versions change.
		inputs.property("version", version)

		// NOTE: for this to work at runtime (e.g. Minecraft Client) Gradle
		// must be selected in the build tools settings for gradle!
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

// Publishing

changelog {
	versionPrefix.set("release/fabric/")
	groups.empty()
	combinePreReleases.set(false)
	repositoryUrl.set("https://github.com/Razzokk/WirelessRedstone")
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
