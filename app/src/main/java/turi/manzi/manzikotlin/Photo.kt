package turi.manzi.manzikotlin

class Photo (val title: String, val author: String, val link: String, val tags: String, val image: String){

    override fun toString(): String {
        return "Photo(title='$title', author='$author', link='$link', tags='$tags', image='$image')"
    }
}