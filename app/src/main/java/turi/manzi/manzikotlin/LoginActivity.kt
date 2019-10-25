package turi.manzi.manzikotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    internal lateinit var mGoogleSignInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    private var mCallbackManager: CallbackManager? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sign_in_button.setOnClickListener(this)
        //Instantiate FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
                firebaseAuth: FirebaseAuth ->

            user = firebaseAuth.currentUser

            if(user != null) {
                //Let's go to Dashboard
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else {

                Toast.makeText(this, "Not Signed In",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
//Configure Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //Build a googleSign in client with the options specified by the gso
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        sign_in_button.setSize(SignInButton.SIZE_WIDE)

//        Initialize FaceBook Login Button
        mCallbackManager = CallbackManager.Factory.create()
        login_button.setPermissions("email", "public_profile")
        login_button.registerCallback(mCallbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d(TAGs,"facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult!!.accessToken)
            }

            override fun onCancel() {
                Log.d(TAGs,"facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAGs,"facebook:onError", error)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.login_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
            if (item.itemId == R.id.disconnectButton) {
                //Log the user out & disconnect!

                revokeAccess()
            }
            if (item.itemId == R.id.signOutButton) {
                //Log the user out!
                signOut()
            }
        return true
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if(currentUser == null){
            Log.d(TAG, "Currently signed in: "+ currentUser?.email)
            Toast.makeText(this,"Currently logged in: "+ currentUser?.email, Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            sign_in_button.id -> signInToGoogle()
//            signOutButton.id -> signOut()
//            disconnectButton.id -> revokeAccess()
        }
    }
    private fun revokeAccess(){
        mAuth!!.signOut()
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this)
        {
            Log.w(TAG,"Revoked Access")
        }
    }

    private fun signOut(){
        mAuth!!.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener(this)
        {
            Log.w(TAG,"Signed out of Google")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(this,"Google Sign in Succeeded", Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException){
                Log.w(TAG, "Google Sign in Failed", e)
                Toast.makeText(this,"Google Sign in Failed $e", Toast.LENGTH_LONG).show()
            }
        }
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken){
        Log.d(TAGs, "firebaseAuthWithGoogle: " + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
//                SignIn success UPDATE UI with user sign in credentials
                    val user = mAuth!!.currentUser
                    Log.d(TAGs, "signInWithCredential: success: currentUser: " + user!!.email)
                    Toast.makeText(this,"Authentication Success ", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivityForResult(intent,RC_SIGN_INs)
                } else {
                    Log.w(TAGs, "signInWithCredential: Failed ", task.exception)
                    Toast.makeText(this,"Authentication Failed: " + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        Log.d(TAG, "firebaseAuthWithGoogle: " + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
//                SignIn success UPDATE UI with user sign in credentials
                    val user = mAuth!!.currentUser
                    Log.d(TAG, "signInWithCredential: success: currentUser: " + user!!.email)
                    Toast.makeText(this,"FireBase Authentication Success ", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivityForResult(intent,RC_SIGN_IN)
                } else {
                    Log.w(TAG, "signInWithCredential: Failed ", task.exception)
                    Toast.makeText(this,"FireBase Authentication Failed: " + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signInToGoogle(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object{
        private const val TAG = "Google Activity"
        private const val RC_SIGN_IN = 9001
        private const val TAGs = "FacebookLogin"
        private const val RC_SIGN_INs = 12345
    }


}
