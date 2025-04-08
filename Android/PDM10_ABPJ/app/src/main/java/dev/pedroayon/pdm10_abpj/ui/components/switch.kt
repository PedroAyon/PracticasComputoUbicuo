import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WifiBluetoothToggle() {
    var isWifiEnabled by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .padding(16.dp)
            .size(100.dp),
        shape = CircleShape,
        color = if (isWifiEnabled) Color(0xFF42A5F5) else Color(0xFF26A69A),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isWifiEnabled = !isWifiEnabled },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = isWifiEnabled,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                }
            ) { wifiOn ->
                Icon(
                    imageVector = if (wifiOn) Icons.Filled.Wifi else Icons.Filled.Bluetooth,
                    contentDescription = if (wifiOn) "Wi-Fi Enabled" else "Bluetooth Enabled",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isWifiEnabled) "Wi-Fi" else "Bluetooth",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWifiBluetoothToggle() {
    WifiBluetoothToggle()
}
