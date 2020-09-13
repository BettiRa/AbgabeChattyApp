package at.fhj.iit.slideshowgroupb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import at.fhj.iit.slideshowgroupb.model.Feed
import at.fhj.iit.slideshowgroupb.service.Slideshow
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val summerSlideshow = Slideshow

    var feedImageView: ImageView? = null

    var currentFeedNumber: Int by Delegates.observable(0) { _, oldFeedNo, newFeedNo ->
        Log.i("MAIN", "Info: The value changes from $oldFeedNo to $newFeedNo.")
        findViewById<TextView>(R.id.feedNo).text = "Slide no $newFeedNo"
    }

    var currentFeedDescription: String by Delegates.observable("") { _, oldFeedDe, newFeedDe ->
        Log.i("MAIN", "Info: The value changes from $oldFeedDe to $newFeedDe.")
        findViewById<TextView>(R.id.feedDe).text = "Slide Description: $newFeedDe"
    }



    fun setTitleAndImageSlide(feed: Feed) {
        var currentSlideId = feed
        findViewById<TextView>(R.id.feedTitle).text = feed.title
        val feedImageView = findViewById<ImageView>(R.id.feedImage)
        val resId = resources.getIdentifier(currentSlideId.imageUrl,
            "drawable",
            packageName)
        feedImageView.setImageResource(resId) // R.drawable.water
        feedImageView?.setOnClickListener {
            showNextSlide()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addSomeDemoSlides()


        val firstFeed = summerSlideshow.slides.first()


        setTitleAndImageSlide(firstFeed)

        currentFeedNumber = 1
        currentFeedDescription = summerSlideshow.slides[0].description.toString()





        val shuffleButton = findViewById<Button>(R.id.buttonShuffle)
        shuffleButton.setOnClickListener {
            Log.i("test", "Button Working")
            summerSlideshow.shuffle()
        }

        val filterButton = findViewById<Button>(R.id.buttonFilter)
        filterButton.setOnClickListener {
            Log.i("testfilter", "funzt")
            summerSlideshow.filter()
        }

    }

    fun showNextSlide() {
        if (currentFeedNumber >= Slideshow.slides.size) {
            currentFeedNumber = 0
        }

        currentFeedDescription = summerSlideshow.slides[currentFeedNumber].description.toString()
        var currentSlide = summerSlideshow.next(currentFeedNumber++)
        setTitleAndImageSlide(currentSlide)
    }


    private fun addSomeDemoSlides() {
        summerSlideshow.addFeed(Feed("Blue water", "water", "20.2.2002", null, "sound"))
        summerSlideshow.addFeed(Feed("Beautiful sand", "sand", "30.3.1998", "it is sandy", "sound"))
        summerSlideshow.addFeed(Feed("Beachparty!", "beach", "1.1.2010", "party time!", "sound"))
        summerSlideshow.addFeed(Feed("Enjoy the sunset", "sunset", "15.8.2007", null, "sound"))
    }

}