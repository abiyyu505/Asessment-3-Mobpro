package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.abiyyu0003.asessment2mobpro.model.User

@Composable
fun ProfilDialog(
    user: User,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Profil")
        },

        text = {

            Column {

                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = null,
                    modifier =
                        Modifier.size(96.dp)
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(user.name)

                Text(user.email)
            }
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Tutup")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onLogout
            ) {
                Text("Logout")
            }
        }
    )
}