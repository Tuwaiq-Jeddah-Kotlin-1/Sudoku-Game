package com.renad.sudoku.ui.main_activity.fragments.main_fragment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.renad.sudoku.R
import com.renad.sudoku.databinding.MainFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var binding: MainFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val bounceAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.button_bounce)
        binding.logo.setBackgroundResource(R.drawable.logo_animated);
        val frameAnimation: AnimationDrawable = binding.logo.background as AnimationDrawable
        frameAnimation.start()

        binding.medalBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.medalBtn.startAnimation(bounceAnim)
                delay(400)
            }
        }

        //display leaderboard
        binding.leaderboardBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.leaderboardBtn.startAnimation(bounceAnim)
                delay(400)
            }
        }

        //navigate to settings fragment
        binding.settingsBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.settingsBtn.startAnimation(bounceAnim)
                delay(400)
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            }
        }

        //game rolls feedback
        binding.questionMarkBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.questionMarkBtn.startAnimation(bounceAnim)
                delay(400)
            }
        }


        //back to the previous level
        var level = resources.getStringArray(R.array.levelArray)
        var pointer = 0
        binding.levelText.setText(level[pointer])
        binding.levelBackBtn.setOnClickListener {
            if (pointer > 0) {
                pointer -= 1
                binding.levelBackBtn.startAnimation(bounceAnim)
            }
            when (pointer) {
                0 -> binding.levelText.setText(level[0])
                1 -> binding.levelText.setText(level[1])
                2 -> binding.levelText.setText(level[2])
            }
            Toast.makeText(context, "pointer = $pointer", Toast.LENGTH_SHORT).show()

        }
        //forward to the next level
        binding.levelForwardBtn.setOnClickListener {
            if (pointer < 2) {
                pointer += 1
                binding.levelForwardBtn.startAnimation(bounceAnim)
            }
            when (pointer) {
                0 -> binding.levelText.setText(level[0])
                1 -> binding.levelText.setText(level[1])
                2 -> binding.levelText.setText(level[2])
            }
            Toast.makeText(context, "pointer = $pointer", Toast.LENGTH_SHORT).show()
        }

        binding.playBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.playBtn.startAnimation(bounceAnim)
                binding.playBtnText.startAnimation(bounceAnim)
                delay(400)
                findNavController().navigate(R.id.action_mainFragment_to_gameFragment)
            }
        }

        binding.continueBtn.setOnClickListener {
            GlobalScope.async(Dispatchers.Main) {
                binding.continueBtn.startAnimation(bounceAnim)
                binding.continueBtnText.startAnimation(bounceAnim)
                delay(400)
            }

        }
    }

}

