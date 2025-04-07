package com.theoctacoder.cats_facts.ui.home

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.fermion.android.base.view.BaseView
import com.fermion.android.base.view.BaseViewModel
import com.theoctacoder.cats_facts.routes.FactsScreenRoute
import com.theoctacoder.cats_facts.routes.HomeScreenRoute
import com.theoctacoder.cats_facts.R

class HomeScreen : BaseView<BaseViewModel>() {

    @Composable
    override fun provideViewModel(navHostController: NavHostController?): BaseViewModel {
        return hiltViewModel()
    }

    @Composable
    override fun Bind(navHostController: NavHostController) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.cat_facts),
                fontSize = 50.sp,
                lineHeight = TextUnit(60f, TextUnitType.Sp),
                fontFamily = FontFamily(Font(R.font.ennobled_pet)),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 50.dp)
            )
            Button(modifier = Modifier.padding(top = 30.dp), onClick = {
                if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    if (navHostController.currentDestination?.route == HomeScreenRoute.routeName) {
                        navHostController.navigate(FactsScreenRoute.routeName)
                    }
                }
            }) {
                Text(
                    text = stringResource(id = R.string.i_want_cat_facts_give_me),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.cattino))
                )
            }


            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.raw.cat_entry)
                        .apply(block = {}).build(), imageLoader = imageLoader
                ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .clip(RoundedCornerShape(5.dp)),
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen().Init(rememberNavController(), LocalContext.current, null, null)

}
