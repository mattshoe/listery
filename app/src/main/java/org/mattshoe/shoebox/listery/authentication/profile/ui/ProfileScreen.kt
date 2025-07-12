package org.mattshoe.shoebox.listery.authentication.profile.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.profile.viewmodel.ProfileIntent
import org.mattshoe.shoebox.listery.authentication.profile.viewmodel.ProfileState
import org.mattshoe.shoebox.listery.authentication.profile.viewmodel.ProfileViewModel
import org.mattshoe.shoebox.listery.ui.common.Level2AppBar
import org.mattshoe.shoebox.listery.ui.common.ListeryCard
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryScaffold
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.TopBarIcon
import org.mattshoe.shoebox.listery.ui.BottomNavItem

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.handleIntent(ProfileIntent.UploadPhoto(it)) }
    }

    ListeryScaffold(
        topBar = {
            Level2AppBar(
                actions = listOf()
            )
        },
        selectedNavItem = BottomNavItem.Cookbook,
        showFab = false
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                ProfilePhotoSection(
                    state = state,
                    onPhotoClick = {
                        imagePickerLauncher.launch("image/*")
                    }
                )
            }
            
            item {
                ProfileInfoSection(
                    state = state,
                    onIntent = { viewModel.handleIntent(it) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                LogoutButton {
                    viewModel.handleIntent(it)
                }
            }
        }
    }
}

@Composable
private fun ProfilePhotoSection(
    state: ProfileState,
    onPhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(enabled = !state.isPhotoUploading) { onPhotoClick() }
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (state.isPhotoUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                if (state.user?.photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.user.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (state.isPhotoUploading) "Uploading..." else "Tap to change photo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileInfoSection(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit
) {
    ListeryCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (!state.isEditing) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onIntent(ProfileIntent.EditProfile) },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            HorizontalDivider()
            
            // Email (read-only)
            Column {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.user?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // First Name
            if (state.isEditing) {
                ListeryTextInput(
                    value = state.firstName,
                    placeholder = "First Name",
                    onValueChange = { onIntent(ProfileIntent.FirstNameChanged(it)) }
                )
            } else {
                ProfileInfoRow("First Name", state.firstName.value)
            }
            
            // Last Name
            if (state.isEditing) {
                ListeryTextInput(
                    value = state.lastName,
                    placeholder = "Last Name",
                    onValueChange = { onIntent(ProfileIntent.LastNameChanged(it)) }
                )
            } else {
                ProfileInfoRow("Last Name", state.lastName.value)
            }
            
//            // Phone Number
//            if (state.isEditing) {
//                ListeryTextInput(
//                    value = state.phoneNumber,
//                    placeholder = "Phone Number",
//                    onValueChange = { onIntent(ProfileIntent.PhoneNumberChanged(it)) }
//                )
//            } else {
//                ProfileInfoRow("Phone Number", state.phoneNumber.value)
//            }
            
            // Error message
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            // Action buttons
            if (state.isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onIntent(ProfileIntent.CancelEdit) },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Cancel")
                    }
                    
                    ListeryPrimaryButton(
                        text = if (state.isLoading) "Saving..." else "Save",
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading,
                        onClick = { onIntent(ProfileIntent.SaveProfile) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.ifEmpty { "Not provided" },
            style = MaterialTheme.typography.bodyMedium,
            color = if (value.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LogoutButton(
    handleIntent: (ProfileIntent) -> Unit
) {
    Button(
        onClick = {
            handleIntent(ProfileIntent.Logout)
        },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(40.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = "Sign out",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}