package com.fermion.android.base.extensions

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.fermion.android.base.network.NetworkResult


/**
 * Created by Bhavesh Auodichya.
 *
 * Adds basic composable extensions.
 *
 **
 *@since 1.0.0
 */


fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.pagingLoadingState(
    isLoaded: (pagingState: Boolean) -> Unit,
) {
    this.apply {
        when {
            // data is loading for first time
            loadState.refresh is LoadState.Loading -> {
                isLoaded(true)
            }
            // data is loading for second time or pagination
            loadState.append is LoadState.Loading -> {
                isLoaded(true)
            }

            loadState.refresh is LoadState.NotLoading -> {
                isLoaded(false)
            }

            loadState.append is LoadState.NotLoading -> {
                isLoaded(false)
            }
        }
    }
}

fun <T : Any> MutableState<NetworkResult<T>?>.pagingLoadingState(isLoaded: (pagingState: Boolean) -> Unit) {
    when (this.value) {
        is NetworkResult.Success<T> -> {
            isLoaded(false)
        }

        is NetworkResult.Loading -> {
            isLoaded(true)
        }

        is NetworkResult.Error -> {
            isLoaded(false)
        }

        is NetworkResult.HttpError<*> -> {
            isLoaded(false)
        }

        else -> {

        }
    }
}

fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}