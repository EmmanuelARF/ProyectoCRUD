package com.example.proyectocrud

import android.os.Parcel
import android.os.Parcelable

class Luchador() :Parcelable {
    private var id = 0
    private var nombre: String? = null

    constructor(parcel: Parcel) : this() {
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getNombre(): String {
        return nombre!!
    }

    fun setNombre(nombre: String?) {
        this.nombre = nombre
    }

    override fun toString(): String {
        return nombre!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(this.id);
        parcel.writeString(this.nombre);
    }
    fun readFromParcel(source: Parcel) {
        this.id = source.readInt()
        this.nombre = source.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    protected fun Luchador(`in`: Parcel) {
        this.id = `in`.readInt()
        this.nombre = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Luchador> {
        override fun createFromParcel(parcel: Parcel): Luchador {
            return Luchador(parcel)
        }

        override fun newArray(size: Int): Array<Luchador?> {
            return arrayOfNulls(size)
        }
    }

}