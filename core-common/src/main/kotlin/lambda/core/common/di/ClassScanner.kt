package lambda.core.common.di

import java.io.File
import java.net.JarURLConnection
import java.net.URLDecoder
import java.util.jar.JarFile

object ClassScanner {

    fun scan(
        packageName: String,
        classLoader: ClassLoader
    ): List<Class<*>> {
        val path = packageName.replace(".", "/")
        val resources = classLoader.getResources(path)

        val classes = mutableListOf<Class<*>>()

        while (resources.hasMoreElements()) {
            val url = resources.nextElement()

            when (url.protocol) {
                "file" -> {
                    val file = File(URLDecoder.decode(url.file, "UTF-8"))
                    classes += findClassesInDirectory(
                        directory = file,
                        packageName = packageName,
                        classLoader = classLoader
                    )
                }

                "jar" -> {
                    classes += findClassesInJar(
                        connection = url.openConnection() as JarURLConnection,
                        path = path,
                        classLoader = classLoader
                    )
                }
            }
        }

        return classes
    }

    private fun findClassesInDirectory(
        directory: File,
        packageName: String,
        classLoader: ClassLoader
    ): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()

        if (!directory.exists()) {
            return classes
        }

        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                classes += findClassesInDirectory(
                    directory = file,
                    packageName = "$packageName.${file.name}",
                    classLoader = classLoader
                )
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.removeSuffix(".class")}"

                loadClass(className, classLoader)?.let { clazz ->
                    classes += clazz
                }
            }
        }

        return classes
    }

    private fun findClassesInJar(
        connection: JarURLConnection,
        path: String,
        classLoader: ClassLoader
    ): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val jarFile: JarFile = connection.jarFile

        jarFile.entries().asSequence()
            .filter { !it.isDirectory }
            .filter { it.name.startsWith(path) }
            .filter { it.name.endsWith(".class") }
            .forEach { entry ->
                val className = entry.name
                    .removeSuffix(".class")
                    .replace("/", ".")

                loadClass(className, classLoader)?.let { clazz ->
                    classes += clazz
                }
            }

        return classes
    }

    private fun loadClass(
        className: String,
        classLoader: ClassLoader
    ): Class<*>? {
        if (className.contains("$")) {
            return null
        }

        return try {
            val clazz = Class.forName(
                className,
                false,
                classLoader
            )

            if (
                clazz.isAnonymousClass ||
                clazz.isLocalClass ||
                clazz.isSynthetic ||
                clazz.isInterface ||
                clazz.isAnnotation
            ) {
                null
            } else {
                clazz
            }
        } catch (e: Throwable) {
            null
        }
    }
}