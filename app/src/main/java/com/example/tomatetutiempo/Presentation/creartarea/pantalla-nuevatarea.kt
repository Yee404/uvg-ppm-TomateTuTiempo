package com.example.tomatetutiempo.Presentation.creartarea

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.Presentation.Componentes.MenuButton
import com.example.tomatetutiempo.Presentation.Componentes.PerfilButton
import com.example.tomatetutiempo.Presentation.Componentes.FAB
import com.example.tomatetutiempo.Presentation.Componentes.CampoDeTexto
import com.example.tomatetutiempo.Presentation.Componentes.SelectorFecha
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.Verdeclaro
import com.example.tomatetutiempo.ui.theme.Verdetexto


@Composable
fun PantallaNuevaTarea() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Verdeclaro)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                MenuButton()
                Text(
                    text = stringResource(R.string.titulo),
                    color = Verdetexto,
                    fontSize = 18.sp
                )
                PerfilButton()
            }
            CampoDeTexto(label = stringResource(R.string.ingr_nombre_de_la_tarea))
            CampoDeTexto(label = stringResource(R.string.ingre_tema_de_tarea))
            CampoDeTexto(label = stringResource(R.string.ing_descripcion_de_tarea))
            SelectorFecha()

        }

        FAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(vertical = 32.dp)

        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPantallaNuevaTarea() {
    PantallaNuevaTarea()
}
