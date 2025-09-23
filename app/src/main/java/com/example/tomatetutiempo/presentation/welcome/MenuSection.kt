package com.example.tomatetutiempo.presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tomatetutiempo.presentation.components.MenuButton
import androidx.compose.ui.res.stringResource
import com.example.tomatetutiempo.R


data class MenuItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun MenuSection(
    modifier: Modifier = Modifier,
    onAddTaskClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val menuItems = listOf(
        MenuItem(
            icon = Icons.Default.Add,
            text = stringResource(R.string.menu_add_task),
            onClick = onAddTaskClick
        ),
        MenuItem(
            icon = Icons.Default.DateRange,
            text = stringResource(R.string.menu_calendar),
            onClick = onCalendarClick
        ),
        MenuItem(
            icon = Icons.Default.ShoppingCart,
            text = stringResource(R.string.menu_store),
            onClick = onStoreClick
        ),
        MenuItem(
            icon = Icons.Default.Settings,
            text = stringResource(R.string.menu_settings),
            onClick = onSettingsClick
        ),
        MenuItem(
            icon = Icons.Default.Person,
            text = stringResource(R.string.menu_profile),
            onClick = onProfileClick
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        menuItems.forEach { menuItem ->
            MenuButton(
                icon = menuItem.icon,
                text = menuItem.text,
                onClick = menuItem.onClick
            )
        }
    }
}