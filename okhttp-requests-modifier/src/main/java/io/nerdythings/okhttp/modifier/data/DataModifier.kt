package io.nerdythings.okhttp.modifier.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.File
import java.util.UUID
import java.util.regex.PatternSyntaxException

class DataModifier(private val context: Context) {

    private val _customResponses = MutableStateFlow<List<Pair<String, CustomResponse>>>(emptyList())
    internal val customResponses: StateFlow<List<Pair<String, CustomResponse>>> =
        _customResponses.asStateFlow()
    private val gson = Gson()

    private val modifiersFile
        get(): File {
            val file = File(context.cacheDir, "profiler_request_modifiers")
            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        }

    /**
     * The list of triples with path, uuid, and isEnabled
     */
    private val pathModifiersList: List<Triple<String, String, Boolean>>
        get() = modifiersFile
            .takeIf { it.exists() }
            ?.readText()
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                val typeToken = object : TypeToken<List<Triple<String, String, Boolean>>>() {}.type
                gson.fromJson(it, typeToken)
            } ?: emptyList()

    private fun getAllFilesByPath(path: String): List<File> =
        pathModifiersList.mapNotNull {
            try {
                val matches = Regex(it.first).matches(path)
                if (matches) {
                    File(context.cacheDir, it.second).takeIf { file -> file.exists() }
                } else {
                    null
                }
            } catch (e: PatternSyntaxException) {
                null
            }
        }

    private fun createFileByPath(path: String, uuid: String): File {
        // create a file
        val file = File(context.cacheDir, uuid)
        file.createNewFile()
        // save path with uuid in file
        val mutableList = pathModifiersList.toMutableList()
        mutableList.add(Triple(path, uuid, true))
        modifiersFile.writeText(
            gson.toJson(mutableList).toString()
        )
        return file
    }

    private fun getCustomResponses(path: String): List<CustomResponse> =
        getAllFilesByPath(path).mapNotNull { file ->
            file.readText().takeIf { it.isNotEmpty() }?.let {
                gson.fromJson(it, CustomResponse::class.java)
            }
        }.toList()

    internal suspend fun fetchAllCustomResponses() = withContext(Dispatchers.IO) {
        val savedModifications = mutableListOf<Pair<String, CustomResponse>>()
        pathModifiersList.forEach { triple ->
            File(context.cacheDir, triple.second)
                .takeIf { it.exists() }
                ?.readText()
                ?.let { response ->
                    gson.fromJson(response, CustomResponse::class.java)
                }?.let {
                    savedModifications.add(triple.first to it)
                }
        }
        _customResponses.emit(savedModifications)
    }

    internal suspend fun createCustomResponse(path: String, response: String, responseCode: Int) {
        val uuid = UUID.randomUUID().toString()
        withContext(Dispatchers.IO) {
            createFileByPath(path, uuid).writeText(
                gson.toJson(
                    CustomResponse(
                        id = uuid,
                        url = path,
                        response = response,
                        code = responseCode,
                        isEnabled = true,
                    )
                )
            )
        }
        fetchAllCustomResponses()
    }

    internal suspend fun updateCustomResponse(customResponse: CustomResponse) {
        withContext(Dispatchers.IO) {
            // update the file
            (
                    File(context.cacheDir, customResponse.id).takeIf { file ->
                        file.exists()
                    }
                        ?: createFileByPath(customResponse.url, customResponse.id)
                    ).writeText(gson.toJson(customResponse))
            // update the path to the file
            val list = pathModifiersList.filter {
                it.second != customResponse.id
            }
            val updatedList = list.toMutableList()
            updatedList.add(
                Triple(customResponse.url, customResponse.id, customResponse.isEnabled)
            )
            modifiersFile.writeText(
                gson.toJson(updatedList).toString()
            )
        }
        fetchAllCustomResponses()
    }

    internal suspend fun removeCustomResponse(uuid: String) {
        withContext(Dispatchers.IO) {
            // delete the file
            File(context.cacheDir, uuid).takeIf { file -> file.exists() }?.delete()
            // delete other data
            val updatedList = pathModifiersList.filter { it.second != uuid }
            modifiersFile.writeText(
                gson.toJson(updatedList).toString()
            )
        }
        fetchAllCustomResponses()
    }

    fun modifyResponse(request: Request, makeRequest: () -> Response) =
        getCustomResponses(request.url.toString()).firstOrNull { it.isEnabled }
            ?.let { customResponse ->
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_2)
                    .message("success")
                    .code(customResponse.code)
                    .body(customResponse.response.toResponseBody())
                    .build()
            } ?: makeRequest()
}