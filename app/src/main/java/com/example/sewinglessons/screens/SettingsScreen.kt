package com.example.sewinglessons.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.AccountViewModel
import com.example.sewinglessons.R
import com.example.sewinglessons.SettingsViewModel
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler
import com.example.sewinglessons.ui.theme.BgColor
import com.example.sewinglessons.ui.theme.Primary
import com.example.sewinglessons.ui.theme.componentShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(accountViewModel: AccountViewModel = viewModel(), settingsViewModel: SettingsViewModel = viewModel()) {
    var writtenEmail by remember { mutableStateOf("") }
    var email = accountViewModel.emailId?.value ?: ""
    val emailSent = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface (
        color = colorResource(id = R.color.colorWhite),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorWhite))
            .padding(24.dp)
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Settings ",
                color = colorResource(id = R.color.colorPrimary),
                fontSize = 28.sp,
                style = TextStyle(textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.padding(40.dp))
            Text(text = stringResource(id = R.string.can_do),
                color = Color.Black,
                style = TextStyle(
                    textAlign = TextAlign.Justify
                )
            )
            Spacer(modifier = Modifier.padding(210.dp))
            Text(text = stringResource(id = R.string.what_to_do),
                color = Color.Black,
                style = TextStyle(
                    textAlign = TextAlign.Justify
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = writtenEmail,
                onValueChange = { writtenEmail = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(componentShapes.small),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Primary,
                    focusedLabelColor =  Primary,
                    cursorColor = Primary,
                    containerColor = BgColor
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    settingsViewModel.sendPasswordResetEmail(email) { emailSuccessfullySent ->
                        if (emailSuccessfullySent) {
                            Toast.makeText(
                                context,
                                "Password reset email sent Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            Toast.makeText(
                                context,
                                "Password reset email Failed to send",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.colorPrimary),
                    contentColor = Color.White
                ), enabled = writtenEmail == email
            ) {
                Text(text = "Reset Password")
            }
        }
    }

    SystemBackButtonHandler {
        SewingAppRouter.navigateTo(Screen.AccountScreen)
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}