package solis.juan.digimind.ui.dashboard

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import solis.juan.digimind.R
import solis.juan.digimind.Recordatorio
import solis.juan.digimind.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //instanciar firebase
        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        val btnSave: Button = root.findViewById(R.id.btnRegister)
        val txtMensaje: TextView = root.findViewById(R.id.mensaje)
        val btnHora: Button = root.findViewById(R.id.btnTime)

        btnHora.setOnClickListener { v ->
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minutes ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minutes)

                btnHora.text = SimpleDateFormat("HH:mm").format(cal.time)
            }

            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }

        val chbMonday: CheckBox = root.findViewById(R.id.chbMonday)
        val chbTuesday: CheckBox = root.findViewById(R.id.chbTuesday)
        val chbWednesday: CheckBox = root.findViewById(R.id.chbWednesday)
        val chbThursday: CheckBox = root.findViewById(R.id.chbThursday)
        val chbFriday: CheckBox = root.findViewById(R.id.chbFriday)
        val chbSaturday: CheckBox = root.findViewById(R.id.chbSaturday)
        val chbSunday: CheckBox = root.findViewById(R.id.chbSunday)

        btnSave.setOnClickListener {
            if (!txtMensaje.text.isNullOrEmpty() && !btnHora.text.isNullOrEmpty() && (chbMonday.isChecked || chbTuesday.isChecked || chbWednesday.isChecked || chbThursday.isChecked || chbFriday.isChecked || chbSaturday.isChecked || chbSunday.isChecked)) {

                var titulo = txtMensaje.text.toString()
                var time = btnHora.text.toString()

                var dias = ArrayList<String>()

                val actividad = hashMapOf(
                    "actividad" to titulo,
                    "email" to usuario.currentUser.email.toString(),
                    "do" to chbSunday.isChecked,
                    "lu" to chbMonday.isChecked,
                    "ma" to chbTuesday.isChecked,
                    "mi" to chbWednesday.isChecked,
                    "ju" to chbThursday.isChecked,
                    "vi" to chbFriday.isChecked,
                    "sa" to chbSaturday.isChecked,
                    "tiempo" to time
                )

                storage.collection("actividades")
                    .add(actividad)
                    .addOnSuccessListener {
                        Toast.makeText(root.context, "Task agregada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(root.context, "Error: intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

                if (chbMonday.isChecked && chbTuesday.isChecked && chbWednesday.isChecked && chbThursday.isChecked && chbFriday.isChecked && chbSaturday.isChecked && chbSunday.isChecked) {
                    dias.add("Everyday")
                } else {
                    if (chbMonday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Monday" else ", Monday")
                    }

                    if (chbTuesday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Tuesday" else ", Tuesday")
                    }

                    if (chbWednesday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Wednesday" else ", Wednesday")
                    }

                    if (chbThursday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Thursday" else ", Thursday")
                    }

                    if (chbFriday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Friday" else ", Friday")
                    }

                    if (chbSaturday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Saturday" else ", Saturday")
                    }

                    if (chbSunday.isChecked) {
                        dias.add(if (dias.isNullOrEmpty()) "Sunday" else ", Sunday")
                    }
                }

                val recordatorio: Recordatorio = Recordatorio(dias, btnHora.text.toString(), txtMensaje.text.toString())

                HomeFragment.tasks.add(recordatorio)

                txtMensaje.setText("")
                btnHora.setText("")

                chbMonday.setChecked(false)
                chbTuesday.setChecked(false)
                chbWednesday.setChecked(false)
                chbThursday.setChecked(false)
                chbFriday.setChecked(false)
                chbSaturday.setChecked(false)
                chbSunday.setChecked(false)

                Toast.makeText(context, "Recordatorio registrado", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(context, "Debes escribir lo que quieres recordar, seleccionar al menos un dia y una hora", Toast.LENGTH_LONG).show()
            }

        }

        return root
    }
}