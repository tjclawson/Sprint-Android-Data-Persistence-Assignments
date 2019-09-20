package com.example.readinglist

import org.json.JSONException
import org.json.JSONObject

class Book {

    companion object {
        const val INVALID_ID = -1
    }

    var title: String? = null
    var reasonToRead: String? = null
    var hasBeenRead: Boolean = false
    var id: Int = 20

    constructor(title: String?, reasonToRead: String?, hasBeenRead: Boolean, id: Int) {
        this.title = title
        this.reasonToRead = reasonToRead
        this.hasBeenRead = hasBeenRead
        this.id = id
    }
    constructor(csvString: String) {
        val values = csvString.split(",")
        this.title = values[0]
        this.reasonToRead = values[1]
        this.hasBeenRead = values[2].toBoolean()
        this.id = values[3].toInt()
    }

    constructor(jsonObject: JSONObject) {
        try {
            this.title = jsonObject.getString("title")
        } catch (e: JSONException) {
            this.title = "Title Not Found"
        }
        try {
            this.reasonToRead = jsonObject.getString("reason_to_read")
        } catch (e: JSONException) {
            this.reasonToRead = "Reason Not Found"
        }
        try {
            this.hasBeenRead = jsonObject.getBoolean("has_been_read")
        } catch (e: JSONException) {
            this.hasBeenRead = false
        }
        try {
            this.id = jsonObject.getInt("id")
        } catch (e: JSONException) {
            this.id = -1
        }
    }

    fun toCsvString(): String {
        return "$title,$reasonToRead,$hasBeenRead,$id"
    }

    fun toJsonObject(): JSONObject? {
        try {
            return JSONObject().apply {
                put("title", title)
                put("reason_to_read", reasonToRead)
                put("has_been_read", hasBeenRead)
                put("id", id)
            }
        } catch (e: JSONException) {
            return try {
                JSONObject("{\"title\" : \"$title\", \"reason_to_read\" : $reasonToRead, \"has_been_read\" : $hasBeenRead, \"id\": $id}")
            } catch (e2: JSONException) {
                e2.printStackTrace()
                return null
            }
        }
    }
}