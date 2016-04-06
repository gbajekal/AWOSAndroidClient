package com.example.awosclient;

import com.example.awosclient.tasks.AddComplaintTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ComplaintFormFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	
	EditText etComplaintDesc;
	String complaint;

	Button   btnSubmit;
	Spinner  spCategoryList;
	String   category;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.complaint_form_fragment, container, false);
		
		etComplaintDesc = (EditText) v.findViewById(R.id.etComplaint);
		final Spinner spCategoryList = (Spinner) v.findViewById(R.id.SpCategories);
		ArrayAdapter<CharSequence> catData = ArrayAdapter.createFromResource(this.getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
		catData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCategoryList.setAdapter(catData);
		spCategoryList.setOnItemSelectedListener(this);
		
		btnSubmit = (Button)v.findViewById(R.id.btSubmitForm);
		btnSubmit.setOnClickListener(this);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return v;
		
		
		
		
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		complaint = this.etComplaintDesc.getText().toString();
		
		AddComplaintTask task = new AddComplaintTask(complaint, category, this);
		task.execute();
		
		
		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Object a = parent.getItemAtPosition(position);
		category = a.toString();
		
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		Object a = parent.getItemAtPosition(0);
		category = a.toString();
	}
	
	
	
	
	
	
	

}
