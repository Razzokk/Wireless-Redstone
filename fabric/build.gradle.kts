import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
	id("fabric-loom") version "1.2-SNAPSHOT"
	id("com.modrinth.minotaur")
	id("net.darkhax.curseforgegradle")
}

val common = project(":common")

val javaVersion: Int by rootProject
val mcVersion: String by project
val modId: String by project
val modVersion: String by project
val loaderVersion: String by project
val yarnMappings: String by project
val fabricApiVersion: String by project
val clothConfigVersion: String by project
val modMenuVersion: String by project
val jeiVersion: String by project
val curseforgeProjectId: String by project
val modReleaseType: String by project
val changelogProvider: Provider<String> by project

val generatedResources: File = common.file("src/main/generated")

base {
	archivesName.set("$modId-fabric")
}

repositories {
	maven("https://maven.shedaniel.me/")				// Cloth config
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
	maven("https://maven.blamejared.com/")				// JEI
}

dependencies {
	implementation(common)
	minecraft("com.mojang", "minecraft", mcVersion)
	mappings("net.fabricmc", "yarn", yarnMappings, classifier = "v2")

	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricApiVersion)

	modCompileOnly("me.shedaniel.cloth", "cloth-config-fabric", clothConfigVersion)
	modCompileOnly("com.terraformersmc", "modmenu", modMenuVersion)

	modLocalRuntime("mezz.jei", "jei-$mcVersion-fabric", jeiVersion)
}

project.evaluationDependsOn(common.path)

loom {
	splitEnvironmentSourceSets()

	mods {
		register(modId) {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets["client"])
		}
	}

	runs {
		named("client") {
			client()
			configName = "Fabric Client"
			programArgs("--username", "Dev")
			// Needed to generate the run configuration
			ideConfigGenerated(true)
		}

		named("server") {
			server()
			configName = "Fabric Server"
			// Needed to generate the run configuration
			ideConfigGenerated(true)
		}
	}
}

tasks {
	jar {
		from(common.sourceSets.main.get().output)
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

	named<Jar>("sourcesJar") {
		from(common.sourceSets.main.get().allSource)
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

	processResources {
		from(common.sourceSets.main.get().resources)
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}

// Publishing

modrinth {
	if (project.hasProperty("debug")) debugMode.set(true)
	token.set(System.getenv("MODRINTH_TOKEN"))

	projectId.set(modId)
	versionNumber.set("fabric-$modVersion")
	versionName.set("[Fabric $mcVersion] $modId-$modVersion")
	versionType.set(modReleaseType)
	uploadFile.set(tasks.remapJar)
	changelog.set(changelogProvider)

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
	file.changelog = changelogProvider.get()
	file.changelogType = Constants.CHANGELOG_MARKDOWN
	file.addJavaVersion("Java $javaVersion")
	file.addRequirement("fabric-api")
	file.addOptional("cloth-config", "modmenu")
}
