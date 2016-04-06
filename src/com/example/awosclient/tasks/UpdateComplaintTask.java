package com.example.awosclient.tasks;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.awosclient.CaseListActivity;
import com.example.awosclient.CaseListFragment;
import com.example.awosclient.ComplaintFormFragment;
import com.example.awosclient.model.Case;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class UpdateComplaintTask extends AsyncTask<Void, Void, Integer> {
	
	private static String ENDPOINT = "https://awosapi-dot-awos-beta.appspot.com/add";
	

	String gPlusToken;
	String loggedInUser;
	CaseListFragment mFragment;
	int mCaseId;
	int mSelectedPosition;
	
	ArrayAdapter<Case> dataAdapter;
	
	
	private String TAG_ID = "id";
	private String TAG_NAME = "Name";
	private String TAG_CONTACT = "Contact";
	
	public UpdateComplaintTask(CaseListFragment aFragment, int selectedPosition)
	{
		
		Activity a = aFragment.getActivity();
		SharedPreferences authDetails = a.getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String token = authDetails.getString("token", null);
		String email = authDetails.getString("email", null);
		
		
		
		gPlusToken = token;
		loggedInUser = email;
		mSelectedPosition = selectedPosition;
		mFragment = aFragment;
		
		dataAdapter = (ArrayAdapter<Case>)this.mFragment.getListView().getAdapter();
		
		mCaseId = dataAdapter.getItem(mSelectedPosition).getId();
	
		
	}
	
	
	@Override
	protected Integer doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		ArrayList<String> catList = new ArrayList<String>();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(this.ENDPOINT);
		
		try{
			List nameValuePairs = new ArrayList();
			nameValuePairs.add(new BasicNameValuePair("idToken", this.gPlusToken) );
			nameValuePairs.add(new BasicNameValuePair("user", loggedInUser ) );
			nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(mCaseId) ) );
			nameValuePairs.add(new BasicNameValuePair("status", "Resolved" ) );
			
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs) );
			
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			final String responseBody = EntityUtils.toString(response.getEntity());
			Log.i("GRB", "Reponse is =" + responseBody);
			
			if ( (statusCode == 200) )
			{ 
				
				Log.i("GRB", "Reponse is =" + responseBody);
				return new Integer(200);
		
				
			}
			else
			{
				
				Log.i("GRB", "Reponse is =" + responseBody);
				return new Integer(300);
				
				
				
				
			}
			
			
			
			
		}
		catch(Exception e)
		{
			Log.e("GRB", "Failed to add complaint", e);
		}
		
		
		
		
		return new Integer(300);
		
	}


	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Toast msg;
		
		if(result.intValue() == 200)
		{
			
			dataAdapter.getItem(this.mSelectedPosition).setStatus("Resolved"); 
			msg = Toast.makeText(this.mFragment.getActivity(), "Updated record successfully", Toast.LENGTH_SHORT);
		}
		else
		{
			msg = Toast.makeText(this.mFragment.getActivity(), "Error in record Update. Please try again", Toast.LENGTH_SHORT);
		}
		
		msg.show();
		
     // Refresh the list
		
	 
	 
	 dataAdapter.notifyDataSetChanged();
	
		
		
	}
	
	
}