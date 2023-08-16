val generatedResources = file("src/main/generated")

sourceSets {
	main {
		resources {
			srcDir(generatedResources)
		}
	}
}
