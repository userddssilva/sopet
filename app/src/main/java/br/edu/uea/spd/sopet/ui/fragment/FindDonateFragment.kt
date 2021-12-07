package br.edu.uea.spd.sopet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R

class FindDonateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_find_donate_pet, container, false)
//        val animalTypes = view.findViewById<AutoCompleteTextView>(R.id.tf_animal_type)
//        val operationType = view.findViewById<AutoCompleteTextView>(R.id.tf_operation_type)
//        val btnNext = view.findViewById<MaterialButton>(R.id.btn_next)
//        val itemsAnimalTypes = listOf("Cachorro", "Gato", "Coelho", "Tartaruga")
//        val itemsOperationTypes = listOf("Adotar", "Perdido", "Doente")
////        val adapterAnimalTypes = ArrayAdapter(requireContext(), R.layout.list_item, itemsAnimalTypes)
////        val adapterOperationTypes = ArrayAdapter(requireContext(), R.layout.list_item, itemsOperationTypes)
//
////        animalTypes.setAdapter(adapterAnimalTypes)
////        operationType.setAdapter(adapterOperationTypes)
//
//        btnNext.setOnClickListener {
//            startActivity(Intent(context, FeedActivity::class.java))
//        }

        return view
    }
}