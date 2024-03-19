val common = project(":common")
val fabric = project(":fabric")
evaluationDependsOn(common.path)
evaluationDependsOn(fabric.path)
loom.splitEnvironmentSourceSets()

val generatedResources = common.file("src/main/generated")

val javaVersion: Int by rootProject
val debug: Boolean by rootProject
val modId: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project

dependencies {
	val clientImplementation = configurations.getByName("clientImplementation")

	implementation(project(common.path, configuration = common.configurations.namedElements.name))
	clientImplementation(common.sourceSets["client"].output)

	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modApi("net.fabricmc.fabric-api", "fabric-api", fabricApiVersion)

	runtimeOnly(project(fabric.path, configuration = common.configurations.namedElements.name)) { isTransitive = false }
}

fabricApi {
	configureDataGeneration {
		outputDirectory.set(generatedResources)
		addToResources.set(false)
	}
}
