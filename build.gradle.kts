buildscript {
    val kotlin_version: String by properties
    val publish_plugin_version: String by properties
    val gradle_tools_version: String by properties

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:$gradle_tools_version")
        classpath("com.vanniktech:gradle-maven-publish-plugin:$publish_plugin_version")
    }
}

allprojects {
    apply(plugin = "com.vanniktech.maven.publish")

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins.withId("com.vanniktech.maven.publish") {
        val extension = extensions.getByName("mavenPublish") as com.vanniktech.maven.publish.MavenPublishPluginExtension
        with(extension) {
            sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
        }
    }
}
