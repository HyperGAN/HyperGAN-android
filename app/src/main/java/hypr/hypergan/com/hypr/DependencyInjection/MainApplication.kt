package hypr.hypergan.com.hypr.DependencyInjection

import android.app.Application
import org.koin.android.ext.android.startAndroidContext

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startAndroidContext(this, GeneratorModule())
    }
}
