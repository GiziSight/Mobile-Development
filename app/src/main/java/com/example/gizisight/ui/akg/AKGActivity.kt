package com.example.gizisight.ui.akg

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.capitalizeFirstLetterEachWord
import com.example.gizisight.data.Result
import com.example.gizisight.data.remote.response.Bagian1Item
import com.example.gizisight.data.remote.response.Bagian2Item
import com.example.gizisight.data.remote.response.Bagian3Item
import com.example.gizisight.data.remote.response.KandunganItem
import com.example.gizisight.databinding.ActivityAkgactivityBinding
import com.example.gizisight.ui.fragment.camera.ListKandunganAdapter
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AKGActivity : AppCompatActivity() {
    private var _binding: ActivityAkgactivityBinding? = null
    private val binding get() = _binding
    private var isExpanded = false
    private var isExpanded2 = false
    private var isExpanded3 = false
    private var targetHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAkgactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@AKGActivity)
        val viewModel: AKGViewModel by viewModels {
            factory
        }

        val sharedPref = SharedPrefManager(this)
        val emailPref = sharedPref.getEmail()

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        var email = Firebase.auth.currentUser?.email

        binding?.btnBack?.setOnClickListener{
            finish()
        }
        binding?.rvAkg1?.setHasFixedSize(true)
        binding?.rvAkg2?.setHasFixedSize(true)
        binding?.rvAkg3?.setHasFixedSize(true)

        if( !emailPref.isNullOrEmpty()) {
            viewModel.getUser(emailPref, sharedPref).observe(this){ result ->
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
                            val bagian = result.data.akgData
                            val user = result.data.user
                            val range = result.data.personalData.userInfo[1].nilai
                            val bb = result.data.personalData.userInfo[2].nilai
                            val tinggi = result.data.personalData.userInfo[3].nilai
                            binding?.apply {
                                tvInfo.text = "Angka Kecukupan Gizi (AKG) Usia $range dengan berat badan $bb kg dan tinggi badan $tinggi"
                                tvName.text = capitalizeFirstLetterEachWord(result.data.user.username)
                                tvUmur.text = "${user.age}"
                                tvTinggi.text = "${user.height} cm"
                                tvBerat.text = "${user.weight} kg"
                            }
                            showRecyclerList(bagian.bagian1, bagian.bagian2, bagian.bagian3)
                            binding?.apply {
                                imageView.setOnClickListener {
                                    toggleCardViewHeightWithAnimation1()
                                    toggleIconImage()
                                }
                                imageView2.setOnClickListener {
                                    toggleCardViewHeightWithAnimation2()
                                    toggleIconImage2()
                                }
                                imageView3.setOnClickListener {
                                    toggleCardViewHeightWithAnimation3()
                                    toggleIconImage3()
                                }
                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        } else if (!email.isNullOrEmpty()) {
            viewModel.getUser(email.toString(), sharedPref).observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(
                                this,
                                "Loading..",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Result.Success -> {
                            val bagian = result.data.akgData
                            binding?.apply {
                                val user = result.data.user
                                if (acct != null) {
                                    val personName = capitalizeFirstLetterEachWord(acct!!.displayName)
                                    binding?.tvName?.text = personName
                                    tvUmur.text = "${user.age}"
                                    tvTinggi.text = "${user.height} cm"
                                    tvBerat.text = "${user.weight} kg"
                                } else {
                                    tvName.text = capitalizeFirstLetterEachWord(user.username)
                                    tvUmur.text = "${user.age}"
                                    tvTinggi.text = "${user.height} cm"
                                    tvBerat.text = "${user.weight} kg"
                                }
                                showRecyclerList(bagian.bagian1, bagian.bagian2, bagian.bagian3)
                                binding?.apply {
                                    imageView.setOnClickListener {
                                        toggleCardViewHeightWithAnimation1()
                                        toggleIconImage()
                                    }
                                    imageView2.setOnClickListener {
                                        toggleCardViewHeightWithAnimation2()
                                        toggleIconImage2()
                                    }
                                    imageView3.setOnClickListener {
                                        toggleCardViewHeightWithAnimation3()
                                        toggleIconImage3()
                                    }
                                }

                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }



    }

    private fun showRecyclerList(listBagian1: List<Bagian1Item?>, listBagian2: List<Bagian2Item?>, listBagian3: List<Bagian3Item?>) {
        binding?.rvAkg1?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvAkg2?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvAkg3?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val listBagian1Adapter = Informasi1Adapter(listBagian1 as List<Bagian1Item>)
        val listBagian2Adapter = Informasi2Adapter(listBagian2 as List<Bagian2Item>)
        val listBagian3Adapter = Informasi3Adapter(listBagian3 as List<Bagian3Item>)

        binding?.rvAkg1?.adapter = listBagian1Adapter
        binding?.rvAkg2?.adapter = listBagian2Adapter
        binding?.rvAkg3?.adapter = listBagian3Adapter
    }

    private fun toggleCardViewHeightWithAnimation1() {
        val startHeight = binding?.cvInformasi?.height
        val endHeight = if (isExpanded) dpToPx(60) else ViewGroup.LayoutParams.WRAP_CONTENT

        val valueAnimator = ValueAnimator.ofInt(40, endHeight)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            val layoutParams = binding?.cvInformasi?.layoutParams
            layoutParams?.height = animatedValue
            binding?.cvInformasi?.layoutParams = layoutParams
        }

        valueAnimator.duration = 300 // Set your desired animation duration here
        valueAnimator.start()

        isExpanded = !isExpanded // Toggle the state
    }

    private fun toggleCardViewHeightWithAnimation2() {
        val startHeight = binding?.cvInformasi2?.height
        val endHeight = if (isExpanded2) dpToPx(50) else ViewGroup.LayoutParams.WRAP_CONTENT

        val valueAnimator = ValueAnimator.ofInt(40, endHeight)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            val layoutParams = binding?.cvInformasi2?.layoutParams
            layoutParams?.height = animatedValue
            binding?.cvInformasi2?.layoutParams = layoutParams
        }

        valueAnimator.duration = 300 // Set your desired animation duration here
        valueAnimator.start()

        isExpanded2 = !isExpanded2 // Toggle the state
    }

    private fun toggleCardViewHeightWithAnimation3() {
        val startHeight = binding?.cvInformasi3?.height
        val endHeight = if (isExpanded3) dpToPx(50) else ViewGroup.LayoutParams.WRAP_CONTENT

        val valueAnimator = ValueAnimator.ofInt(40, endHeight)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            val layoutParams = binding?.cvInformasi3?.layoutParams
            layoutParams?.height = animatedValue
            binding?.cvInformasi3?.layoutParams = layoutParams
        }

        valueAnimator.duration = 300 // Set your desired animation duration here
        valueAnimator.start()

        isExpanded3 = !isExpanded3 // Toggle the state
    }

    private fun toggleIconImage() {
        if (isExpanded) {
            binding?.imageView?.setImageResource(R.drawable.baseline_arrow_drop_down_24) // Set to chevron right icon
        } else {
            binding?.imageView?.setImageResource(R.drawable.baseline_arrow_right_24) // Set to chevron bottom icon
        }
    }
    private fun toggleIconImage2() {
        if (isExpanded2) {
            binding?.imageView2?.setImageResource(R.drawable.baseline_arrow_drop_down_24) // Set to chevron right icon
        } else {
            binding?.imageView2?.setImageResource(R.drawable.baseline_arrow_right_24) // Set to chevron bottom icon
        }
    }

    private fun toggleIconImage3() {
        if (isExpanded3) {
            binding?.imageView3?.setImageResource(R.drawable.baseline_arrow_drop_down_24) // Set to chevron right icon
        } else {
            binding?.imageView3?.setImageResource(R.drawable.baseline_arrow_right_24) // Set to chevron bottom icon
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}