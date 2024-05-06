package com.example.proyectocrud

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private var txtid: EditText? = null
    private var txtnom:EditText? = null
    private var btnbus: Button? = null
    private var btnmod:Button? = null
    private var btnreg:Button? = null
    private var btneli:Button? = null
    private var lvDatos: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        txtid   = findViewById(R.id.txtid)
        txtnom  =  findViewById(R.id.txtnom)
        btnbus  = findViewById(R.id.btnbus)
        btnmod  = findViewById(R.id.btnmod)
        btnreg  = findViewById(R.id.btnreg)
        btneli  = findViewById(R.id.btneli)
        lvDatos = findViewById(R.id.lvDatos)

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
    }

    private fun botonBuscar() {
        btnbus!!.setOnClickListener {
            if (txtid!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                ocultarTeclado()
                Toast.makeText(
                    this,
                    "Digite El ID del Luchador a Buscar!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val id = txtid!!.text.toString().toInt()

                val db = FirebaseDatabase.getInstance()
                val dbref = db.getReference(Luchador::class.java.simpleName)

                //DatabaseReference dbref = db.getReference().child("Luchador");
                dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val aux = id.toString()
                        var res = false
                        for (x in snapshot.children) {
                            if (aux.equals(x.child("id").value.toString(), ignoreCase = true)) {
                                res = true
                                ocultarTeclado()
                                txtnom!!.setText(x.child("nombre").value.toString())
                                break
                            }
                        }

                        if (res == false) {
                            ocultarTeclado()
                            Toast.makeText(this@MainActivity,
                                "ID ($aux) No Encontrado!!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    private fun botonModificar() {
        btnmod!!.setOnClickListener {
            if (txtid!!.text.toString().trim { it <= ' ' }.isEmpty()
                || txtnom!!.text.toString().trim { it <= ' ' }.isEmpty()
            ) {
                ocultarTeclado()
                Toast.makeText(
                    this,
                    "Complete Los Campos Faltantes Para Actualizar!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val id = txtid!!.text.toString().toInt()
                val nom = txtnom!!.text.toString()

                val db = FirebaseDatabase.getInstance()
                val dbref = db.getReference(Luchador::class.java.simpleName)

                dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var res2 = false
                        for (x in snapshot.children) {
                            if (x.child("nombre").value.toString().equals(nom, ignoreCase = true)) {
                                res2 = true
                                ocultarTeclado()
                                Toast.makeText(
                                    this@MainActivity,
                                    "El Nombre ($nom) Ya Existe.\nImposible Modificar!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                break
                            }
                        }

                        if (res2 == false) {
                            val aux = id.toString()
                            var res = false
                            for (x in snapshot.children) {
                                if (x.child("id").value.toString().equals(aux, ignoreCase = true)) {
                                    res = true
                                    ocultarTeclado()
                                    x.ref.child("nombre").setValue(nom)
                                    txtid!!.setText("")
                                    txtnom!!.setText("")
                                    listarLuchadores()
                                    break
                                }
                            }

                            if (res == false) {
                                ocultarTeclado()
                                Toast.makeText(
                                    this@MainActivity,
                                    "ID ($aux) No Encontrado.\nImposible Modificar!!!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                txtid!!.setText("")
                                txtnom!!.setText("")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    private fun botonRegistrar() {
        btnreg!!.setOnClickListener {
            if (txtid!!.text.toString().trim { it <= ' ' }.isEmpty()
                || txtnom!!.text.toString().trim { it <= ' ' }.isEmpty()
            ) {
                ocultarTeclado()
                Toast.makeText(
                    this@MainActivity,
                    "Complete Los Campos Faltantes!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val id = txtid!!.text.toString().toInt()
                val nom = txtnom!!.text.toString()

                val db = FirebaseDatabase.getInstance()
                val dbref = db.getReference(Luchador::class.java.simpleName)

                dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val aux = id.toString()
                        var res = false
                        for (x in snapshot.children) {
                            if (x.child("id").value.toString().equals(aux, ignoreCase = true)) {
                                res = true
                                ocultarTeclado()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error. El ID ($aux) Ya Existe!!", Toast.LENGTH_SHORT
                                ).show()
                                break
                            }
                        }

                        var res2 = false
                        for (x in snapshot.children) {
                            if (x.child("nombre").value.toString().equals(nom, ignoreCase = true)) {
                                res2 = true
                                ocultarTeclado()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error. El Nombre ($nom) Ya Existe!!", Toast.LENGTH_SHORT
                                ).show()
                                break
                            }
                        }

                        if (res == false && res2 == false) {
                            val luc: Luchador = Luchador()
                            dbref.push().setValue(luc)
                            ocultarTeclado()
                            Toast.makeText(
                                this@MainActivity,
                                "Luchador Registrado Correctamente!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            txtid!!.setText("")
                            txtnom!!.setText("")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            } // Cierra el if/else inicial.
        }
    }

    private fun botonEliminar() {
        btneli!!.setOnClickListener {
            if (txtid!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                ocultarTeclado()
                Toast.makeText(
                    this,
                    "Digite El ID del Luchador a Eliminar!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val id = txtid!!.text.toString().toInt()

                val db = FirebaseDatabase.getInstance()
                val dbref = db.getReference(Luchador::class.java.simpleName)

                //DatabaseReference dbref = db.getReference().child("Luchador");
                dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val aux = id.toString()
                        val res = booleanArrayOf(false)
                        for (x in snapshot.children) {
                            if (aux.equals(x.child("id").value.toString(), ignoreCase = true)) {
                                val a =
                                    AlertDialog.Builder(this@MainActivity)
                                a.setCancelable(false)
                                a.setTitle("Pregunta")
                                a.setMessage("¿Está Seguro(a) De Querer Eliminar El Registro?")

                                a.setNegativeButton(
                                    "Cancelar"
                                ) { dialogInterface, i -> }

                                a.setPositiveButton(
                                    "Aceptar"
                                ) { dialogInterface, i ->
                                    res[0] = true
                                    ocultarTeclado()
                                    x.ref.removeValue()
                                    listarLuchadores()
                                }

                                a.show()

                                break
                            }
                        }

                        if (res[0] == false) {
                            ocultarTeclado()
                            Toast.makeText(
                                this@MainActivity,
                                "ID ($aux) No Encontrado.\nImposible Eliminar!!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            } // Cierra el if/else inicial.
        }
    }

    private fun ocultarTeclado() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun listarLuchadores() {
        val db = FirebaseDatabase.getInstance()
        val dbref = db.getReference(Luchador::class.java.simpleName)

        val lisluc = ArrayList<Luchador?>()
        val ada =
            ArrayAdapter<Luchador?>(this, android.R.layout.simple_list_item_1, lisluc)
        lvDatos!!.adapter = ada

        dbref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val luc = snapshot.getValue(Luchador::class.java)
                lisluc.add(luc)
                ada.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                ada.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        lvDatos!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                val luc = lisluc[i]!!
                val a = AlertDialog.Builder(this)
                a.setCancelable(true)
                a.setTitle("Luchador Seleccionado")

                var msg = ("ID : " + luc.getId()).toString() + "\n\n"
                msg += "NOMBRE : " + luc.getNombre()

                a.setMessage(msg)
                a.show()
            }
    }
}