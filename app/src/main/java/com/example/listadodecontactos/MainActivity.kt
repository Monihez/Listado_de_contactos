package com.example.listadodecontactos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listadodecontactos.ui.theme.ListadoDeContactosTheme

// 1. Data Class obligatoria (Actualizada con dirección)
data class Contacto(
    val nombre: String,
    val telefono: String,
    val fotoRes: Int,
    val direccion: String
)

// 2. Lista exacta de 16 contactos (Actualizada con direcciones)
val listaDe16Contactos = listOf(
    Contacto("Adalverto", "555-1001", R.drawable.c_12, "Av. Reforma 100, León, Guanajuato"),
    Contacto("Moni", "555-1002", R.drawable.c_18, "Calle Madero 500, Ciudad de México, CDMX"),
    Contacto("Fidel", "555-1003", R.drawable.c_11, "Paseo de la Sierra 20, Monterrey, Nuevo León"),
    Contacto("Luna", "555-1004", R.drawable.c_10, "Av. Insurgentes Sur 15, Ciudad de México, CDMX"),
    Contacto("Alvaro", "555-1005", R.drawable.c_5, "Blvd. Campestre 302, León, Guanajuato"),
    Contacto("Uriel", "555-1006", R.drawable.c_8, "Calle Juárez 45, Guadalajara, Jalisco"),
    Contacto("Aytana", "555-1007", R.drawable.c_7, "Av. Universidad 10, Querétaro, Querétaro"),
    Contacto("Charly", "555-1008", R.drawable.c_5, "Calle 5 de Mayo 200, Puebla, Puebla"),
    Contacto("Criss", "555-1009", R.drawable.c_4, "Av. Hidalgo 88, Toluca, Estado de México"),
    Contacto("Jaz", "555-1010", R.drawable.c_3, "Calle Libertad 5, San Luis Potosí, SLP"),
    Contacto("Josselyn", "555-1011", R.drawable.c_2, "Av. Tecnológico 1000, Aguascalientes, AGS"),
    Contacto("Marta", "555-1012", R.drawable.c_13, "Calle Allende 33, Saltillo, Coahuila"),
    Contacto("Sol", "555-1013", R.drawable.c_15, "Av. Carranza 450, San Luis Potosí, SLP"),
    Contacto("Zapata", "555-1014", R.drawable.c_14, "Blvd. Díaz Ordaz 200, Irapuato, Guanajuato"),
    Contacto("Less", "555-1015", R.drawable.c1, "Av. de la Paz 77, Guadalajara, Jalisco"),
    Contacto("Luis", "555-0116", R.drawable.c_9, "Calle Morelos 12, Cuernavaca, Morelos")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListadoDeContactosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppMainHost()
                }
            }
        }
    }
}

@Composable
fun AppMainHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaContactos(
                onVerDetalle = { contacto ->
                    val encodedDireccion = android.net.Uri.encode(contacto.direccion)
                    navController.navigate("detalle/${contacto.nombre}/${contacto.telefono}/${contacto.fotoRes}/$encodedDireccion")
                }
            )
        }
        composable(
            route = "detalle/{nombre}/{telefono}/{fotoRes}/{direccion}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType },
                navArgument("fotoRes") { type = NavType.IntType },
                navArgument("direccion") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            val fotoRes = backStackEntry.arguments?.getInt("fotoRes") ?: 0
            val direccion = android.net.Uri.decode(backStackEntry.arguments?.getString("direccion") ?: "")
            
            PantallaDetalle(
                nombre = nombre,
                telefono = telefono,
                fotoRes = fotoRes,
                direccion = direccion,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContactos(onVerDetalle: (Contacto) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredContacts = listaDe16Contactos.filter { 
        it.nombre.contains(searchQuery, ignoreCase = true) 
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Contacts",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search contacts") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F3F4),
                        unfocusedContainerColor = Color(0xFFF1F3F4),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text("Calendar") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Contacts") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Settings") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            items(filteredContacts) { contacto ->
                TarjetaDeContacto(contacto = contacto, onClick = { onVerDetalle(contacto) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TarjetaDeContacto(contacto: Contacto, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(nombre = contacto.nombre, fotoRes = contacto.fotoRes, size = 48)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(contacto.nombre, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(contacto.telefono, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, "tel:${contacto.telefono}".toUri())
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Call, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO, "smsto:${contacto.telefono}".toUri())
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Outlined.MailOutline, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun AvatarCircle(nombre: String, fotoRes: Int, size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        if (fotoRes != 0) {
            Image(
                painter = painterResource(id = fotoRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            val inicial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?"
            Text(
                text = inicial,
                style = if (size > 60) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleMedium,
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(nombre: String, telefono: String, fotoRes: Int, direccion: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var verDireccion by remember { mutableStateOf(false) }

    val callLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            intentLlamadaDirecta(context, telefono)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Contact Info") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            AvatarCircle(nombre = nombre, fotoRes = fotoRes, size = 120)
            Spacer(modifier = Modifier.height(24.dp))
            Text(nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(telefono, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sección de dirección revelada (solo aparece si se presiona el icono de Maps)
            AnimatedVisibility(visible = verDireccion) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = direccion,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FilledIconButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                intentLlamadaDirecta(context, telefono)
                            } else {
                                callLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                    }
                    Text("Call", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FilledIconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, "smsto:$telefono".toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF3B82F6))
                    ) {
                        Icon(Icons.Outlined.MailOutline, null, tint = Color.White)
                    }
                    Text("Message", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                }
                
                // BOTÓN GOOGLE MAPS
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FilledIconButton(
                        onClick = {
                            verDireccion = true
                            val uri = android.net.Uri.parse("geo:0,0?q=${android.net.Uri.encode(direccion)}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.setPackage("com.google.android.apps.maps")
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "No se encontró una aplicación compatible con mapas", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFFEA4335))
                    ) {
                        Icon(Icons.Default.Map, contentDescription = "Maps", tint = Color.White)
                    }
                    Text("Maps", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Nombre: $nombre\nTeléfono: $telefono\nDirección: $direccion")
                        putExtra(Intent.EXTRA_SUBJECT, "Información de contacto")
                    }
                    val chooser = Intent.createChooser(intent, "Compartir contacto vía")
                    context.startActivity(chooser)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Compartir contacto")
            }
        }
    }
}

private fun intentLlamadaDirecta(context: Context, tel: String) {
    try {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$tel".toUri()
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Error al realizar la llamada", Toast.LENGTH_SHORT).show()
    }
}
