package hypr.hypergan.com.hypr.Dashboard

import hypr.hypergan.com.hypr.BuyGenerator


interface DashboardMVP{
    interface view{
        fun displayListOfModels(buyGenerators: MutableList<BuyGenerator>, welcomeScreenAdapter: WelcomeScreenAdapter)

    }

}