package com.example.zd3_Chernomorcev

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import android.arch.lifecycle.Observer
import com.example.zd3_pavina.R
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
class CrimeFragment : Fragment(){
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        val dateNow = Date()
        crime.mDate = dateNow

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        dateButton.apply {
            text = crime.getDate().toString()
            isEnabled = false
        }
        solvedCheckBox = view.findViewById(R.id.crime_solved)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,
            savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner as LifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object: TextWatcher {
            override fun beforeTextChanged(
                senquence: CharSequence?,
                start: Int,
                count: Int,
                after: Int) {

            }

            override fun onTextChanged(
                senquence: CharSequence?,
                start: Int,
                count: Int,
                after: Int) {
                crime.setTitle(senquence.toString())
            }

            override fun afterTextChanged(senquence: Editable?) {

            }
        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener{_, isChecked ->
                crime.setSolved(isChecked) }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    private fun updateUI() {
        titleField.setText(crime.mTitle)
        dateButton.text = crime.mDate.toString()
        solvedCheckBox.apply {
            isChecked = crime.mSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(
                    ARG_CRIME_ID,
                    crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}