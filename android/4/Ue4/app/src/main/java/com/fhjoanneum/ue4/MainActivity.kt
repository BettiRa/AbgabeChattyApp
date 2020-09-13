package com.fhjoanneum.ue4
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt
import kotlin.properties.Delegates

// TODO refactor
// slideshow.shuffle()
// currentSlide = Slideshow.first()
// setTitleAndImage( currentSlide )

class MainActivity : AppCompatActivity(), SensorEventListener{

    private val summerSlideshow = Slideshow

    var feedImageView: ImageView? = null

    lateinit var sensorManager: SensorManager

    var accelValue: Float? = null
    var accelLast: Float? = null
    var shake: Float? = null


    var currentFeedNumber: Int by Delegates.observable(0) { _, oldFeedNo, newFeedNo ->
        Log.i("MAIN", "Info: The value changes from $oldFeedNo to $newFeedNo.")
        findViewById<TextView>(R.id.feedNo).text = "Slide no $newFeedNo"
    }

    var currentFeedDescription: String by Delegates.observable("") { _, oldFeedDe, newFeedDe ->
        Log.i("MAIN", "Info: The value changes from $oldFeedDe to $newFeedDe.")
        findViewById<TextView>(R.id.feedDe).text = "Slide Description: $newFeedDe"
    }



    private fun setTitleAndImageSlide(feed: Feed) {
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

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        )

        accelValue=SensorManager.GRAVITY_EARTH
        accelLast=SensorManager.GRAVITY_EARTH
        shake=0.00f

        addSomeDemoSlides()


        val firstFeed = summerSlideshow.slides.first()


        setTitleAndImageSlide(firstFeed)

        currentFeedNumber = 1
        currentFeedDescription = summerSlideshow.slides[0].description.toString()



        val shuffleButton = findViewById<Button>(R.id.buttonShuffle)
        shuffleButton.setOnClickListener {
            summerSlideshow.shuffle()
        }

        val filterButton = findViewById<Button>(R.id.filterButton)
        filterButton.setOnClickListener {
            summerSlideshow.filter()
        }



    }

    private fun showNextSlide() {
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
        summerSlideshow.addFeed(Feed("What a sunset", "sunset", "15.8.2007", null, "sound"))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        accView.text = "X = ${event!!.values[0]}\n\n"+
                "y = ${event.values[1]}\n\n"+
                "z = ${event.values[2]}"

        val vibe: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val effectSuccess: VibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        val effectFailure: VibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);

        val status = findViewById<Switch>(R.id.switchButton)

        var x: Float = event.values[0]
        var y: Float = event.values[1]
        var z: Float = event.values[2]

        accelLast = accelValue
        accelValue=  sqrt( (x*x + y*y + z*z).toDouble()).toFloat()
        var delta: Float = accelValue!! - accelLast!!

        shake = shake!! * 0.9f +delta

        if(shake!! > 15  && status.isChecked){
            showNextSlide()
            vibe.vibrate(effectSuccess)
        }
        if(shake!! > 15  && !(status.isChecked)){
            var toast: Toast = Toast.makeText(applicationContext, "Turn on switch for next Slide", Toast.LENGTH_LONG)
            toast.show()
            vibe.vibrate(effectFailure)
        }



    }




}