import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
	id("com.modrinth.minotaur")
	id("net.darkhax.curseforgegradle")
}

val common = project(":common")
evaluationDependsOn(common.path)
loom.splitEnvironmentSourceSets()

val javaVersion: Int by rootProject
val debug: Boolean by rootProject
val mcVersion: String by project
val modId: String by project
val modVersion: String by project
val loaderVersion: String by project
val yarnMappings: String by project
val fabricApiVersion: String by project
val clothConfigVersion: String by project
val modMenuVersion: String by project
val jeiVersion: String by project
val modrinthProjectId: String by project
val curseForgeProjectId: String by project
val changelogProvider: Provider<String> by project

val modReleaseType: String by project
val modDisplayName = "[Fabric $mcVersion] $modId-$modVersion"

base {
	archivesName.set("$modId-fabric")
}

repositories {
	maven("https://maven.shedaniel.me/")				// Cloth config
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
	maven("https://maven.blamejared.com/")				// JEI
}

dependencies {
	val clientImplementation = configurations.getByName("clientImplementation")

	implementation(project(common.path, configuration = common.configurations.namedElements.name))
	clientImplementation(common.sourceSets["client"].output)

	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modApi("net.fabricmc.fabric-api", "fabric-api", fabricApiVersion)

	modApi("me.shedaniel.cloth", "cloth-config-fabric", clothConfigVersion)
	modApi("com.terraformersmc", "modmenu", modMenuVersion)

	modLocalRuntime("mezz.jei", "jei-$mcVersion-fabric", jeiVersion)
}

loom {
	runs {
		configureEach {
			ideConfigGenerated(true)
		}
	}

	mods {
		register(modId) {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets["client"])
		}
	}
}

tasks {
	// needed for the run configs
	processResources {
		from(common.sourceSets.main.get().resources)
	}

	compileJava {
		source(common.sourceSets.main.get().allJava)
	}

	getByName<JavaCompile>("compileClientJava") {
		source(common.sourceSets["client"].allJava)
	}

	sourcesJar {
		from(common.sourceSets.main.get().allSource)
		from(common.sourceSets["client"].allSource)
	}
}

// Publishing

modrinth {
	debugMode.set(debug)
	token.set(System.getenv("MODRINTH_TOKEN"))

	projectId.set(modrinthProjectId)
	versionNumber.set("fabric-$modVersion")
	versionName.set(modDisplayName)
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
	debugMode = debug
	apiToken = System.getenv("CURSEFORGE_TOKEN")

	val file = upload(curseForgeProjectId, tasks.remapJar)
	file.displayName = modDisplayName
	file.releaseType = modReleaseType
	file.changelog = changelogProvider.get()
	file.changelogType = Constants.CHANGELOG_MARKDOWN
	file.addJavaVersion("Java $javaVersion")
	file.addModLoader("fabric")
	file.addRequirement("fabric-api")
	file.addOptional("cloth-config", "modmenu")
}
