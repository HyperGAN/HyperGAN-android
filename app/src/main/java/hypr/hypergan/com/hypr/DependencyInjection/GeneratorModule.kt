package hypr.hypergan.com.hypr.DependencyInjection

import hypr.hypergan.com.hypr.Generator.Generator
import hypr.hypergan.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.hypergan.com.hypr.ModelFragmnt.ModelFragmentPresenter
import org.koin.android.module.AndroidModule
import org.koin.dsl.context.Context

class GeneratorModule : AndroidModule() {
    override fun context(): Context {
        return applicationContext {
            context("generator") {
                provide { getGeneratorLoader() }
                provide { ModelFragmentPresenter(get()) }
            }
        }
    }
    fun getGeneratorLoader(): EasyGeneratorLoader {
        return EasyGeneratorLoader()
    }

}