plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
}

group = "iut.info.sa201.2025"
version = "1.0"

repositories {
    maven {
        url = uri("http://nexus.dep-info.iut-nantes.univ-nantes.prive/repository/public/")
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.21")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    implementation("io.ktor:ktor-client-core:3.1.3")
    implementation("io.ktor:ktor-client-cio:3.1.3")
    implementation("io.ktor:ktor-client-json:3.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation(files("libs/sae-qui-est-ce-client-1.0.jar"))

}


tasks.test {
    useJUnitPlatform()

    //minHeapSize="128m"
    //maxHeapSize="1024m"

    // Use half of available processor to run tests in parallel.
    // The following maxParallelForks config is suggested by gradle itself, https://docs.gradle.org/current/userguide/performance.html#execute_tests_in_parallel

    println("***** AVAILABLE PROCESSORS: ${Runtime.getRuntime().availableProcessors()}")
    //maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    reports.html.required = true

}
