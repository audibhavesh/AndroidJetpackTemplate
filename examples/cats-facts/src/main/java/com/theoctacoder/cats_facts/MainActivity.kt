package com.theoctacoder.cats_facts

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.fermion.android.base.constants.AppThemeType
import com.fermion.android.base.view.BaseActivity
import com.fermion.android.base.view.navigation.Navigation
import com.fermion.android.base.view.theme.AppTheme
import com.fermion.android.base.view.theme.ThemeViewModel
import com.theoctacoder.cats_facts.routes.HomeScreenRoute
import com.theoctacoder.cats_facts.routes.routes
import com.theoctacoder.cats_facts.theme.appDarkScheme
import com.theoctacoder.cats_facts.theme.appLightScheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val viewModel: ThemeViewModel = hiltViewModel()
            AppTheme(
                appThemeType = viewModel.appTheme.collectAsState().value,
                darkTheme = false,
                appLightTheme = appLightScheme,
                appDarkTheme = appDarkScheme,
                dynamicColor = false,
                viewModel = viewModel
            ) {
                Scaffold {
                    ScaffoldDefaults.contentWindowInsets // Fixes blank screen for Xiaomi devices
                    Box(
                        modifier = Modifier
                            .padding(it)
                    )
                    Column {
                        Icon(
                            painter = painterResource(id = if (viewModel.appTheme.collectAsState().value == AppThemeType.Light) com.fermion.android.base.R.drawable.baseline_sunny_24 else com.fermion.android.base.R.drawable.baseline_dark_mode_24),
                            contentDescription = "Theme", modifier = Modifier
                                .clickable {
                                    if (viewModel.appTheme.value == AppThemeType.Light) {
                                        viewModel.setTheme(AppThemeType.Dark)
                                    } else {
                                        viewModel.setTheme(AppThemeType.Light)
                                    }
                                }
                                .size(50.dp)
                                .padding(10.dp)
                                .align(Alignment.End),
                            tint = if (viewModel.appTheme.collectAsState().value == AppThemeType.Light) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                        AppNavigation()

                    }
                }

            }
        }
    }

    @Composable
    override fun showProgressBar() {
        println("Dialog Called fdfdfdfdfd")
        val context = LocalContext.current
        ProgressDialog(showDialog = true)
    }

    @Composable
    override fun hideProgressBar() {
    }

    @Composable
    fun AppNavigation() {
        val context = LocalContext.current
        val navController = rememberNavController()
        Navigation(navController,
            startDestination = HomeScreenRoute.routeName,
            navGraphScreenList = {
                routes.forEach { screen ->
                    composable(screen.routeName) {
                        screen.view.invoke(navController, context, it.arguments, null)
                    }
                }
            })
    }

    @Composable
    fun ProgressDialog(
        showDialog: Boolean, onDismissRequest: () -> Unit = {}
    ) {
        if (showDialog) {
//            Dialog(onDismissRequest = onDismissRequest,) {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context).components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context).data(data = R.raw.main).apply(block = {})
                            .build(), imageLoader = imageLoader
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(100.dp))                       // clip to the circle shape
                )

            }
        }
    }
}

