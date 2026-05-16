package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abiyyu0003.asessment2mobpro.R
import com.abiyyu0003.asessment2mobpro.database.CatatanDb
import com.abiyyu0003.asessment2mobpro.model.Catatan
import com.abiyyu0003.asessment2mobpro.navigation.Screen
import com.abiyyu0003.asessment2mobpro.util.SettingsDataStore
import com.abiyyu0003.asessment2mobpro.util.ViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = SettingsDataStore(context)
    val showList by dataStore.layoutFlow.collectAsState(initial = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                dataStore.saveLayout(!showList)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (showList) {
                                    R.drawable.baseline_grid_view_24
                                } else {
                                    R.drawable.baseline_view_list_24
                                }
                            ),
                            contentDescription = stringResource(
                                if (showList) {
                                    R.string.grid_view
                                } else {
                                    R.string.list_view
                                }
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tombol_tambah)
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            showList = showList
        )
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, navController: NavHostController, showList: Boolean) {

    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Text(
            text = stringResource(R.string.data_kosong),
            modifier = modifier.padding(16.dp)
        )
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) { catatan ->
                    ListItem(catatan = catatan) {
                        navController.navigate(Screen.FormUbah.withId(catatan.id))
                    }
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier,
                contentPadding = PaddingValues(8.dp)
            ) {
                items(data) { catatan ->
                    GridItem(catatan = catatan) {
                        navController.navigate(Screen.FormUbah.withId(catatan.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(
    catatan: Catatan,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = catatan.judul_materi,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = catatan.isi_materi,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = catatan.tanggal,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun GridItem(
    catatan: Catatan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = catatan.judul_materi,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = catatan.isi_materi,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = catatan.tanggal,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}