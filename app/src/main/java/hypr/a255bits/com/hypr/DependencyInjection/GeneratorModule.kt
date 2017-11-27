package hypr.a255bits.com.hypr.DependencyInjection

import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import org.koin.android.module.AndroidModule
import org.koin.dsl.context.Context

class GeneratorModule : AndroidModule() {
    override fun context(): Context {
        return applicationContext {
            provide {EasyGeneratorLoader(Generator())}
        }
    }
}