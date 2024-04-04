package mx.unam.fi.practica2
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import mx.unam.fi.practica2.ui.theme.Practica2Theme

import mx.unam.fi.practica2.model.Practica2Model
import mx.unam.fi.practica2.network.Practica2Api

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable

fun MainScreen() {
    var carModels by remember { mutableStateOf<List<Practica2Model>?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Usamos LaunchedEffect dentro de MainScreen para cargar los datos al iniciar
    LaunchedEffect(Unit) {
        // Llamamos a loadPractica2 dentro de LaunchedEffect
        loadPractica2 { models ->
            carModels = models
            isLoading = false
        }
        isLoading = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Cuando se presiona el botón, se carga la lista de modelos de automóviles
                loadPractica2 { models ->
                    carModels = models
                    isLoading = false
                }
                isLoading = true
            },
            enabled = !isLoading // Deshabilita el botón mientras se carga la información
        ) {
            if (isLoading) {
                Text("Cargando...")
            } else {
                Text("Cargar modelos de automóviles")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        carModels?.let { models ->
            if (models.isNotEmpty()) {
                Column {
                    Text("Modelos de automóviles:")
                    Spacer(modifier = Modifier.height(8.dp))
                    models.forEach { model ->
                        Text(text = "${model.modelName}, ${model.bodyStyle}, ${model.transmissionType}")
                    }
                }
            }
        }
    }
}
@Composable
fun loadPractica2(onLoaded: (List<Practica2Model>) -> Unit) {
    // Llamamos a la función de la API para obtener los modelos de automóviles
    // y los pasamos al callback `onLoaded`.
    // Nota: Esto debe hacerse dentro de un contexto coroutine.

    // Valores de ejemplo para la marca y el año
    val make = "Toyota"
    val year = 2022

    // Utilizamos LaunchedEffect para realizar la llamada a la API de manera asíncrona
    LaunchedEffect(Unit) {
        try {
            // Realizamos la llamada a la API para obtener los modelos de automóviles
            val models = withContext(Dispatchers.IO) {
                Practica2Api.getCarModelsByMakeAndYear(make, year)
            }
            // Llamamos al callback con los modelos obtenidos
            onLoaded(models)
        } catch (e: Exception) {
            // Manejamos cualquier excepción que pueda ocurrir durante la llamada a la API
            // Puedes mostrar un mensaje de error, registrar la excepción, etc.
            Log.e("API", "Error al obtener los modelos de automóviles: ${e.message}", e)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Practica2Theme {
        MainScreen()
    }
}