package mx.unam.fi.practica2.model

import kotlinx.serialization.Serializable
@Serializable
data class Practica2Model(

val modelName: String,
val bodyStyle: String,
val transmissionType: String

)
