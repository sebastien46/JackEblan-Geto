package com.android.geto.presentation.user_app_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.geto.R
import com.android.geto.common.navigation.NavigationScreen

@Composable
fun UserAppListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserAppListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    StatelessScreen(modifier = modifier, state = state, onItemClick = { packageName, appName ->
        navController.navigate(route = NavigationScreen.UserAppSettings.route + "/$packageName/$appName")
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessScreen(
    modifier: Modifier = Modifier, state: UserAppListState, onItemClick: (String, String) -> Unit
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.apps) { appItem ->

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(appItem.packageName, appItem.label) }
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            modifier = Modifier.size(50.dp),
                            bitmap = appItem.icon.asImageBitmap(),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = appItem.label, style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = appItem.packageName,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}