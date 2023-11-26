package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatapp.databinding.ItemTabloginSigninBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignInFragment : Fragment() {

    private lateinit var binding: ItemTabloginSigninBinding

    private var mainBinding: com.example.chatapp.databinding.ActivityAuthBinding? = null

    private var fragmentContext: Context? = null

    private val autenticacao by lazy {
        Firebase.auth
    }

    private val db by lazy {
        Firebase.firestore
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ItemTabloginSigninBinding.inflate(inflater, container, false)

        mainBinding = (activity as? AuthActivity)?.binding

        return binding.getRoot()


    }

    override fun onStart() {
        super.onStart()
        binding.btnCriar.setOnClickListener {
            cadastroUsuario()
        }

        binding.txtSenha2.setOnEditorActionListener { it, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                it.hideKeyboard()
                cadastroUsuario()
                true // Indica que o evento foi tratado
            } else {
                false // Indica que o evento não foi tratado
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    private fun cadastroUsuario() {
      //  mainBinding?.pbLoading?.visibility = View.VISIBLE
        binding.inputNome.setError(null);
        binding.inputemail.setError(null);
        binding.inputSenha.setError(null);

        val email = binding.txtEmail.text.toString()
        val senha = binding.txtSenha2.text.toString()
        val nome = binding.txtNome.text.toString()

        if (validarCampos(nome, email, senha)) {
            autenticacao.createUserWithEmailAndPassword(
                email,
                senha
            ).addOnSuccessListener { authResult ->


                val email = authResult.user?.email
                val id = authResult.user?.uid
                val user = autenticacao.currentUser

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()

                if (user != null){

                    user.updateProfile(profileUpdate).addOnSuccessListener {

                        val usuario = mapOf(
                            "nome" to nome,
                            "email" to "${email}",
                            "id" to "${id}",
                        )
                        exibirMensagem("Sucesso ao cadastrar usuario: ${email} - ${id}")
                        //mainBinding?.pbLoading?.visibility = View.INVISIBLE
                        db.collection("usuarios").document("${id}")
                            .set(usuario)
                            .addOnSuccessListener {
                                Log.d(
                                    "info_db",
                                    "DocumentSnapshot successfully written!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w("info_db", "Error writing document", e) }
                        //binding.textView.text = "sucesso : ${authResult.user?.email}"
                       /* val intent = Intent(fragmentContext, PrincipalActivity::class.java)
                        startActivity(intent)*/
                    }.addOnFailureListener { exception ->
                        val mensagemErro = exception.message
                        // binding.textView.text = "ERRO : ${mensagemErro}"
                    }

                    }
                }









        }


    }

    private fun validarCampos(nome: String, email: String, senha: String): Boolean {
        if (nome.isNotEmpty()) {
            val formatoEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (email.isNotEmpty() && formatoEmail) {
                if (senha.isNotEmpty() && senha.length >= 8) {
                    return true

                } else {
                    binding.inputSenha.error = "insira um senha mais forte"
                   // mainBinding?.pbLoading?.visibility = View.INVISIBLE
                    return false
                }

            } else {
                binding.inputemail.error = "insira um email valido"
              //  mainBinding?.pbLoading?.visibility = View.INVISIBLE
                return false
            }

        } else {
            binding.inputNome.error = "insira um texto valido"
           // mainBinding?.pbLoading?.visibility = View.INVISIBLE
            return false
        }

    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(fragmentContext, texto, Toast.LENGTH_LONG).show()

    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}