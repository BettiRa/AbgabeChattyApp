package at.fhj.iit.slideshowgroupb.model


data class Feed(val title:String, val imageUrl:String = "http://default.img", val timestamp: String?, val description: String?, val sound: String?){
    override fun toString():String {
        return "Slide '$title'"
    }

    //Train lazy Properties

    val base64ImageString:String by lazy {
        loadTheImageBase64()
    }

    val base64Timestamp:String? by lazy {
        loadTheTimestampBase64()
    }
    val base64Description:String? by lazy {
        loadTheDescriptionBase64()
    }
    val base64Sound:String? by lazy {
        loadTheSoundBase64()
    }

    private fun loadTheImageBase64():String{
        println("expensive operation")
        return "not implemented yet"
    }

    private fun loadTheDescriptionBase64(): String? {
        println("useless function")
        return null
    }
    private fun loadTheTimestampBase64():String?{
        println("useless function")
        return null
    }
    private fun loadTheSoundBase64():String?{
        println("useless function")
        return null
    }
}