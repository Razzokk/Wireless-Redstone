import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
	eclipse
	idea
	id("net.minecraftforge.gradle") version "[6.0,6.2)"
	id("org.parchmentmc.librarian.forgegradle") version "1.+"
	id("com.modrinth.minotaur")
	id("net.darkhax.curseforgegradle")
}

val common = project(":common")

val javaVersion: Int by project
val mcVersion: String by project
val modId: String by project
val modVersion: String by project
val clothConfigVersion: String by project
val jeiVersion: String by project
val forgeVersion: String by project
val parchmentMappings: String by project
val curseforgeProjectId: String by project
val modReleaseType: String by project
val changelogProvider: Provider<String> by project

base {
	archivesName.set("$modId-forge")
}

repositories {
	maven("https://maven.shedaniel.me/")	// Cloth config
	maven("https://maven.blamejared.com/")	// JEI
}

dependencies {
	implementation(common)
	minecraft("net.minecraftforge", "forge", "$mcVersion-$forgeVersion")

	compileOnly(fg.deobf("me.shedaniel.cloth:cloth-config-forge:$clothConfigVersion"))
	runtimeOnly(fg.deobf("me.shedaniel.cloth:cloth-config-forge:$clothConfigVersion"))

	runtimeOnly(fg.deobf("mezz.jei:jei-$mcVersion-forge:$jeiVersion"))
}

project.evaluationDependsOn(common.path)

minecraft {
	mappings("parchment", parchmentMappings)

	copyIdeResources.set(true)

	runs {
		configureEach {
			ideaModule = "${rootProject.name}.${project.name}.main"

			mods {
				register(modId) {
					source(sourceSets.main.get())
				}
			}
		}

		create("client") {
			taskName = "Client"
			args("--username", "Dev")
		}

		create("server") {
			taskName = "Server"
			arg("--nogui")
		}
	}
}

tasks {
	jar {
		from(common.sourceSets.main.get().output)
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		finalizedBy("reobfJar")
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
	versionNumber.set("forge-$modVersion")
	versionName.set("[Forge $mcVersion] $modId-$modVersion")
	versionType.set(modReleaseType)
	uploadFile.set(tasks.jar)
	changelog.set(changelogProvider)

	dependencies {
		optional.project("cloth-config")
	}
}

tasks.register<TaskPublishCurseForge>("curseforge") {
	if (project.hasProperty("debug")) debugMode = true
	apiToken = System.getenv("CURSEFORGE_TOKEN")

	val file = upload(curseforgeProjectId, tasks.jar)
	file.displayName = "[Forge $mcVersion] $modId-$modVersion"
	file.releaseType = modReleaseType
	file.changelog = changelogProvider.get()
	file.changelogType = Constants.CHANGELOG_MARKDOWN
	file.addJavaVersion("Java $javaVersion")
	file.addOptional("cloth-config")
}
