package com.example.awosclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.awosclient.model.Case;
import com.example.awosclient.tasks.FetchCasesTask;
import com.example.awosclient.tasks.UpdateComplaintTask;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class CaseListFragment extends ListFragment implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener, OnItemSelectedListener {
	
	ArrayList<Case> mItems;
	CaseAdapter mCaseAdapter;
	GoogleApiClient mGoogleApiClient;
	int mIdOfSelectedItem = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Activity a = this.getActivity();
		if( a == null)
			return;
		
		SharedPreferences authDetails = a.getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String token = authDetails.getString("token", null);
		String email = authDetails.getString("email", null);
		
		/*
		 this.mGoogleApiClient =  new GoogleApiClient.Builder(this.getActivity())
         .addConnectionCallbacks(this)
         .addOnConnectionFailedListener(this).addApi(Plus.API)
         .addScope(Plus.SCOPE_PLUS_LOGIN).build();
         */
		 
		
		//*********************************
		// This fragment can be associated
		// with multiple activities so 
		// check if token is provided
		// if not return
		//*********************************
		if( (token == null) )
			return;
		
		this.setHasOptionsMenu(true);
		getActivity().setTitle(R.string.title_activity_case_list);
		
		
		// Create Async task to retrieve the data
		FetchCasesTask backgroundTask = new FetchCasesTask(token, email, this);
		backgroundTask.execute();
		
		
		
	}



	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}





	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		mIdOfSelectedItem = position;
	}





	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//mGoogleApiClient.connect();
		
		
		
	}





	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	    
	    }
		
	





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		 View view = inflater.inflate(R.layout.list_fragment, container, false);
		
		
		 
		 if( view != null)
		 {
			 Button btnLogout = (Button)view.findViewById(R.id.LogoutButton);
			 btnLogout.setOnClickListener(this);
			 
			 Button btnAddCase = (Button)view.findViewById(R.id.addButton);
			 btnAddCase.setOnClickListener(this);
			 
			 
			 
		 }
		 
		

		 return view;
	
	
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		 //
	}





	public void setAdapter()
	{
		
		if( mItems != null)
		{
			mCaseAdapter = new CaseAdapter(this);
			this.getListView().setAdapter(mCaseAdapter);
			
		}
		
	}
	
	public void setListData(ArrayList<Case> caseList)
	{
		mItems = caseList;
	}
	






@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	int id = v.getId();
	
	switch(id)
	{
		case R.id.LogoutButton:
			handleLogOut();
		break;
		
		case R.id.addButton:
			handleAddCase();
		break;
		
	}
	
}


private void handleLogOut()
{
	//*********************************
	// Logout from GPlus & move to
	// Main Activity
	//******************************
	/*
    Intent i = new Intent(this.getActivity(), MainActivity.class);
    i.putExtra("loggedInState", true);
    this.getActivity().startActivity(i);
    */
	this.getActivity().finish();
	
	
}


private void handleAddCase()
{
	// Launch Activity to Add a new Complaint
	
	Intent i = new Intent(this.getActivity(), ComplaintFormActivity.class);
	this.getActivity().startActivity(i);
	
	
}


@Override
public void onConnectionFailed(ConnectionResult result) {
	// TODO Auto-generated method stub
	
	if( result.hasResolution())
	{
		 try {
			result.startResolutionForResult(this.getActivity(), 0);
		} catch (SendIntentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else
	{
		//this.mGoogleApiClient.connect();
	}
	
}





@Override
public void onConnected(Bundle connectionHint) {
	// TODO Auto-generated method stub
	
}


@Override
public void onConnectionSuspended(int arg0) {
    // TODO Auto-generated method stub
  //  mGoogleApiClient.connect();
    // updateUI(false);
}




private class CaseAdapter extends ArrayAdapter<Case> implements OnClickListener
{
	CaseListFragment fragment = null;
	public CaseAdapter(CaseListFragment fragment)
	{
		super(getActivity(), 0, mItems);
		this.fragment = fragment;
		
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null)
		{
			convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_case, null);
		}
		
		Case theCase = getItem(position);
		
		TextView caseTitleView = (TextView) convertView.findViewById(R.id.case_list_item_title);
		CheckBox caseSolvedCB = (CheckBox) convertView.findViewById(R.id.case_list_item_solvedCheckBox);
		TextView caseSubmittedDateTV = (TextView)convertView.findViewById(R.id.case_list_item_date);
		
		caseSolvedCB.setOnClickListener(this);
		caseSolvedCB.setTag(new Integer(position) );
		caseTitleView.setText(theCase.getComplaint());
		
		String status = theCase.getStatus();
		caseSolvedCB.setChecked(theCase.isCaseSolved());
		
		Date submittedDate = theCase.getCreatedDate();
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yy");
		String formattedDate = df.format(submittedDate);
		caseSubmittedDateTV.setText(formattedDate);
		
		
		
			
		
		
		return convertView;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		// Create a task to update the record 
		
		CheckBox cb = (CheckBox)v;
		Integer position = (Integer)cb.getTag();
		
		UpdateComplaintTask updateTask = new UpdateComplaintTask(fragment, position.intValue());
		updateTask.execute();
		
		
		
	}
}




@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// TODO Auto-generated method stub
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.fragment_case_list, menu);
}





@Override
public void onItemSelected(AdapterView<?> parent, View view, int position,
		long id) {
	// TODO Auto-generated method stub
	
	//***********************************
	// This callback is called when List
	// item is selected
	//************************************
	this.mIdOfSelectedItem = this.mItems.get(position).getId();	
}


public int getIdofSelectedItem()
{
	 Case aCase = this.mItems.get(this.mIdOfSelectedItem);
	 return aCase.getId();
	 
}


@Override
public void onNothingSelected(AdapterView<?> parent) {
	// TODO Auto-generated method stub
	
}

} // end outer class
