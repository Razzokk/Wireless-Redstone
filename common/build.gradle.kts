loom.splitEnvironmentSourceSets()

val generatedResources = file("src/main/generated")

val modId: String by project

loom {
	mods {
		register(modId) {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets["client"])
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
