package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abiyyu0003.asessment2mobpro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    id: Long = 0L
) {
    val judul = remember { mutableStateOf("") }
    val isi = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == 0L) {
                            stringResource(R.string.tambah_catatan)
                        } else {
                            stringResource(R.string.ubah_catatan)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.tombol_kembali)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.tombol_simpan)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        FormCatatan(
            title = judul.value,
            onTitleChange = { judul.value = it },
            desc = isi.value,
            onDescChange = { isi.value = it },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FormCatatan(
    title: String,
    onTitleChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.judul_materi)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { onDescChange(it) },
            label = { Text(text = stringResource(R.string.isi_materi)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}