package at.fhj.iit.slideshowgroupb.service

import android.transition.Slide
import at.fhj.iit.slideshowgroupb.model.Feed

object Slideshow {
    var slideCounter = 0

    var slides = mutableListOf<Feed>()

    fun addFeed(aNewFeed: Feed){
        slides.add(aNewFeed)
    }

    override fun toString(): String {
        val sortedFeeds = slides.sortedBy { it.title }
        return sortedFeeds.joinToString(" and ", "A new show with slides: " )
    }

    // TODO
    // shuffle
    // first
    // next

    fun shuffle() {
        return slides.shuffle()
    }
    fun first(): Feed {
        return slides.first()
    }
    fun next(num: Int): Feed {
        return slides[num]
    }

    fun filter():Feed{
        var x:Int = 0
        while(x < slides.size){
            if (slides[x].description == null){
                slides.removeAt(x)
            }
            x++
        }
        return slides.first()
    }


    fun resetCounter(){
        slideCounter = 0
    }
}