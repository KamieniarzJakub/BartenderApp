package com.example.bartenderjetpack.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlin.math.atan2

@Composable
fun rememberSensorRotation(
    sensorType: Int = Sensor.TYPE_ROTATION_VECTOR, // TYPE_ACCELEROMETER is an alternative
    sensorDelay: Int = SensorManager.SENSOR_DELAY_UI,
    maxTiltDegrees: Float = 20f, // Adjust this: How much phone tilt (e.g., 20 degrees)
    maxImageRotationDegrees: Float = 5f // corresponds to how much image rotation (e.g., 5 degrees)
): State<Float> {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor = remember(sensorType) { sensorManager.getDefaultSensor(sensorType) }

    // State to hold the calculated rotation angle
    val targetRotationDegreesState = remember { mutableFloatStateOf(0f) }

    if (sensor == null) {
        return targetRotationDegreesState
    }

    // Effect to manage sensor listener registration and unregistration
    DisposableEffect(sensor, sensorDelay, maxTiltDegrees, maxImageRotationDegrees) {
        val listener = object : SensorEventListener {
            // Reuse arrays to avoid allocation pressure in hot loops
            val rotationMatrix = FloatArray(9)
            val orientationAngles = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == sensorType) {
                    val currentRotation = when (sensorType) {
                        Sensor.TYPE_ROTATION_VECTOR -> {
                            // Calculate orientation angles from rotation vector data
                            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                            SensorManager.getOrientation(rotationMatrix, orientationAngles)

                            // orientationAngles[1] is Pitch (rotation around X axis - forward/backward tilt)
                            // orientationAngles[2] is Roll (rotation around Y axis - left/right tilt)

                            // Use Roll for a typical "slight tilt" effect
                            var rollRadians = orientationAngles[2]
                            var rollDegrees = Math.toDegrees(rollRadians.toDouble()).toFloat()

                            // Scale and clamp the rotation for a "slight" effect
                            // Clamp the phone's effective tilt angle
                            rollDegrees = rollDegrees.coerceIn(-maxTiltDegrees, maxTiltDegrees)

                            // Map the clamped phone tilt angle to the desired image rotation angle
                            // Formula: (current_phone_tilt / max_phone_tilt) * max_image_rotation
                            (rollDegrees / maxTiltDegrees) * maxImageRotationDegrees
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            // Alternative: Estimate tilt from accelerometer data (less stable/accurate than Rotation Vector)
                            val accelX = event.values[0]
//                            val accelY = event.values[1]
                            val accelZ = event.values[2]

                            // Estimate roll (rotation around Y) from X and Z acceleration
                            // atan2(opposite, adjacent) - here, X is opposite gravity direction on Y axis, Z is adjacent
                            var rollRadians = atan2(-accelX, accelZ)
                            var rollDegrees = Math.toDegrees(rollRadians.toDouble()).toFloat()

                            // Scale and clamp the rotation for a "slight" effect
                            rollDegrees = rollDegrees.coerceIn(-maxTiltDegrees, maxTiltDegrees)
                            (rollDegrees / maxTiltDegrees) * maxImageRotationDegrees

                            // You could also use pitch: atan2(accelY, accelZ)
                        }
                        else -> 0f // Should not happen
                    }
                    // Update the state on the main thread
                    targetRotationDegreesState.floatValue = currentRotation
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Register the listener
        sensorManager.registerListener(listener, sensor, sensorDelay)

        // Unregister the listener when the Composable leaves the composition
        onDispose {
            sensorManager.unregisterListener(listener)
        }


    }

    return targetRotationDegreesState
}