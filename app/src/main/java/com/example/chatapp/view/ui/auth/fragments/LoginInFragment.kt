package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatapp.databinding.ItemTabloginLoginBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth


class LoginInFragment : Fragment() {

    private lateinit var binding: ItemTabloginLoginBinding

    private var mainBinding: com.example.chatapp.databinding.ActivityAuthBinding? = null


    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemTabloginLoginBinding.inflate(inflater, container, false)

        mainBinding = (activity as? AuthActivity)?.binding

        return binding.getRoot()
    }

    override fun onStart() {
        super.onStart()
        binding.btnLogar.setOnClickListener {
            it.hideKeyboard()
            logarUsuario()
        }

    }

    private fun logarUsuario() {

       // mainBinding?.pbLoading?.visibility = View.VISIBLE
        binding.inputLoginSenha.setError(null);
        binding.inputLoginEmail.setError(null);

        val email = binding.txtEmail.text.toString()
        val senha = binding.txtSenha.text.toString()
        Log.i("info_app", "logarUsuario: ${email} -  ${senha}")

       if(validarCampos(email, senha)){
           autenticacao.signInWithEmailAndPassword(
               email,
               senha
           ).addOnSuccessListener {
               exibirMensagem("usuario logado com sucesso")
               //mainBinding?.pbLoading?.visibility = View.INVISIBLE
               Log.i("info_app", "logarUsuario: logado")
               verificarUsuarioLogado()
           }.addOnFailureListener { exception ->
               //binding.textView.text = "erro: ${exception.message}"
               /*mainBinding?.pbLoading?.visibility = View.INVISIBLE
               Log.i("info_app", "logarUsuario: ${exception.message}")*/

               if(exception.message == "The password is invalid or the user does not have a password."){
                   binding.inputLoginSenha.error = "Senha invalida"
                  // mainBinding?.pbLoading?.visibility = View.INVISIBLE
               }else{
                  // mainBinding?.pbLoading?.visibility = View.INVISIBLE
                   exibirMensagem("email nao registrado, registre-se agora!")
                   val tab: TabLayout.Tab? = mainBinding?.tabAuth?.getTabAt(1)

                   if (tab != null) {
                       tab.select() // Move para a segunda aba
                   }
               }

           }
       }
    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(binding.btnLogar.context, texto, Toast.LENGTH_LONG).show()
        Log.i("info_app", "exibirMensagem: ${texto}")

    }

    private fun verificarUsuarioLogado() {
        val usuario = autenticacao.currentUser
        val id = usuario?.uid

        if (usuario != null) {
           // mainBinding?.pbLoading?.visibility = View.INVISIBLE
          //  val intent = Intent(binding.btnLogar.context, PrincipalActivity::class.java)
           // startActivity(intent)
        } else {
            exibirMensagem("nao tem usuario Logado")
        }


    }

    private fun validarCampos(email: String, senha: String): Boolean {
        val formatoEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (email.isNotEmpty() && formatoEmail) {
            if (senha.isNotEmpty() && senha.length >= 8) {
                return true

            } else {
                binding.inputLoginSenha.error = "insira um senha mais forte"
             //   mainBinding?.pbLoading?.visibility = View.INVISIBLE
                return false
            }

        } else {
            binding.inputLoginEmail.error = "insira um email valido"
           // mainBinding?.pbLoading?.visibility = View.INVISIBLE
            return false
        }



    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }




}