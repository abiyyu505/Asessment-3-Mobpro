package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.abiyyu0003.asessment2mobpro.R
import com.abiyyu0003.asessment2mobpro.database.CatatanDb
import com.abiyyu0003.asessment2mobpro.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    id: Long = 0L
) {

    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)

    val viewModel: DetailViewModel = viewModel(
        factory = factory
    )

    var judul by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != 0L) {
            val data = viewModel.getCatatan(id)

            if (data != null) {
                judul = data.judul_materi
                isi = data.isi_materi
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            if (id == 0L)
                                stringResource(R.string.tambah_catatan)
                            else
                                stringResource(R.string.ubah_catatan)
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.tombol_kembali)
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {

                            if (judul.isNotBlank() && isi.isNotBlank()) {

                                if (id == 0L) {
                                    viewModel.insert(
                                        judul = judul,
                                        isi = isi
                                    )
                                } else {
                                    viewModel.update(
                                        id = id,
                                        judul = judul,
                                        isi = isi
                                    )
                                }

                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.tombol_simpan)
                        )
                    }

                    if (id != 0L) {

                        IconButton(
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.hapus_materi)
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    showDialog = true
                                }
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        FormCatatan(
            title = judul,
            onTitleChange = {
                judul = it
            },
            desc = isi,
            onDescChange = {
                isi = it
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showDialog) {

        DisplayAlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            onConfirmation = {

                viewModel.delete(id)

                navController.popBackStack()
            }
        )
    }
}

@Composable
fun FormCatatan(
    title: String,
    onTitleChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(16.dp)
    ) {

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = {
                Text(
                    text = stringResource(R.string.judul_materi)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = onDescChange,
            label = {
                Text(
                    text = stringResource(R.string.isi_materi)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            minLines = 5
        )
    }
}