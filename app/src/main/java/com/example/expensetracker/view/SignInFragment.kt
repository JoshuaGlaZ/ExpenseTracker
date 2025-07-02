package com.example.expensetracker.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.expensetracker.databinding.FragmentSignInBinding
import com.example.expensetracker.viewmodel.SignViewModel


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)

        // Auto login
        val user = viewModel.getCurrentUser()
        if (user != null) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignin.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.signIn(username, password)
        }

        binding.btnSignup.setOnClickListener {
            val action = SignInFragmentDirections.actionSignupFragment()
            Navigation.findNavController(it).navigate(action)
        }

        viewModel.signInStatus.observe(viewLifecycleOwner) {
            when (it) {
                "success" -> {
                    Toast.makeText(context, "Account Signin successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
                "error" -> {
                    Toast.makeText(context, "Error during signin", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}