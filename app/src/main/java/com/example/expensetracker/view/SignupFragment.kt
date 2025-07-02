package com.example.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.expensetracker.databinding.FragmentSignupBinding
import com.example.expensetracker.viewmodel.SignViewModel


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val firstName = binding.txtFirstname.text.toString().trim()
            val lastName = binding.txtLastname.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()
            val repeatPassword = binding.txtRepeatPassword.text.toString().trim()

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(view.context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(view.context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signUp(username, password, firstName, lastName)
        }

        viewModel.signUpStatus.observe(viewLifecycleOwner) {
            when (it) {
                "success" -> {
                    Toast.makeText(context, "Account Signup successfully", Toast.LENGTH_SHORT).show()
                    val action = SignupFragmentDirections.actionSigninFragment()
                    Navigation.findNavController(view).navigate(action)
                }
                "exists" -> {
                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                }
                "error" -> {
                    Toast.makeText(context, "Error during signup", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}