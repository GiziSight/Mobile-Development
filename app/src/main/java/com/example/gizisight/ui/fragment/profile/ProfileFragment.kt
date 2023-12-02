package com.example.gizisight.ui.fragment.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.capitalizeFirstLetterEachWord
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.FragmentProfileBinding
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel // Replace with your ViewModel class name
    private lateinit var viewModelFactory: ViewModelFactory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        val sharedPref = SharedPrefManager(requireActivity())
        val emailPref = sharedPref.getEmail()
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        var email = Firebase.auth.currentUser?.email

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
                            binding.cvProfile.alpha = 1.0f;
                            val user = result.data.user
                            binding.apply {
                                if (acct != null) {
                                    val personName = capitalizeFirstLetterEachWord(acct!!.displayName)
                                    val personPhoto = acct!!.photoUrl
                                    binding.tvName.text = capitalizeFirstLetterEachWord(personName)
                                    binding.tvEmail.text = acct.email
                                    tvUmur.text = "${user.age}"
                                    tvHeight.text = user.height.toString() + " cm"
                                    tvWeight.text = user.weight.toString() + " Kg"
                                    Glide.with(requireActivity()).load(personPhoto).circleCrop()
                                        .into(binding.imgProfile)
                                } else {
                                    tvName.text = capitalizeFirstLetterEachWord(user.username)
                                    tvName.text = user.email
                                    tvUmur.text = "${user.age}"
                                    tvHeight.text = user.height.toString() + " cm"
                                    tvWeight.text = user.weight.toString() + " Kg"
                                    when (user.gender) {
                                        "Laki-laki", "MALE", "Male" -> {
                                            Glide.with(requireActivity()).load(R.drawable.man2)
                                                .circleCrop().into(imgProfile)
                                        }
                                        "Perempuan", "FEMALE" -> {
                                            Glide.with(requireActivity()).load(R.drawable.girl2)
                                                .circleCrop().into(imgProfile)
                                        }
                                        else -> {
                                            Glide.with(requireActivity())
                                                .load("https://ui-avatars.com/api/?name=${user.username}")
                                                .circleCrop().into(imgProfile)
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
            viewModel.getUser(emailPref!!, sharedPref).observe(viewLifecycleOwner) { result ->
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
                            binding.cvProfile.alpha = 1.0f
                            binding.apply {
                                tvName.text = capitalizeFirstLetterEachWord(user.username)
                                tvUmur.text = user.age.toString()
                                tvHeight.text = user.height.toString() + " CM"
                                tvWeight.text = user.weight.toString() + " Kg"
                                tvEmail.text = user.email
                                when (user.gender) {
                                    "Laki-laki", "Male", "MALE" -> {
                                        Glide.with(requireActivity()).load(R.drawable.man2)
                                            .circleCrop().into(imgProfile)
                                    }
                                    "Perempuan", "Female" -> {
                                        Glide.with(requireActivity()).load(R.drawable.girl2)
                                            .circleCrop().into(imgProfile)
                                    }
                                    else -> {
                                        Glide.with(requireActivity())
                                            .load("https://ui-avatars.com/api/?name=${user.username}")
                                            .circleCrop().into(imgProfile)
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




        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val sharedPref = SharedPrefManager(requireActivity())
            sharedPref.removeToken()
            sharedPref.removeEmail()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().finish()
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}