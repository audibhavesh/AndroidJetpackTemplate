package com.fermion.android.base.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fermion.android.base.R
import com.fermion.android.base.constants.AppThemeType
import com.fermion.android.base.view.theme.ThemePreference
import com.fermion.android.base.view.theme.ThemeViewModel

@Composable
fun SelectThemeDialog(
    themeViewModel: ThemeViewModel,
    setShowDialog: (Boolean) -> Unit,
    returnValue: (AppThemeType) -> Unit,
) {
    Dialog(
        onDismissRequest = { setShowDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                ItemSelectRadioButton(
                    title = "Default Theme",
                    onClick = {
                        if (themeViewModel.appTheme.value != AppThemeType.Default) {
                            ThemePreference.setTheme(AppThemeType.Default)
                            themeViewModel.setTheme(AppThemeType.Default)
                            setShowDialog(false)
                            returnValue(AppThemeType.Default)
                        }
                    },
                    isSelect = themeViewModel.appTheme.collectAsState().value == AppThemeType.Default
                )
                ItemSelectRadioButton(
                    title = "Light Theme",
                    onClick = {
                        if (themeViewModel.appTheme.value != AppThemeType.Light) {
                            themeViewModel.setTheme(AppThemeType.Light)
                            setShowDialog(false)
                            returnValue(AppThemeType.Light)
                        }
                    },
                    isSelect = themeViewModel.appTheme.collectAsState().value == AppThemeType.Light
                )
                ItemSelectRadioButton(
                    title = "Dark Theme",
                    onClick = {
                        if (themeViewModel.appTheme.value != AppThemeType.Dark) {
                            themeViewModel.setTheme(AppThemeType.Dark)
                            setShowDialog(false)
                            returnValue(AppThemeType.Dark)
                        }
                    },
                    isSelect = themeViewModel.appTheme.collectAsState().value == AppThemeType.Dark
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ItemSelectRadioButton(
    title: String,
    onClick: () -> Unit,
    isSelect: Boolean
) {
    Column(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = if (isSelect)
                    painterResource(id = R.drawable.ic_check_box) else
                    painterResource(id = R.drawable.ic_un_check_box),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ShowThemeChooser(viewModel: ThemeViewModel) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it).fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                viewModel.showDialog(true)
            }) {
                Text("Show Theme Dialog")
            }
            if (viewModel.showThemeDialog.collectAsState().value) {
                SelectThemeDialog(themeViewModel = viewModel, setShowDialog = {
                    viewModel.showDialog(it)
                }, returnValue = {})
            }
        }
    }
}