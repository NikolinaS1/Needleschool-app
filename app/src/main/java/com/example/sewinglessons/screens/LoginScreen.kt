package com.example.sewinglessons.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sewinglessons.LoginViewModel
import com.example.sewinglessons.components.NormalTextComponent
import com.example.sewinglessons.R
import com.example.sewinglessons.components.ButtonComponent
import com.example.sewinglessons.components.ClickableLoginTextComponent
import com.example.sewinglessons.components.MyTextField
import com.example.sewinglessons.components.PasswordFieldComponent
import com.example.sewinglessons.components.TitleTextComponent
import com.example.sewinglessons.data.LoginUIEvent
import com.example.sewinglessons.navigation.Screen
import com.example.sewinglessons.navigation.SewingAppRouter
import com.example.sewinglessons.navigation.SystemBackButtonHandler

@Composable
fun LoginScreen (loginViewModel: LoginViewModel = viewModel()) {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                NormalTextComponent(value = stringResource(id = R.string.welcome))
                TitleTextComponent(value = stringResource(id = R.string.app_name))
                Spacer(modifier = Modifier.height(80.dp))
                NormalTextComponent(value = stringResource(id = R.string.sign_in))
                Spacer(modifier = Modifier.height(20.dp))
                MyTextField(labelValue = stringResource(id = R.string.email), onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                }, errorStatus = loginViewModel.loginUIState.value.emailError)
                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    }, errorStatus = loginViewModel.loginUIState.value.passwordError)
                Spacer(modifier = Modifier.height(60.dp))
                ButtonComponent(value = stringResource(id = R.string.login), onButtonClicked = {
                    loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                }, isEnabled = loginViewModel.allValidationsPassed.value)
                Spacer(modifier = Modifier.height(40.dp))
                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    SewingAppRouter.navigateTo(Screen.SignUpScreen)
                })
            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }
    SystemBackButtonHandler {
        SewingAppRouter.navigateTo(Screen.SignUpScreen)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
