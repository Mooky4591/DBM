package com.example.dbm.presentation

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToastButton(text: String) {
    val context = LocalContext.current

    Button(onClick = {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }) {
        Text(text = "Show Toast")
    }
}
