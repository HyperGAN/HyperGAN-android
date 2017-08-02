package hypr.a255bits.com.hypr

import android.test.ActivityTestCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GeneratorLoaderTest() : ActivityTestCase() {
    @Test
    fun load_and_feed() {
        var generatorLoader: GeneratorLoader = GeneratorLoader()
        generatorLoader.load(activity.assets)
        assertNotNull(generatorLoader)

        generatorLoader.sample()
    }
}
