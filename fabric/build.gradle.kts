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

val generatedResources = common.file("src/main/generated")

base {
	archivesName.set("$modId-fabric")
}

repositories {
	maven("https://maven.shedaniel.me/")				// Cloth config
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
	maven("https://maven.blamejared.com/")				// JEI
}

dependencies {
	implementation(project(common.path, configuration = common.configurations.namedElements.name))
	"clientImplementation"(common.sourceSets["client"].output)

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

		register("datagen") {
			server()
			name("Data Generation")
			property("fabric-api.datagen")
			property("fabric-api.datagen.output-dir", generatedResources.toString())
			property("fabric-api.datagen.modid", modId)
			runDir("build/datagen")
			ideConfigGenerated(false)
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
