package com.example.gizisight.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.News
import com.example.gizisight.databinding.FragmentHomeBinding
import com.example.gizisight.ui.fragment.ListNewsAdapter
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.example.gizisight.data.Result
import com.example.gizisight.ui.AKGActivity
import com.example.gizisight.ui.registrasi.RegistrasiActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<News>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel // Replace with your ViewModel class name
    private lateinit var viewModelFactory: ViewModelFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val sharedPref = SharedPrefManager(requireActivity())
        val token = sharedPref.getUser()


        viewModel.getUser("Bearer $token", sharedPref).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Toast.makeText(
                            requireActivity(),
                            "Loading..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        binding.apply {
                            tvName.text = result.data.username
                            tvUmur.text = "${result.data.age} Tahun"
                            tvJenis.text = result.data.gender
                            Glide.with(requireActivity()).load("https://ui-avatars.com/api/?name=${result.data.username}").circleCrop().into(ivPhoto)
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            requireActivity(),
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.ivMore.setOnClickListener{
            val intent = Intent(requireActivity(), AKGActivity::class.java)
            startActivity(intent)
        }


        rvHeroes = binding.rvNews
        rvHeroes.setHasFixedSize(true)

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct!!.displayName
            val personPhoto = acct!!.photoUrl
            binding.tvName.text = personName

            Glide.with(requireActivity()).load(personPhoto).circleCrop().into(binding.ivPhoto)

        }

        list.addAll(getListHeroes())
        showRecyclerList()
    }

    private fun getListHeroes(): ArrayList<News> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)
        val listNews = ArrayList<News>()
        for (i in dataName.indices) {
            val hero = News(dataName[i], dataDescription[i], dataPhoto[i])
            listNews.add(hero)
        }
        return listNews
    }

    private fun showRecyclerList() {
        rvHeroes.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val listNewsAdapter = ListNewsAdapter(list)
        rvHeroes.adapter = listNewsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}