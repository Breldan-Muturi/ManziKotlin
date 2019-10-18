package turi.manzi.manzikotlin

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_detail.*

class PhotoDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        activateToolbar(true)
//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras!!.getParcelable<Photo>(PHOTO_TRANSFER) as Photo
        photo_title.text = photo.title
        photo_tags.text = photo.tags
        photo_author.text = photo.author
        Picasso.get().load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)
    }
}

