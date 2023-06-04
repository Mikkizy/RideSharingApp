package com.ukaka.ridesharingapp.domain.usecases

import com.ukaka.ridesharingapp.common.Constants.CLIENT_ID
import com.ukaka.ridesharingapp.common.Constants.CLIENT_SECRET
import com.ukaka.ridesharingapp.common.Constants.GRANT_TYPE
import com.ukaka.ridesharingapp.common.Constants.SCOPE
import com.ukaka.ridesharingapp.data.remote.UberAuthService
import com.ukaka.ridesharingapp.domain.models.AccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAccessToken @Inject constructor(
    private val authService: UberAuthService
) {
    operator fun invoke(): AccessToken {
        lateinit var accessToken: AccessToken
        CoroutineScope(Dispatchers.IO).launch {
           val response = authService.getAccessToken(
               CLIENT_ID,
               CLIENT_SECRET,
               GRANT_TYPE,
               SCOPE
           )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        accessToken = it
                    }
                }
            }
        }
        return accessToken
    }
}