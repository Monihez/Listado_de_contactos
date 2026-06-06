package com.example.listadodecontactos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listadodecontactos.ui.theme.ListadoDeContactosTheme

// 1. Data Class obligatoria
data class Contacto(
    val nombre: String,
    val telefono: String,
    val fotoRes: Int
)

// 2. Lista exacta de 16 contactos
val listaDe16Contactos = listOf(
    Contacto("Adalverto", "555-1001", 0),
    Contacto("Moni", "555-1002", 0),
    Contacto("Fidel", "555-1003", R.drawable.c_11),
    Contacto("Luna", "555-1004", R.drawable.c_10),
    Contacto("Alvaro", "555-1005", 0),
    Contacto("Uriel", "555-1006", R.drawable.c_8),
    Contacto("Aytana", "555-1007", R.drawable.c_7),
    Contacto("Charly", "555-1008", R.drawable.c_5),
    Contacto("Criss", "555-1009", R.drawable.c_4),
    Contacto("Jaz", "555-1010", R.drawable.c_3),
    Contacto("Josselyn", "555-1011", R.drawable.c_2),
    Contacto("Marta", "555-1012", 0),
    Contacto("Sol", "555-1013", 0),
    Contacto("Zapata", "555-1014", 0),
    Contacto("Less", "555-1015", 0),
    Contacto("Luis", "555-0116", 0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListadoDeContactosTheme {
                AppMainContainer()
            }
        }
    }
}

@Composable
fun AppMainContainer() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaContactos(
                onNavigateToDetail = { contacto ->
                    navController.navigate("detalle/${contacto.nombre}/${contacto.telefono}")
                }
            )
        }
        composable(
            route = "detalle/{nombre}/{telefono}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType }
            )
        ) { backStackEntry: NavBackStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            PantallaDetalleContacto(
                nombre = nombre,
                telefono = telefono,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContactos(onNavigateToDetail: (Contacto) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtrados = listaDe16Contactos.filter { it.nombre.contains(query, ignoreCase = true) }

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
                // Barra de búsqueda minimalista
                OutlinedTextField(
                    value = query,
                    onValueChange = { newValue: String -> query = newValue },
                    placeholder = { Text("Search contacts") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F3F4),
                        unfocusedContainerColor = Color(0xFFF1F3F4),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
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
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Calendar") },
                    label = { Text("Calendar") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Contacts") },
                    label = { Text("Contacts") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings") },
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
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues: PaddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            items(items = filtrados) { contacto: Contacto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToDetail(contacto) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (contacto.nombre.isNotEmpty()) contacto.nombre.take(1).uppercase() else "?",
                            color = Color(0xFF1976D2),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(contacto.nombre, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        Text(contacto.telefono, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }
                    val context = LocalContext.current
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:${contacto.telefono}".toUri())
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Call, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, "smsto:${contacto.telefono}".toUri())
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Outlined.MailOutline, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleContacto(nombre: String, telefono: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val callLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            try {
                val intent = Intent(Intent.ACTION_CALL, "tel:$telefono".toUri())
                context.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(context, "Error al llamar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Contact Info") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(telefono, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FilledIconButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    val intent = Intent(Intent.ACTION_CALL, "tel:$telefono".toUri())
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                }
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
                        Icon(Icons.Outlined.MailOutline, contentDescription = "Message", tint = Color.White)
                    }
                    Text("Message", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
