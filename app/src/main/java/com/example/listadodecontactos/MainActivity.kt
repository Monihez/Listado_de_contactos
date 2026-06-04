package com.example.listadodecontactos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listadodecontactos.ui.theme.ListadoDeContactosTheme

data class Contacto(val nombre: String, val telefono: String, val fotoRes: Int)

val contactosEjemplo = listOf(
    Contacto("Juan Pérez", "5551234", 0),
    Contacto("María García", "5555678", 0),
    Contacto("Carlos López", "5559012", 0),
    Contacto("Ana Martínez", "5553456", 0),
    Contacto("Pedro Rodríguez", "5557890", 0),
    Contacto("Lucía Sánchez", "5552345", 0),
    Contacto("Roberto Gómez", "5556789", 0),
    Contacto("Elena Díaz", "5550123", 0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListadoDeContactosTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaContactosScreen(
                contactos = contactosEjemplo,
                onContactoClick = { contacto ->
                    navController.navigate("detalle/${contacto.nombre}/${contacto.telefono}/${contacto.fotoRes}")
                }
            )
        }
        composable(
            route = "detalle/{nombre}/{telefono}/{fotoRes}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType },
                navArgument("fotoRes") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            val fotoRes = backStackEntry.arguments?.getInt("fotoRes") ?: 0
            DetalleContactoScreen(nombre, telefono, fotoRes)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContactosScreen(contactos: List<Contacto>, onContactoClick: (Contacto) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Contactos") })
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(contactos) { contacto ->
                ContactoItem(contacto = contacto, onClick = { onContactoClick(contacto) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun ContactoItem(contacto: Contacto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(nombre = contacto.nombre)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = contacto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = contacto.telefono, color = Color.Gray)
        }
    }
}

@Composable
fun Avatar(nombre: String, modifier: Modifier = Modifier) {
    val inicial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?"
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inicial,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun DetalleContactoScreen(nombre: String, telefono: String, fotoRes: Int) {
    val context = LocalContext.current
    
    // Launcher para solicitar permiso de llamada
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            makeCall(context, telefono)
        } else {
            Toast.makeText(context, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(64.dp))
        AvatarDetalle(nombre = nombre)
        Spacer(modifier = Modifier.size(24.dp))
        Text(text = nombre, style = MaterialTheme.typography.headlineMedium)
        Text(text = telefono, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        
        Spacer(modifier = Modifier.size(32.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Intent implícito para Llamada
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    makeCall(context, telefono)
                } else {
                    callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }) {
                Icon(Icons.Default.Call, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Llamar")
            }

            // Intent implícito para Mensaje (SMS)
            Button(onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$telefono")
                }
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Mensaje")
            }
        }
    }
}

private fun makeCall(context: android.content.Context, telefono: String) {
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$telefono")
    }
    context.startActivity(intent)
}

@Composable
fun AvatarDetalle(nombre: String) {
    val inicial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?"
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inicial,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
