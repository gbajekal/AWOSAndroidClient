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

import com.example.awosclient.CaseListFragment;
import com.example.awosclient.model.Case;

import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

public class FetchCasesTask extends AsyncTask<Void, Void, ArrayList<Case>> {
	
	private static String ENDPOINT = "https://awosapi-dot-awos-beta.appspot.com/complaints";
	

	String gPlusToken;
	String loggedInUser;
	CaseListFragment mFragment;
	private String TAG_ID = "id";
	private String TAG_Complaint = "Complaint";
	private String TAG_Status = "Status";
	private String TAG_DATE_RESOLVED = "Date_Resolved";
	private String TAG_DATE_CREATED = "Date_Created";
	private String TAG_CATEGORY = "Category";
	private String TAG_COMMENT = "Comment";
	private String TAG_SUBMITTER = "Submitter";
	
	public FetchCasesTask( String token, String user, CaseListFragment aFragment)
	{
		gPlusToken = token;
		loggedInUser = user;
		mFragment = aFragment;
	}
	
	
	@Override
	protected ArrayList<Case> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		ArrayList<Case> caseList = new ArrayList<Case>();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(this.ENDPOINT);
		
		try{
			List nameValuePairs = new ArrayList();
			nameValuePairs.add(new BasicNameValuePair("idToken", this.gPlusToken) );
			nameValuePairs.add(new BasicNameValuePair("user", loggedInUser ) );
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs) );
			
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			final String responseBody = EntityUtils.toString(response.getEntity());
			Log.i("GRB", "Reponse is =" + responseBody);
			
			if ( (statusCode == 200) && (responseBody != null))
			{ 
				
				JSONObject jsonObj = new JSONObject(responseBody);
				JSONArray complaints = jsonObj.getJSONArray("data");
				
				for( int i = 0; i < complaints.length(); i++)
				{
					JSONObject aCase = complaints.getJSONObject(i);
					Case theCase = new Case();
					
					theCase.setId(aCase.getInt(TAG_ID));
					theCase.setComplaint(aCase.getString(TAG_Complaint));
					theCase.setStatus(aCase.getString(TAG_Status));
					theCase.setCategory(aCase.getString(TAG_CATEGORY));
					String createdDate = aCase.getString(TAG_DATE_CREATED);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date cd = df.parse(createdDate);
					theCase.setCreatedDate(cd);
					//Date rd = new SimpleDateFormat().parse(aCase.getString(TAG_DATE_RESOLVED));
					//theCase.setResolvedDate(rd);
					caseList.add(theCase);
					
					
					
				}
				
				
				
				
				
			}
			
			
			
			
		}
		catch(JSONException e)
		{
			Log.e("GRB", "Failed to parse JSON string", e);
		}
		catch(ClientProtocolException e)
		{
			Log.e("GRB", "Error sending ID Token to Backend", e);
			
		}
		catch(IOException e)
		{
			Log.e("GRB", "Error sending ID Token to Backend", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e("GRB", "Error parsing date", e);
			e.printStackTrace();
		}
		
		
		
		
		return caseList;
	}


	@Override
	protected void onPostExecute(ArrayList<Case> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		// send data back to the UX thread 
		this.mFragment.setListData(result);
		this.mFragment.setAdapter();
		
		
		
	}

}
