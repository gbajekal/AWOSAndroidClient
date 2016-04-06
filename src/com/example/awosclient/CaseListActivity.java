package com.example.awosclient;



import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;


public class CaseListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		
		//******************************************
		// Retrieve the token and email address of
		// the logged in user and send it to the 
		// fragment
		//********************************************
		
		return new CaseListFragment();
	}

}
