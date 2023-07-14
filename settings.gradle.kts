pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		maven("https://maven.minecraftforge.net/") {
			name = "Forge"
		}
		maven("https://maven.parchmentmc.org") {
			name = "Parchment Mappings"
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

val modName: String by extra
rootProject.name = modName
include("common", "fabric", "forge")
