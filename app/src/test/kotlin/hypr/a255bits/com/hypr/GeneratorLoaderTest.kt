package hypr.a255bits.com.hypr

import android.graphics.Bitmap
import android.test.ActivityTestCase
import org.mockito.Mockito.*
import org.junit.Test

class GeneratorLoaderTest: ActivityTestCase() {
    val generatorLoader = GeneratorLoader()

    val scaled = Bitmap.createScaledBitmap(any(), 128, 128, false)
    val encoded = generatorLoader.encode(scaled)

    @Test
    fun load_and_feed() {
        generatorLoader.load(activity.assets)

        generatorLoader.sample(encoded)
        assertNotNull(generatorLoader)
    }
}
