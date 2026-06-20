package com.abiyyu0003.asessment2mobpro.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abiyyu0003.asessment2mobpro.BuildConfig
import com.abiyyu0003.asessment2mobpro.R
import com.abiyyu0003.asessment2mobpro.database.CatatanDb
import com.abiyyu0003.asessment2mobpro.model.Catatan
import com.abiyyu0003.asessment2mobpro.navigation.Screen
import com.abiyyu0003.asessment2mobpro.util.SettingsDataStore
import com.abiyyu0003.asessment2mobpro.util.ViewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.staggeredgrid.items as staggeredItems
import com.abiyyu0003.asessment2mobpro.model.User
import com.abiyyu0003.asessment2mobpro.util.UserDataStore
import androidx.credentials.ClearCredentialStateRequest
import kotlinx.coroutines.flow.first


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val userDataStore = remember { UserDataStore(context) }
    val user by userDataStore.userFlow.collectAsState(initial = User())
    LaunchedEffect(user) {
        Log.d("USER_DEBUG", "Name=${user.name}")
        Log.d("USER_DEBUG", "Email=${user.email}")
        Log.d("USER_DEBUG", "Photo=${user.photoUrl}")
    }
    var showProfileDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dataStore = SettingsDataStore(context)

    val showList by dataStore.layoutFlow.collectAsState(initial = true)
    val isDarkTheme by dataStore.themeFlow.collectAsState(initial = true)
    var showAbout by remember {
        mutableStateOf(false)
    }

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

                    IconButton(
                        onClick = {
                            showAbout = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                R.drawable.baseline_info_24
                            ),
                            contentDescription = "Tentang"
                        )
                    }

                    IconButton(
                        onClick = {
                            scope.launch {
                                dataStore.saveTheme(!isDarkTheme)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isDarkTheme) {
                                    R.drawable.baseline_light_mode_24
                                } else {
                                    R.drawable.baseline_dark_mode_24
                                }
                            ),
                            contentDescription = stringResource(
                                if (isDarkTheme) {
                                    R.string.mode_terang
                                } else {
                                    R.string.mode_gelap
                                }
                            )
                        )
                    }

                    IconButton(
                        onClick = {

                            if (user.email.isEmpty()) {

                                scope.launch {

                                    signIn(
                                        context,
                                        userDataStore
                                    )
                                }

                            } else {

                                showProfileDialog = true
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_account_circle),
                            contentDescription = stringResource(R.string.profil
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor =
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        Screen.FormBaru.route
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription =
                        stringResource(R.string.tombol_tambah)
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

    if (showAbout) {
        AlertDialog(
            onDismissRequest = {
                showAbout = false
            },
            title = {
                Text(text = "StudyNotes")
            },
            text = {
                Text(
                    text =
                        "Versi 1.0\n\n" +
                                "Dibuat oleh:\n" +
                                "Muhammad Abiyyu Fawwaz J.\n\n" +
                                "StudyNotes adalah aplikasi " +
                                "pencatatan materi belajar " +
                                "yang menggunakan Room Database " +
                                "dan Retrofit API."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAbout = false
                    }
                ) {
                    Text("Tutup")
                }
            }
        )
    }
    if (showProfileDialog) {

        ProfilDialog(

            user = user,

            onDismiss = {

                showProfileDialog = false
            },

            onLogout = {

                scope.launch {

                    signOut(
                        context,
                        userDataStore
                    )

                    showProfileDialog = false
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenContent(modifier: Modifier = Modifier, navController: NavHostController, showList: Boolean) {

    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    val jumlahCatatan by viewModel.jumlahCatatan.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val refreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = { viewModel.insertDataFromApi() })
    var query by remember { mutableStateOf("") }
    var selectedCatatan by remember { mutableStateOf<Catatan?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {
    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    R.drawable.logo_study_notes
                ),
                contentDescription =
                    stringResource(R.string.logo_study_notes),
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(R.string.data_kosong),
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {

        if (showList) {

            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(
                    bottom = 84.dp
                )
            ) {

                item {
                    HeaderLogo()
                }

                item {
                    Text(
                        text = "Jumlah Catatan: $jumlahCatatan",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.search(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        label = {
                            Text("Cari Catatan")
                        },
                        singleLine = true
                    )
                }

                lazyItems(data) { catatan ->

                    ListItem(catatan = catatan) {
                        selectedCatatan = catatan
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    HorizontalDivider()
                }
            }

        } else {

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier,
                contentPadding = PaddingValues(8.dp)
            ) {

                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    HeaderLogo()
                }

                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Text(
                        text = "Jumlah Catatan: $jumlahCatatan",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.search(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        label = {
                            Text("Cari Catatan")
                        },
                        singleLine = true
                    )
                }

                staggeredItems(data) { catatan ->
                    GridItem(catatan = catatan) {
                        selectedCatatan = catatan
                    }
                }
            }
        }
    }
    selectedCatatan?.let { catatan ->

        AlertDialog(
            onDismissRequest = {
                selectedCatatan = null
            },
            title = {
                Text(catatan.judul_materi)
            },
            text = {
                Column {

                    Text(
                        text = catatan.isi_materi
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Tanggal: ${catatan.tanggal}",
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        selectedCatatan = null

                        navController.navigate(
                            Screen.FormUbah
                                .withId(catatan.id)
                        )
                    }
                ) {
                    Text("Edit")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        selectedCatatan = null
                    }
                ) {
                    Text("Tutup")
                }
            }
        )
    }
        PullRefreshIndicator(
            refreshing = isLoading,
            state = refreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}
@Composable
fun HeaderLogo(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo_study_notes),
                contentDescription = stringResource(R.string.logo_study_notes),
                modifier = Modifier.size(96.dp)
            )

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.welcome_text),
                    style = MaterialTheme.typography.bodyMedium
                )
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
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = catatan.tanggal,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}
private suspend fun signIn(
    context: Context,
    userDataStore: UserDataStore
) {

    Log.d("LOGIN_DEBUG", "signIn dipanggil")

    try {

        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setServerClientId(BuildConfig.API_KEY)
                .setFilterByAuthorizedAccounts(false)
                .build()

        Log.d("LOGIN_DEBUG", "googleIdOption dibuat")

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        Log.d("LOGIN_DEBUG", "request dibuat")

        val credentialManager =
            CredentialManager.create(context)

        Log.d("LOGIN_DEBUG", "sebelum getCredential")

        val result =
            credentialManager.getCredential(
                context,
                request
            )

        Log.d("LOGIN_DEBUG", "sesudah getCredential")

        handleSignIn(
            result,
            userDataStore
        )

    } catch (e: Exception) {

        Log.e(
            "LOGIN_ERROR",
            e.toString()
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
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = catatan.tanggal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}


private suspend fun handleSignIn(
    result: GetCredentialResponse,
    userDataStore: UserDataStore
) {

    val credential = result.credential

    Log.d(
        "LOGIN_DEBUG",
        "Credential Type = ${credential.type}"
    )

    if (
        credential.type ==
        GoogleIdTokenCredential
            .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {

        Log.d(
            "LOGIN_DEBUG",
            "Masuk blok GoogleIdTokenCredential"
        )

        try {

            val googleCredential =
                GoogleIdTokenCredential
                    .createFrom(
                        credential.data
                    )
            Log.d("LOGIN_DEBUG", "DisplayName=${googleCredential.displayName}")
            Log.d("LOGIN_DEBUG", "Email=${googleCredential.id}")

            userDataStore.saveData(
                User(
                    name = googleCredential.displayName ?: "",
                    email = googleCredential.id,
                    photoUrl = googleCredential.profilePictureUri?.toString() ?: ""
                )
            )

            val savedUser =
                userDataStore.userFlow.first()

            Log.d(
                "LOGIN_DEBUG",
                "Saved Email = ${savedUser.email}"
            )

            Log.d("LOGIN_DEBUG", "Data berhasil disimpan")
            Log.d("LOGIN_DEBUG", "Masuk handleSignIn")

            Log.d("LOGIN_DEBUG", "Credential Type = ${credential.type}")

        } catch (
            e: GoogleIdTokenParsingException
        ) {

            Log.e(
                "LOGIN_ERROR",
                e.toString()
            )
        }
    }
}

private suspend fun signOut(
    context: Context,
    userDataStore: UserDataStore
) {

    CredentialManager
        .create(context)
        .clearCredentialState(
            ClearCredentialStateRequest()
        )

    userDataStore.clearData()
}