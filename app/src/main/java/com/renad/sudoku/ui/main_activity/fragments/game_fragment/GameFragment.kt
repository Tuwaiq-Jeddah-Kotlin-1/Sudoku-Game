package com.renad.sudoku.ui.main_activity.fragments.game_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.renad.sudoku.R
import com.renad.sudoku.databinding.GameFragmentBinding
import com.renad.sudoku.ui.main_activity.fragments.game_fragment.custom_view.SudokuBoardView

class GameFragment : Fragment(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: GameViewModel
    private lateinit var numberButtons: List<Button>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var binding: GameFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        binding.sudokuBoardView.registerListener(this)

        numberButtons = listOf(
            binding.oneBtn,
            binding.twoBtn,
            binding.threeBtn,
            binding.fourBtn,
            binding.fiveBtn,
            binding.sixBtn,
            binding.sevenBtn,
            binding.eightBtn,
            binding.nineBtn
        )

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener { }
        }

        //timer
        binding.timerText.text
        binding.stopBtn.setOnClickListener {}

        binding.resetBtn.setOnClickListener {}

        binding.eraseBtn.setOnClickListener {}

        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_mainFragment)
        }


    }

    override fun onCellTouched(row: Int, col: Int) {
    }
}
