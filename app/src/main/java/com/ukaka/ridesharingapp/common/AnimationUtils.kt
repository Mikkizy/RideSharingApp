package com.ukaka.ridesharingapp.common

import android.app.Activity
import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController

/**
 * Creates a [tween] AnimationSpec that follows system 'Animator duration scale' (i.e. if the
 * 'Animator duration scale' value is modified in the device's developer options, then this value
 * would also be tweaked accordingly).
 * */
@Composable
fun <T> systemTween(
    durationMillis: Int = AnimationConstants.DefaultDurationMillis,
    easing: Easing = FastOutSlowInEasing
): TweenSpec<T> = tween(
    durationMillis = durationMillis.times(getAnimationDurationScale()).toInt(),
    easing = easing
)

@Composable
private fun getAnimationDurationScale(): Float {
    val activity = LocalContext.current as Activity
    return remember { activity.getAnimationDurationScale() }
}

private fun Activity.getAnimationDurationScale(): Float {
    return Settings.Global.getFloat(
        contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1.0f
    )
}

fun SystemUiController.changeSystemBarsColor(
    color: Color = Color.Transparent,
    darkIcons: Boolean = true
) {
    setSystemBarsColor(
        color = color,
        darkIcons = darkIcons
    )
}

fun NavController.clearAndNavigate(
    clearDestination: String,
    navigateToDestination: String,
) {
    navigate(navigateToDestination) {
        popUpTo(clearDestination) {
            inclusive = true
        }
    }
}