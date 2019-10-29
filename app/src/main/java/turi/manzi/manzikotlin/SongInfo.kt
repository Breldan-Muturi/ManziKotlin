package turi.manzi.manzikotlin

class SongInfo {
    var Title: String? = null
    var Artist: String? = null
    var SongURL: String? = null

    constructor(Title: String?, Artist: String?, SongURL: String?) {
        this.Title = Title
        this.Artist = Artist
        this.SongURL = SongURL
    }
}