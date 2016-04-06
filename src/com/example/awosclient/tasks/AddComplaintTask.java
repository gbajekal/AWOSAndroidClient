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
import android.widget.Toast;

public class AddComplaintTask extends AsyncTask<Void, Void, Integer> {
	
	private static String ENDPOINT = "https://awosapi-dot-awos-beta.appspot.com/add";
	

	String gPlusToken;
	String loggedInUser;
	ComplaintFormFragment mFragment;
	String mComplaint;
	String mCategory;
	
	private String TAG_ID = "id";
	private String TAG_NAME = "Name";
	private String TAG_CONTACT = "Contact";
	
	public AddComplaintTask( String complaint, String category, ComplaintFormFragment aFragment)
	{
		
		Activity a = aFragment.getActivity();
		SharedPreferences authDetails = a.getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String token = authDetails.getString("token", null);
		String email = authDetails.getString("email", null);
		
		gPlusToken = token;
		loggedInUser = email;
		mFragment = aFragment;
		this.mComplaint = complaint;
		this.mCategory = category;
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
			nameValuePairs.add(new BasicNameValuePair("complaint", mComplaint ) );
			nameValuePairs.add(new BasicNameValuePair("category", mCategory ) );
			nameValuePairs.add(new BasicNameValuePair("status", "Pending" ) );
			
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
		
		
		
		if(result.intValue() == 200)
		{
			// Move to Next Activity i.e. The List that should display newly added complaint
			
			Intent i = new Intent(mFragment.getActivity(), CaseListActivity.class);
 		mFragment.getActivity().startActivity(i);
			
		}
		else
		{
			Toast t = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Error in adding complaint", Toast.LENGTH_SHORT);
			t.show();
		}
		
		
	}
	
}