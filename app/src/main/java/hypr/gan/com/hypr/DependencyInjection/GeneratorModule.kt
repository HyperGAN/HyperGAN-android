package hypr.gan.com.hypr.DependencyInjection

import hypr.gan.com.hypr.Generator.Generator
import hypr.gan.com.hypr.GeneratorLoader.EasyGeneratorLoader
import org.koin.android.module.AndroidModule
import org.koin.dsl.context.Context

class GeneratorModule : AndroidModule() {
    override fun context(): Context {
        return applicationContext {
            context("generator") {
                provide { getGeneratorLoader() }
            }
        }
    }
    fun getGeneratorLoader(): EasyGeneratorLoader {
        return EasyGeneratorLoader(Generator())
    }

}