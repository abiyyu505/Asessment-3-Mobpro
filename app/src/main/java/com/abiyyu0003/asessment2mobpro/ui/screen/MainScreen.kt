package com.abiyyu0003.mobpro1.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abiyyu0003.asessment2mobpro.R
import com.abiyyu0003.asessment2mobpro.model.Catatan
import com.abiyyu0003.asessment2mobpro.ui.screen.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, R.string.tambah_catatan, Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tombol_tambah)
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val data = viewModel.data
    val context = LocalContext.current

    if (data.isEmpty()) {
        Text(
            text = stringResource(R.string.data_kosong),
            modifier = modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            items(data) { catatan ->
                ListItem(catatan = catatan) {
                    Toast.makeText(context, catatan.judul_materi, Toast.LENGTH_SHORT).show()
                }
                HorizontalDivider()
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