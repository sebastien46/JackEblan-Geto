package com.feature.appsettings

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.designsystem.icon.GetoIcons
import com.core.model.AppSettings
import com.core.ui.AppSettingsItem
import com.core.ui.EmptyListPlaceHolderScreen
import com.core.ui.LoadingPlaceHolderScreen

@Composable
internal fun AppSettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: AppSettingsViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit,
    onOpenAddSettingsDialog: (String) -> Unit,
    onOpenCopyPermissionCommandDialog: () -> Unit
) {
    val context = LocalContext.current

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val appSettingsUiState = viewModel.appSettingsUiState.collectAsStateWithLifecycle().value

    val snackBar = viewModel.snackBar.collectAsStateWithLifecycle().value

    val launchAppIntent = viewModel.launchAppIntent.collectAsStateWithLifecycle().value

    val commandPermissionDialog =
        viewModel.commandPermissionDialog.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = snackBar) {
        snackBar?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearSnackBar()
        }
    }

    LaunchedEffect(key1 = launchAppIntent) {
        launchAppIntent?.let {
            context.startActivity(it)
            viewModel.clearLaunchAppIntent()
        }
    }

    LaunchedEffect(key1 = commandPermissionDialog) {
        if (commandPermissionDialog) {
            onOpenCopyPermissionCommandDialog()
            viewModel.clearCommandPermissionDialog()
        }
    }

    AppSettingsScreen(modifier = modifier,
                      snackbarHostState = snackbarHostState,
                      appName = viewModel.appName,
                      appSettingsUiState = appSettingsUiState,
                      onNavigationIconClick = {
                          onNavigationIconClick()
                      },
                      onRevertSettingsIconClick = {
                          viewModel.onEvent(
                              AppSettingsEvent.OnRevertSettings
                          )
                      },
                      onAppSettingsItemCheckBoxChange = { checked, userAppSettingsItem ->
                          viewModel.onEvent(
                              AppSettingsEvent.OnAppSettingsItemCheckBoxChange(
                                  checked = checked, appSettings = userAppSettingsItem
                              )
                          )
                      },
                      onDeleteAppSettingsItem = {
                          viewModel.onEvent(AppSettingsEvent.OnDeleteAppSettingsItem(it))
                      },
                      onAddAppSettingsClick = { onOpenAddSettingsDialog(viewModel.packageName) },
                      onLaunchApp = {
                          viewModel.onEvent(
                              AppSettingsEvent.OnLaunchApp
                          )
                      })
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppSettingsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    appName: String,
    appSettingsUiState: AppSettingsUiState,
    onNavigationIconClick: () -> Unit,
    onRevertSettingsIconClick: () -> Unit,
    onAppSettingsItemCheckBoxChange: (Boolean, AppSettings) -> Unit,
    onDeleteAppSettingsItem: (AppSettings) -> Unit,
    onAddAppSettingsClick: () -> Unit,
    onLaunchApp: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = appName, maxLines = 1)
        }, navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = GetoIcons.Back, contentDescription = "Navigation icon"
                )
            }
        })
    }, bottomBar = {
        BottomAppBar(actions = {
            IconButton(onClick = onRevertSettingsIconClick) {
                Icon(
                    imageVector = GetoIcons.Refresh, contentDescription = "Revert icon"
                )
            }
            IconButton(onClick = onAddAppSettingsClick) {
                Icon(
                    GetoIcons.Add,
                    contentDescription = "Add icon",
                )
            }
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = onLaunchApp,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = GetoIcons.Android, contentDescription = "Launch icon"
                )
            }
        })
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState, modifier = Modifier.testTag("appsettings:snackbar")
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
        ) {
            when (appSettingsUiState) {
                AppSettingsUiState.Empty -> {
                    EmptyListPlaceHolderScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("appsettings:emptyListPlaceHolderScreen"),
                        icon = GetoIcons.Empty,
                        text = "Nothing is here"
                    )
                }

                AppSettingsUiState.Loading -> {
                    LoadingPlaceHolderScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("appsettings:loadingPlaceHolderScreen")
                    )
                }

                is AppSettingsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), contentPadding = innerPadding
                    ) {
                        appSettings(
                            appSettingsList = appSettingsUiState.appSettingsList,
                            onAppSettingsItemCheckBoxChange = onAppSettingsItemCheckBoxChange,
                            onDeleteAppSettingsItem = onDeleteAppSettingsItem
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.appSettings(
    modifier: Modifier = Modifier,
    appSettingsList: List<AppSettings>,
    onAppSettingsItemCheckBoxChange: (Boolean, AppSettings) -> Unit,
    onDeleteAppSettingsItem: (AppSettings) -> Unit,
) {
    items(appSettingsList) { appSettings ->
        AppSettingsItem(
            modifier = modifier,
            enabled = appSettings.enabled,
            label = appSettings.label,
            settingsTypeLabel = appSettings.settingsType.label,
            key = appSettings.key,
            onUserAppSettingsItemCheckBoxChange = { check ->
                onAppSettingsItemCheckBoxChange(
                    check, appSettings
                )
            },
            onDeleteUserAppSettingsItem = {
                onDeleteAppSettingsItem(appSettings)
            },
            safeToWrite = appSettings.safeToWrite
        )
    }
}