package hypr.a255bits.com.hypr.Dashboard

import hypr.a255bits.com.hypr.BuyGenerator


interface DashboardMVP{
    interface view{
        fun displayListOfModels(buyGenerators: MutableList<BuyGenerator>, welcomeScreenAdapter: WelcomeScreenAdapter)

    }
    interface presenter{

    }

}