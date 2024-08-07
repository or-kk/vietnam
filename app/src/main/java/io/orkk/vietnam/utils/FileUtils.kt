package io.orkk.vietnam.utils

import java.io.File

class FileUtils {

    companion object {

        fun splitAppVersion(version: String): List<String> {
            val versionList: MutableList<String> = arrayListOf()
            val splitVersion = version.split("|")
            for(item in splitVersion) {
                versionList.add(item)
            }

            return versionList.toList()
        }

        fun removeFileExtension(file: File): File {
            val fileName = file.name
            val newFileName = if (fileName.contains('.')) {
                fileName.substring(0, fileName.lastIndexOf('.'))
            } else {
                fileName
            }
            return File(file.parent, newFileName)
        }

        fun getPathAndFileName(file: File): Pair<String, String> {
            val path = file.parent ?: ""
            val fileName = file.name
            return Pair(path, fileName)
        }

        fun isFolderExists(path: String, folderName: String): Boolean {
            val folder = File(path, folderName)
            return folder.exists() && folder.isDirectory
        }
    }
}