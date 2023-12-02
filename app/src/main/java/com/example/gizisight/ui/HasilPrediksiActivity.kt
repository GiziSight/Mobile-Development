package com.example.gizisight.ui

import android.animation.ValueAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.capitalizeFirstLetterEachWord
import com.example.gizisight.data.remote.response.KandunganItem
import com.example.gizisight.data.remote.response.ManfaatItem
import com.example.gizisight.data.remote.response.UploadResponse
import com.example.gizisight.databinding.ActivityHasilPrediksiBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.ui.fragment.camera.ListKandunganAdapter
import com.example.gizisight.ui.fragment.camera.ListManfaatAdapter

class HasilPrediksiActivity : AppCompatActivity() {
    private var _binding: ActivityHasilPrediksiBinding? = null
    private val binding get() = _binding
    private var isExpanded = false
    private var isExpanded2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHasilPrediksiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rvInformasi?.setHasFixedSize(true)
        binding?.rvManfaat?.setHasFixedSize(true)

        binding?.btnBack?.setOnClickListener{
            finish()
        }

        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<UploadResponse>("NUTRISI", UploadResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UploadResponse>("NUTRISI")
        }

        if(data != null) {
            binding?.tvNama?.text = capitalizeFirstLetterEachWord(data.prediction)
            binding?.ivResult?.let { Glide.with(this).load(data.imageUrl).into(it) }
            binding?.tvAkurasi?.text = "Tingkat Akurasi: ${data.accuracy}%"
            showRecyclerList(data.gizi.kandungan, data.manfaat.manfaat)
            binding?.apply {
                imageView.setOnClickListener {
                    toggleCardViewHeightWithAnimation1()
                    toggleIconImage()
                }
                imageView2.setOnClickListener {
                    toggleCardViewHeightWithAnimation2()
                    toggleIconImage2()
                }
            }

        } else {
            finish()
            Toast.makeText(this, "Data tidak ada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecyclerList(listKandungan: List<KandunganItem?>, listManfaat: List<ManfaatItem?>) {


        binding?.rvInformasi?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvManfaat?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val listKandunganAdapter = ListKandunganAdapter(listKandungan as List<KandunganItem>)
        val listManfaatAdapter = ListManfaatAdapter(listManfaat as List<ManfaatItem>)

        binding?.rvInformasi?.adapter = listKandunganAdapter
        binding?.rvManfaat?.adapter = listManfaatAdapter
    }

    private fun toggleCardViewHeightWithAnimation1() {
        val startHeight = binding?.cvInformasi?.height
        val endHeight = if (isExpanded) dpToPx(50) else ViewGroup.LayoutParams.WRAP_CONTENT

        val valueAnimator = ValueAnimator.ofInt(50, endHeight)
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
        val startHeight = binding?.cvManfaat?.height
        val endHeight = if (isExpanded2) dpToPx(50) else ViewGroup.LayoutParams.WRAP_CONTENT

        val valueAnimator = ValueAnimator.ofInt(50, endHeight)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            val layoutParams = binding?.cvManfaat?.layoutParams
            layoutParams?.height = animatedValue
            binding?.cvManfaat?.layoutParams = layoutParams
        }

        valueAnimator.duration = 300 // Set your desired animation duration here
        valueAnimator.start()

        isExpanded2 = !isExpanded2 // Toggle the state
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

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}