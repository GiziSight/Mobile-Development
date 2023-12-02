package com.example.gizisight.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.capitalizeFirstLetterEachWord
import com.example.gizisight.data.News
import com.example.gizisight.databinding.FragmentHomeBinding
import com.example.gizisight.ui.fragment.ListNewsAdapter
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.example.gizisight.data.Result
import com.example.gizisight.data.remote.response.ArticleItem
import com.example.gizisight.ui.akg.AKGActivity
import com.example.gizisight.ui.DetailArticleActivity
import com.example.gizisight.util.RvClickListener
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HomeFragment : Fragment(), RvClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val list = ArrayList<News>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel // Replace with your ViewModel class name
    private lateinit var viewModelFactory: ViewModelFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]

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
        var emailPref = sharedPref.getEmail()

        var email = Firebase.auth.currentUser?.email

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())

        if (!email.isNullOrEmpty()) {
            viewModel.getUser(email.toString(), sharedPref).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            Toast.makeText(
//                                requireActivity(),
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        is Result.Success -> {
                            binding.cvHeadline.alpha = 1.0f;
                            binding.apply {
                                val user = result.data.user
                                val akgData = result.data.akgData

                                val jenis = when (user.gender) {
                                    "Female" -> "Perempuan"
                                    "Male", "MALE" -> "Laki-laki"
                                    else -> {
                                        // Handle other gender values if needed
                                        // For example, if user.gender is neither "Female" nor "Male", set jenis to some default value
                                        "SomeOtherValue" // Change this to a value you prefer for other cases
                                    }
                                }


                                if (acct != null) {
                                    val personName = capitalizeFirstLetterEachWord(acct.displayName)
                                    val personPhoto = acct.photoUrl
                                    binding.tvName.text = personName
                                    tvUmur.text = "${user.age}"
                                    tvJenis.text = jenis

                                    tvKaloriName.text = akgData.bagian1[0].name
                                    tvKaloriValue.text = akgData.bagian1[0].nilai

                                    tvProteinName.text = akgData.bagian1[1].name
                                    tvProteinValue.text = akgData.bagian1[1].nilai

                                    tvSeratName.text = akgData.bagian1[7].name
                                    tvSeratValue.text = akgData.bagian1[7].nilai

                                    Glide.with(requireActivity()).load(acct.photoUrl).circleCrop()
                                        .into(ivPhoto)
                                } else {
                                    tvName.text = capitalizeFirstLetterEachWord(user.username)
                                    tvUmur.text = "${user.age}"
                                    tvKaloriName.text = akgData.bagian1[0].name
                                    tvKaloriValue.text = akgData.bagian1[0].nilai

                                    tvProteinName.text = akgData.bagian1[1].name
                                    tvProteinValue.text = akgData.bagian1[1].nilai

                                    tvSeratName.text = akgData.bagian1[7].name
                                    tvSeratValue.text = akgData.bagian1[7].nilai

                                    tvJenis.text = jenis
                                    when (user.gender) {
                                        "Laki-laki", "Male", "MALE" -> {
                                            Glide.with(requireActivity()).load(R.drawable.man2)
                                                .circleCrop().into(ivPhoto)
                                        }
                                        "Perempuan", "Female" -> {
                                            Glide.with(requireActivity()).load(R.drawable.girl2)
                                                .circleCrop().into(ivPhoto)
                                        }
                                        else -> {
                                            Glide.with(requireActivity())
                                                .load("https://ui-avatars.com/api/?name=${user.username}")
                                                .circleCrop().into(ivPhoto)
                                        }
                                    }
                                }

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
        } else if (!emailPref.isNullOrEmpty()) {
            viewModel.getUser(emailPref, sharedPref).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            Toast.makeText(
//                                requireActivity(),
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        is Result.Success -> {
                            val user = result.data.user
                            val akgData = result.data.akgData
                            binding.cvHeadline.alpha = 1.0f;
                            val jenis = when (user.gender) {
                                "Female" -> "Perempuan"
                                "Male", "MALE" -> "Laki-laki"
                                else -> {
                                    // Handle other gender values if needed
                                    // For example, if user.gender is neither "Female" nor "Male", set jenis to some default value
                                    "SomeOtherValue" // Change this to a value you prefer for other cases
                                }
                            }

                            binding.apply {
                                tvName.text = capitalizeFirstLetterEachWord(user.username)
                                tvUmur.text = "${user.age}"
                                tvKaloriName.text = akgData.bagian1[0].name
                                tvKaloriValue.text = akgData.bagian1[0].nilai

                                tvProteinName.text = akgData.bagian1[1].name
                                tvProteinValue.text = akgData.bagian1[1].nilai

                                tvSeratName.text = akgData.bagian1[7].name
                                tvSeratValue.text = akgData.bagian1[7].nilai


                                tvJenis.text = jenis
                                when (user.gender) {
                                    "Laki-laki", "Male", "MALE" -> {
                                        Glide.with(requireActivity()).load(R.drawable.man2)
                                            .circleCrop().into(ivPhoto)
                                    }
                                    "Perempuan", "Female" -> {
                                        Glide.with(requireActivity()).load(R.drawable.girl2)
                                            .circleCrop().into(ivPhoto)
                                    }
                                    else -> {
                                        Glide.with(requireActivity())
                                            .load("https://ui-avatars.com/api/?name=${user.username}")
                                            .circleCrop().into(ivPhoto)
                                    }
                                }

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

        } else {
            Toast.makeText(requireActivity(), "Silahkan login kembali", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }



        binding.ivMore.setOnClickListener {
            val intent = Intent(requireActivity(), AKGActivity::class.java)
            startActivity(intent)
        }

        binding.rvNews.setHasFixedSize(true)


        val query = "gizi harian stunting"

        viewModel.getArticle(query).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Log.d("Loaading", "Loading Article")
                    }
                    is Result.Success -> {
                        list.addAll(getListHeroes())
                        val data = result.data.article
                        if (data != null) {
                            showRecyclerList(data)
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

    private fun showRecyclerList(list: List<ArticleItem?>) {
        val filteredList = list
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val listNewsAdapter = ListNewsAdapter(filteredList as List<ArticleItem>)
        listNewsAdapter.listener = this
        binding.rvNews.adapter = listNewsAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(view: View, data: ArticleItem) {
        val intent = Intent(requireActivity(), DetailArticleActivity::class.java)
        intent.putExtra("link", data.link)
        startActivity(intent)
    }

}