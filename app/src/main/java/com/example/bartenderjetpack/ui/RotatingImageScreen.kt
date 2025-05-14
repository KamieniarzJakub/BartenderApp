package com.example.bartenderjetpack.ui

import android.hardware.Sensor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer // Use graphicsLayer for transformations
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bartenderjetpack.R

@Composable
fun RotatingImageScreen() {

    // Get the calculated rotation angle from the sensor hook
    // Use TYPE_ROTATION_VECTOR for better results if available
    val rotationDegrees by rememberSensorRotation(
        sensorType = Sensor.TYPE_ROTATION_VECTOR,
        maxTiltDegrees = 25f, // Tilting phone up to 25 degrees
        maxImageRotationDegrees = 25f // results in image rotation up to 6 degrees
    )

    // --- Alternative: Using Accelerometer (less ideal for orientation) ---
    // val rotationDegrees by rememberSensorRotation(
    //      sensorType = Sensor.TYPE_ACCELEROMETER,
    //      maxTiltDegrees = 30f, // Tilting phone up to 30 degrees
    //      maxImageRotationDegrees = 5f // results in image rotation up to 5 degrees
    // )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tilt the phone to rotate the image!")
        Spacer(Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.drink), // <--- **Replace with your image resource ID**
            contentDescription = "Image rotating based on phone tilt",
            modifier = Modifier
                .size(250.dp) // Adjust size as needed
                // Apply the rotation using graphicsLayer
                .graphicsLayer {
                    // rotationZ applies rotation around the Z-axis (standard 2D rotation)
                    rotationZ = rotationDegrees
                    // You could also experiment with rotationX and rotationY
                    // to simulate a 3D tilt effect, but rotationZ is most common
                    // for a "parallax" or slight shift effect based on device tilt.
                    // rotationX = -rotationDegrees // Example: Apply tilt to simulate perspective
                    // rotationY = rotationDegrees // Example: Apply tilt to simulate perspective
                    // Note: combining these requires careful consideration of perspective effects
                }
        )

        Spacer(Modifier.height(24.dp))
        // Optional: Display the current rotation value
        Text("Current Image Rotation: ${"%.2f".format(rotationDegrees)}°")
        if (rotationDegrees  == 0f) { // Simple check if sensor might be missing or not active yet
            Text("Sensor data not active or sensor not found.")
        }
    }
}

// --- Preview (sensors won't work in preview) ---
@Preview(showBackground = true)
@Composable
fun PreviewRotatingImageScreen() {
    // In preview, rotationDegrees will be 0f as sensors aren't available
    RotatingImageScreen()
}