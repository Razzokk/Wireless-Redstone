import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.jetbrains.changelog.Changelog
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
	java
	id("org.jetbrains.changelog") version "2.2.0"
	id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
	id("com.modrinth.minotaur") version "2.+" apply false
	id("net.darkhax.curseforgegradle") version "1.+" apply false
}

val common = project(":common")

val javaVersion by extra {
	JavaLanguageVersion.of(property("javaVersion").toString()).asInt()
}

val debug by extra {
	findProperty("debug")?.toString()?.toBoolean() ?: false
}

val mcVersion: String by project
val mcVersionRange: String by project
val modId: String by project
val modVersion: String by project
val modDisplayName: String by project
val modGroup: String by project
val modAuthor: String by project
val modLicense: String by project
val modDescription: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project
val clothConfigVersion: String by project
val modMenuVersion: String by project
val forgeVersionRange: String by project
val loaderVersionRange: String by project
val neoVersionRange: String by project
val neoLoaderVersionRange: String by project
val repoUrl: String by project

val modReleaseType by extra {
	if (modVersion.lowercase().contains("beta")) "beta"
	else if (modVersion.lowercase().contains("alpha")) "alpha"
	else "release"
}

val changelogProvider by extra {
	provider {
		changelog.renderItem(changelog.get(modVersion), Changelog.OutputType.MARKDOWN)
	}
}

val now: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
val isReleaseBuild = System.getenv("GITHUB_REF")?.startsWith("refs/tags/v") ?: false
val buildNumber: String = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
val license = file("LICENSE")

allprojects {
	version = if (isReleaseBuild) modVersion else "$modVersion-SNAPSHOT+$buildNumber"
	group = modGroup
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "dev.architectury.loom")

	val loom = extensions.getByName<LoomGradleExtensionAPI>("loom")
	loom.silentMojangMappingsLicense()

	val minecraft = configurations.getByName("minecraft")
	val mappings = configurations.getByName("mappings")

	dependencies {
		minecraft("com.mojang", "minecraft", mcVersion)
		mappings("net.fabricmc", "yarn", yarnMappings, classifier = "v2")
	}

	java {
		toolchain {
			languageVersion.set(JavaLanguageVersion.of(javaVersion))
		}
		withSourcesJar()
	}

	tasks {
		jar {
			from(license)
			exclude(".cache/**")
		}

		named<Jar>("sourcesJar") {
			from(license)
		}

		withType<JavaCompile> {
			configureEach {
				options.encoding = "UTF-8"
			}
		}

		withType<Jar> {
			configureEach {
				manifest {
					attributes(mapOf(
						"Specification-Title" to modId,
						"Specification-Vendor" to modAuthor,
						"Specification-Version" to "1",
						"Implementation-Title" to modDisplayName,
						"Implementation-Version" to modVersion,
						"Implementation-Vendor" to modAuthor,
						"Implementation-Timestamp" to now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
						"Timestamp" to System.currentTimeMillis(),
						"Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
						"Built-On-Minecraft" to mcVersion
					))
				}
			}
		}

		processResources {
			val props = mapOf(
				"java_version" to javaVersion,
				"mc_version" to mcVersion,
				"mc_version_range" to mcVersionRange,
				"mod_id" to modId,
				"mod_version" to modVersion,
				"mod_display_name" to modDisplayName,
				"mod_author" to modAuthor,
				"mod_license" to modLicense,
				"mod_description" to modDescription,
				"loader_version" to loaderVersion,
				"fabric_api_version" to fabricApiVersion,
				"cloth_config_version" to clothConfigVersion,
				"modmenu_version" to modMenuVersion,
				"forge_version_range" to forgeVersionRange,
				"loader_version_range" to loaderVersionRange,
				"neo_version_range" to neoVersionRange,
				"neo_loader_version_range" to neoLoaderVersionRange
			)

			// this will ensure that this task is redone when the versions change.
			inputs.properties(props)

			// NOTE: for this to work at runtime (e.g. Minecraft Client) Gradle
			// must be selected in the build tools settings for gradle!
			filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml", "pack.mcmeta", "*.mixins.json")) {
				expand(props)
			}
		}
	}
}

changelog {
	versionPrefix.set("release/")
	groups.empty()
	combinePreReleases.set(false)
	repositoryUrl.set(repoUrl)
}
