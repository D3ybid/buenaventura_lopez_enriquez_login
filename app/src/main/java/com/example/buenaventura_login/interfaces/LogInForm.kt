package com.example.buenaventura_login.interfaces

import android.widget.Toast
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.buenaventura_login.ui.theme.Buenaventura_loginTheme
import com.example.buenaventura_login.data.Credentials
import com.example.buenaventura_login.data.User
import com.example.buenaventura_login.data.UserRepository


@Composable
fun LogInForm() {
    var credentials by remember { mutableStateOf(Credentials()) }
    var loggedInUser by remember { mutableStateOf<User?>(null) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var isUserDetailsDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loggedInUser == null) {

            // Show login fields and buttons if not logged in
            LogInField(
                value = credentials.login,
                onChange = { data -> credentials = credentials.copy(login = data) }
            )

            PasswordField(
                value = credentials.pwd,
                onChange = { data -> credentials = credentials.copy(pwd = data) },
                submit = {
                    val user = checkCredentials(credentials, context)
                    if (user != null) {
                        loggedInUser = user
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val user = checkCredentials(credentials, context)
                    if (user != null) {
                        loggedInUser = user
                    }
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Log In")
            }
        } else {
            // Show User List if logged in
            UserList(
                users = UserRepository.users,
                onUserClick = { clickedUser ->
                    selectedUser = clickedUser
                    isUserDetailsDialogVisible = true
                },
                onLogoutClick = {
                    loggedInUser = null
                    selectedUser = null
                },
            )
        }

        // Display UserDetails when a user is selected
        selectedUser?.let { user ->
            if (isUserDetailsDialogVisible) {
                UserDetailsDialog(user = user, onDismiss = {
                    isUserDetailsDialogVisible = false
                    selectedUser = null
                })
            }
        }
    }
}

@Composable
fun UserDetailsDialog(user: User, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        UserDetailsScreen(user = user, onBackClick = onDismiss)
    }
}


@Composable
fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit,
    onLogoutClick: () -> Unit,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User List", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying user tabs
        for (user in users) {
            Button(
                onClick = {
                    // Invoke onUserClick when a user button is clicked
                    onUserClick(user)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "${user.firstName} ${user.lastName}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout button
        Button(onClick = onLogoutClick) {
            Text(text = "Logout")
        }
    }
}

fun checkCredentials(creds: Credentials, context: Context): User? {
    val user = UserRepository.findUserByEmail(creds.login)

    return if (user != null && creds.isNotEmpty() && creds.pwd == user.password) {
        // Login successful
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        user
    } else {
        // Display an error message for incorrect credentials
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,  // Corrected parameter name
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password"
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Lock,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInField(
    value : String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label : String = "Email",
    placeholder : String = "Enter your Login"
){
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable{
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {focusManager.moveFocus(FocusDirection.Down)}
        ),
        placeholder = { Text(placeholder)},
        label = { Text(label)},
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}
@Composable
fun UserDetailsScreen(user: User, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // You can adjust the padding if needed
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("User Details: ${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email: ${user.email}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Favorite Food: ${user.favoriteFood}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_3a")
@Composable
fun LogInFormPreview() {
    Buenaventura_loginTheme {
        LogInForm()
    }
}