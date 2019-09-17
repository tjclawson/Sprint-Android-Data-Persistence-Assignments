package com.example.readinglist

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

    fun toCsvString(): String {
        return "$title,$reasonToRead,$hasBeenRead,$id"
    }
}