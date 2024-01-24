package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    var id:Int ,
    var name:String,
    var quantity:Int,
    var isEditing: Boolean= false)


@Composable
fun ShoppingListApp() {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
                item->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName,editedQuantity ->
                        sItems = sItems.map {it.copy(isEditing = false)}
                        val editedItem = sItems.find { it.id==item.id }
                        editedItem?.let {
                            it.name=editedName
                            it.quantity=editedQuantity
                        }
                    })
                    
                }else{
                    ShowItem(item = item, onClickEdit = {
                        sItems = sItems.map { it.copy(isEditing = it.id==item.id) }

                    }, onClickDelete = {
                        sItems= sItems - item
                    })
                }
              
            }
            
        }

    }
    if(showDialog) {
       AlertDialog(onDismissRequest = { showDialog=false},
           confirmButton = {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = {
                    if(itemName.isNotBlank()){
                        val newItem = ShoppingItem(
                            id = sItems.size + 1,
                            name = itemName,
                            quantity = itemQuantity.toInt()
                        )
                        sItems = sItems + newItem
                        itemName = ""
                        itemQuantity = ""
                        showDialog= false
                    }
                })
                {
                    Text("Add")
                }
                Button(onClick = { showDialog=false
                itemName = ""
                itemQuantity= ""}) {
                    Text("Cancel")
                }
            }
       },
           title = {Text("Add items") },
           text = {
                Column {
                    OutlinedTextField(
                        value =itemName,
                        onValueChange ={itemName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        singleLine = true,
                        )
                    OutlinedTextField(
                    value =itemQuantity,
                        onValueChange ={itemQuantity = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        singleLine = true)
                }
           })

       }
    }
@Composable
fun ShoppingItemEditor (item: ShoppingItem , onEditComplete:(String,Int)-> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row (modifier = Modifier
        .padding(8.dp)
        .border(width = 2.dp, color = Color.Blue, shape = RoundedCornerShape(20)), horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = editedName, onValueChange= {editedName = it},
                Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                singleLine = true)
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity = it},
                Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                singleLine = true)

        }
        Button(onClick = {}) {
            isEditing = false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?: 1)
            Text("Save")
        }
    }
}

@Composable
fun ShowItem(
    item: ShoppingItem,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
){
    Row (modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text("Qnt: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onClickEdit ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit button")
            }
            IconButton(onClick = onClickDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete button")
            }
        }
    }
}
