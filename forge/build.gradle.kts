import net.darkhax.curseforgegradle.Constants
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.spongepowered.asm.gradle.plugins.struct.DynamicProperties

plugins {
	eclipse
	idea
	id("net.minecraftforge.gradle") version "[6.0,6.2)"
	id("org.parchmentmc.librarian.forgegradle") version "1.+"
	id("org.spongepowered.mixin") version "0.7.+"
	id("com.modrinth.minotaur")
}

val common = project(":common")

val javaVersion: Int by rootProject
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

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

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

mixin {
	add(sourceSets.main.get(), "$modId.refmap.json")
	config("$modId.mixins.json")

	if (project.hasProperty("debug")) {
		val debug = this.debug as DynamicProperties
		debug.setProperty("verbose", true)
		debug.setProperty("export", true)
		setDebug(debug)
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
