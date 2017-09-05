package hypr.a255bits.com.hypr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.junit.Test
import java.io.File


class GeneratorLoaderTest {
    val generatorLoader = GeneratorLoader()


    fun load_and_feed() {
//        generatorLoader.load(activity.assets)
//
//        generatorLoader.sample(encoded)
//        assertNotNull(generatorLoader)
    }

    @Test
    fun testImagesDoneThrowErrorWithGenerator() {
        val file = File(javaClass.classLoader.getResource("image.jpg").toURI())
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        checkNotNull(bitmap)
    }
}
