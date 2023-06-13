/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.uqac.pixelwatch_sensor_data.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.uqac.pixelwatch_sensor_data.R
import com.uqac.pixelwatch_sensor_data.presentation.theme.PixelwatchsensordataTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Check Permissions Capteurs

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BODY_SENSORS), 2)
        }

        var sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        var ppgSensor = sensorManager.getDefaultSensor(65572)
        var galvanicSkinResponse = sensorManager.getDefaultSensor(65554)

        sensorManager.registerListener(
            this,
            galvanicSkinResponse,
            SensorManager.SENSOR_DELAY_NORMAL // Crash si supérieur a NORMAL | Ne fonctionne pas avec PPG
        )

        sensorManager.registerListener(
            this,
            ppgSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )

        setContent {
            WearApp("Android")
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {

        var result =""
        for(value in p0!!.values) {
            result += "$value |"
        }

        Log.d("Sensor${p0.sensor.type}", result)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}

@Composable
fun WearApp(greetingName: String) {
    PixelwatchsensordataTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}