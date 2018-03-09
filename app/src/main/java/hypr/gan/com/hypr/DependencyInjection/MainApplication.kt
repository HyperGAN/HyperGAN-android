package hypr.gan.com.hypr.DependencyInjection

import android.app.Application
import android.support.multidex.MultiDex
import org.koin.android.ext.android.startAndroidContext

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startAndroidContext(this, GeneratorModule())
        MultiDex.install(this)
    }
}
