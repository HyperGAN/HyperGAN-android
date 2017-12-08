package hypr.a255bits.com.hypr.DependencyInjection

import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorFacePosition
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragmentPresenter
import hypr.a255bits.com.hypr.Util.FaceDetection
import org.koin.android.module.AndroidModule
import org.koin.dsl.context.Context

class GeneratorModule : AndroidModule() {
    override fun context(): Context {
        return applicationContext {
            context("generator") {
                provide { getGeneratorLoader() }
                provide { ModelFragmentPresenter(get()) }
                provide { GeneratorFacePosition() }
                provide {FaceDetection(get())}
            }
        }
    }
    fun getGeneratorLoader(): EasyGeneratorLoader {
        val easyGen = EasyGeneratorLoader(Generator())
        return easyGen
    }

}