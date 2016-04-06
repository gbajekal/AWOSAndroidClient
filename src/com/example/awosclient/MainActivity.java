package com.example.awosclient;

import com.example.awosclient.tasks.FetchCasesTask;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	
private static final int RC_SIGN_IN = 0;
private static final String TAG = "GRB";
GoogleApiClient mGoogleApiClient;
SignInButton    btnSignIn;
Button			btnSignOut;
Button			btnRevokeAccess;
private boolean mSignInClicked;
private boolean mIntentInProgress;
private ConnectionResult mConnectionResult;
private TextView tvDisplayName;
private TextView tvEmail;





	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.btnSignIn = (SignInButton)this.findViewById(R.id.btn_sign_in);
		this.btnSignOut = (Button)this.findViewById(R.id.btn_sign_out);
		this.btnRevokeAccess = (Button)this.findViewById(R.id.btn_revoke_access);
		this.tvDisplayName = (TextView)this.findViewById(R.id.txtName);
		this.tvEmail       = (TextView)this.findViewById(R.id.txtEmail);
		
		this.btnSignIn.setOnClickListener(this);
		this.btnSignOut.setOnClickListener(this);
		this.btnRevokeAccess.setOnClickListener(this);
		
		// Initialize the GooglePlus Client
		try
		{
			this.validateServerClientID();
			//GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
	        .requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
			
			
			
			/*mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		    */
			
			mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
			
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	protected void onStart()
	{
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	protected void onStop()
	{
		super.onStop();
		if( mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch( v.getId())
		{
		case R.id.btn_sign_in:
			signInWithGPlus();
		break;
		
		case R.id.btn_sign_out:
			signOutFromGPlus();
		break;
		
		
		}
		
	}

	private void signOutFromGPlus() {
		// TODO Auto-generated method stub
		// Here we signout the user from Google and enable the sign in Button
		Auth.GoogleSignInApi.signOut(this.mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
				{
			
			 @Override
             public void onResult(Status status) {
                 // [START_EXCLUDE]
                 updateUI(false);
                 // [END_EXCLUDE]
             }
			
				}
				
				
				
				);
		
		
		
		
	}

	private void signInWithGPlus() {
		// TODO Auto-generated method stub
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient);
		this.startActivityForResult(signInIntent, RC_SIGN_IN);
				
		
		
	}
	
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if( requestCode == this.RC_SIGN_IN)
		{
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
		
		
		
	}

	private void handleSignInResult(GoogleSignInResult result) {
		// TODO Auto-generated method stub
		Log.d(TAG, "In handleSignInResult with status = " + result.isSuccess());
		if( result.isSuccess())
		{
			//Get signedIn users details
			
			GoogleSignInAccount account = result.getSignInAccount();
			Log.d(TAG, "Signed In User Name = " + account.getDisplayName());
			//tvDisplayName.setText("Welcome " + account.getDisplayName());
			//tvDisplayName.setText("Token ID:=" + account.getIdToken());
			tvEmail.setText("Your email is: " + account.getEmail());
			updateUI(true);
			
            //*********************************************************			
			// Save token in Sharedpreferences so that it is available
			// for all activities across the app
			//***********************************************************
			SharedPreferences authData = this.getSharedPreferences("user", Context.MODE_PRIVATE);
			authData.edit().putString("token", account.getIdToken()).commit();
			authData.edit().putString("email", account.getEmail()).commit();
			
			
			Intent i = new Intent(this, CaseListActivity.class);
			//i.putExtra("token", account.getIdToken());
			//i.putExtra("email", account.getEmail());
			
			
			
			this.startActivity(i);
			
			
			
		}
		else
		{
			Toast t = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
			t.show();
			
			
			
		}
		
		
	}

	private void updateUI(boolean signInFlag) {
		// TODO Auto-generated method stub
		
		if(signInFlag)
		{
			// user has signed in so hide the Sign In Button and Enable Sign Out Button
			this.btnSignIn.setVisibility(View.GONE);
			this.btnSignOut.setVisibility(View.VISIBLE);
			
		}
		else
		{
			// Clear the Text Box
			
			this.tvDisplayName.setText("");
			this.tvEmail.setText("");
			
			this.btnSignIn.setVisibility(View.VISIBLE);
			this.btnSignOut.setVisibility(View.GONE);
			
		}
		
		
	}

	private void resolveSignInError() {
		// TODO Auto-generated method stub
		if (mConnectionResult.hasResolution()) {
	        try {
	            mIntentInProgress = true;
	            mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	        } catch (SendIntentException e) {
	            mIntentInProgress = false;
	            mGoogleApiClient.connect();
	        }
	    }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
		// Check if the user is already connected
		
		boolean isUserLogged = this.getIntent().getBooleanExtra("loggedInState", false);
		
		if(isUserLogged)
		{
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	        mGoogleApiClient.disconnect();
			this.signOutFromGPlus();
			return;
		}
		
		
		Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		 if (!result.hasResolution()) {
		        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
		                0).show();
		        return;
		    }
		 
		    if (!mIntentInProgress) {
		        // Store the ConnectionResult for later usage
		        mConnectionResult = result;
		 
		        if (mSignInClicked) {
		            // The user has already clicked 'sign-in' so we attempt to
		            // resolve all
		            // errors until the user is signed in, or they cancel.
		            resolveSignInError();
		        }
		    }
		
		
	}
	
	  private void validateServerClientID() {
	        String serverClientId = getString(R.string.server_client_id);
	        String suffix = ".apps.googleusercontent.com";
	        if (!serverClientId.trim().endsWith(suffix)) {
	            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

	            Log.w(TAG, message);
	            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	        }
	    }
	
}
