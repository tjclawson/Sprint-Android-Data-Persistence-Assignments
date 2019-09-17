package com.example.readinglist

class Book {

    var title: String? = null
    var reasonToRead: String? = null
    var hasBeenRead: Boolean? = null
    var id: String? = null

    constructor(title: String?, reasonToRead: String?, hasBeenRead: Boolean?, id: String?) {
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
        this.id = values[3]
    }

    fun toCsvString(): String {
        return "$title,$reasonToRead,$hasBeenRead,$id"
    }
}