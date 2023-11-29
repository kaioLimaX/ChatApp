package com.example.chatapp.data

import android.util.Log
import com.example.chatapp.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthRepository {


    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Sucess(result.user!!)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)

        }

    }

    override suspend fun signup(
        email: String,
        password: String,
        name: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.addOnCompleteListener {


                val uid = result?.user!!.uid

                val usuario = mapOf(
                    "nome" to name,
                    "email" to email,
                    "id" to uid,
                )

                Log.i("info_db", "signup: $usuario")

                db.collection("usuarios").document("${uid}")
                    .set(usuario)
                    .addOnSuccessListener {
                        Log.d(
                            "info_db",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w("info_db", "Error writing document", e) }
            }






            Resource.Sucess(result.user!!)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "falhou")
            Resource.Failure(e)

        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}