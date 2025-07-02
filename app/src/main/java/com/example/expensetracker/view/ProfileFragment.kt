package com.example.expensetracker.view

import SessionManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentProfileBinding
import com.example.expensetracker.model.User
import com.example.expensetracker.viewmodel.SignViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), ProfileListener {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: SignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)
        viewModel.updatePasswordStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                "success" -> {
                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    binding.txtOldPassword.text?.clear()
                    binding.txtNewPassword.text?.clear()
                    binding.txtConfirmPassword.text?.clear()
                }
                "incorrect_old_password" -> {
                    Toast.makeText(context, "Incorrect old password", Toast.LENGTH_SHORT).show()
                }
                "not_found", "not_logged_in" -> {
                    Toast.makeText(context, "User session error. Please re-signin", Toast.LENGTH_LONG).show()
                    viewModel.signOut()
                    val intent = Intent(requireActivity(), SignActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                "error" -> {
                    Toast.makeText(context, "Failed to update password. Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.updatePasswordStatus.postValue("")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
             binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }

    override fun onPasswordChangeClick(view: View) {
        val oldPassword = binding.txtOldPassword.text.toString().trim()
        val newPassword = binding.txtNewPassword.text.toString().trim()
        val confirmPassword = binding.txtConfirmPassword.text.toString().trim()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updatePassword(oldPassword, newPassword)
    }

    override fun onSignOutClick(view: View) {
        viewModel.signOut()

        val intent = Intent(requireActivity(), SignActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(context, "Account Signout successfully", Toast.LENGTH_SHORT).show()
    }
}