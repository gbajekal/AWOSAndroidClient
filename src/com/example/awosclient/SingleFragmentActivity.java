package com.example.awosclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public abstract class SingleFragmentActivity extends ActionBarActivity {
	
protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = this.getSupportFragmentManager();
		
		Fragment fragment =  fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null)
		{
			// add the fragment to the view
			
		 fragment = createFragment();
			
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
			
		}
		
	}
	
	
	

}
