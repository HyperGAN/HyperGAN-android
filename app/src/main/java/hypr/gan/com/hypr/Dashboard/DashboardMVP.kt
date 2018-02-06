package hypr.gan.com.hypr.Dashboard

import hypr.gan.com.hypr.BuyGenerator


interface DashboardMVP{
    interface view{
        fun displayListOfModels(buyGenerators: MutableList<BuyGenerator>, welcomeScreenAdapter: WelcomeScreenAdapter)

    }

}