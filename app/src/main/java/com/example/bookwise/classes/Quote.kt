package com.example.bookwise.classes

class Quote(){
    var id:Int = 0
    var text:String = ""
    var book_id:Int = 0
    constructor(
        text:String
        ) : this() {
        this.text = text
    }
    constructor(
        id:Int,
        text:String,
        book_id:Int
    ) : this() {
        this.id = id
        this.text = text
        this.book_id = book_id
    }
}
