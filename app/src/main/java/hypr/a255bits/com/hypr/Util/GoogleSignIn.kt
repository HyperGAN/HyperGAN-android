package hypr.a255bits.com.hypr.Util

import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by tedho on 9/13/2017.
 */

class GoogleSignIn(val context: Context){
val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
    }
    val client: GoogleApiClient by lazy {
        GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }
    init{

    }
}