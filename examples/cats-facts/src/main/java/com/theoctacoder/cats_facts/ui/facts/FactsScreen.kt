package com.theoctacoder.cats_facts.ui.facts

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.fermion.android.base.view.BaseView
import com.theoctacoder.cats_facts.R


class FactsScreen : BaseView<FactsViewModel>() {
    private val cattinoFontFamily = FontFamily(Font(R.font.cattino))

    @Composable
    override fun provideViewModel(navHostController: NavHostController?): FactsViewModel {
        return hiltViewModel()
    }

    @Composable
    override fun Bind(navHostController: NavHostController) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        if (!viewModel.callOnce) {
            viewModel.getCatFact()
        }
        Scaffold {
            Box(modifier = Modifier.padding(it))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if ((viewModel.catFactListener.collectAsState().value?.fact ?: "").isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxHeight(0.4f)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context).data(data = R.raw.cat_fun)
                                    .apply(block = {
                                    }).build(), imageLoader = imageLoader
                            ),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 40.dp, top = 30.dp)
                                .clip(RoundedCornerShape(5.dp)),
                        )
                    }
                }
                Text(
                    text = viewModel.catFactListener.collectAsState().value?.fact ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = cattinoFontFamily,
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onBackground
//                    color =
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    viewModel.getCatFact()
                }) {
                    Text(
                        text = stringResource(id = R.string.give_me_more_cat_facts),
                        fontSize = 20.sp,
                        fontFamily = cattinoFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = { navHostController.popBackStack() }) {
                    Text(
                        text = stringResource(id = R.string.i_want_to_leave_i_am_getting_pawddicted),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = cattinoFontFamily
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FactsScreenPreview() {
    FactsScreen().Init(rememberNavController(), LocalContext.current, null, null)

}

