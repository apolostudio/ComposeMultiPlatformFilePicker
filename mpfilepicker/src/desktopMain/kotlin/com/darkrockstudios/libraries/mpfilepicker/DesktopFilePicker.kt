package com.darkrockstudios.libraries.mpfilepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

public data class JvmFile(
    override val path: String,
    override val platformFile: File,
) : MPFile<File>

@Composable
public actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    onFileSelected: FileSelected
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(show) {
        if (show) {
            scope.launch(Dispatchers.Default) {
                val fileFilter = if (fileExtensions.isNotEmpty()) {
                    fileExtensions.joinToString(",")
                } else {
                    ""
                }

                val initialDir = initialDirectory ?: System.getProperty("user.dir")
                val filePath = FileChooser.chooseFile(
                    initialDirectory = initialDir,
                    fileExtensions = fileFilter
                )
                withContext(Dispatchers.Main) {
                    if(filePath != null) {
                        onFileSelected(JvmFile(filePath, File(filePath)))
                    } else {
                        onFileSelected(null)
                    }
                }
            }
        }
    }
}

@Composable
public actual fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    onFileSelected: (String?) -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(show) {
        if (show) {
            scope.launch(Dispatchers.Default) {
                val initialDir = initialDirectory ?: System.getProperty("user.dir")
                val fileChosen = FileChooser.chooseDirectory(initialDir)
                withContext(Dispatchers.Main) {
                    onFileSelected(fileChosen)
                }
            }
        }
    }
}