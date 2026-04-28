package lambda.core.common.di

import java.io.File
import java.net.URLDecoder

object ClassScanner {

    fun scan(packageName: String): List<Class<*>> {
        val path = packageName.replace(".", "/")
        val resources = Thread.currentThread().contextClassLoader.getResources(path)

        val classes = mutableListOf<Class<*>>()

        while (resources.hasMoreElements()) {
            val url = resources.nextElement()
            val file = File(URLDecoder.decode(url.file, "UTF-8"))

            classes += findClasses(file, packageName)
        }

        return classes
    }

    private fun findClasses(directory: File, packageName: String): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()

        if (!directory.exists()) return classes

        for (file in directory.listFiles()!!) {
            if (file.isDirectory) {
                classes += findClasses(file, "$packageName.${file.name}")
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.removeSuffix(".class")}"
                classes += Class.forName(className)
            }
        }

        return classes
    }
}