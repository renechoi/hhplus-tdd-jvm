plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    id("jacoco")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

allprojects {
    group = property("app.group").toString()
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.cloud.dependencies.get().toString())
    }
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.spring.boot.configuration.processor)



    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.aspectj:aspectjrt:1.9.7")
    implementation("org.aspectj:aspectjweaver:1.9.7")


    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.rest-assured:json-path:5.3.2")
    testImplementation("io.rest-assured:xml-path:5.3.2")

    // Cucumber dependencies
    testImplementation("io.cucumber:cucumber-java:7.14.0")
    testImplementation("io.cucumber:cucumber-java8:7.14.0")
    testImplementation("io.cucumber:cucumber-spring:7.14.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.0")
    testImplementation("org.junit.platform:junit-platform-suite:1.10.0")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.10.0")
    testImplementation("org.junit.platform:junit-platform-commons:1.10.0")
    testImplementation("org.junit.platform:junit-platform-engine:1.10.0")



    testImplementation(libs.spring.boot.starter.test)
}

// about source and compilation
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

with(extensions.getByType(JacocoPluginExtension::class.java)) {
    toolVersion = "0.8.7"
}

// bundling tasks
tasks.getByName("bootJar") {
    enabled = true
}
tasks.getByName("jar") {
    enabled = false
}
// test tasks
tasks.test {
    ignoreFailures = true
    useJUnitPlatform()
}
