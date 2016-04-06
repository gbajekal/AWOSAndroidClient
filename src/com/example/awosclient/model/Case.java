package com.example.awosclient.model;

import java.util.Date;

// This class represents a Case or a complaint

public class Case {
	
	
	private int mId;
	private String mComplaint;
	private Date mCreatedDate;
	private Date mResolvedDate;
	private String mComments;
	private String mSubmitter;
	private String mCategory;
	private String mStatus;
	
	

	public Case() {
		// TODO Auto-generated constructor stub
	}



	public Case(int id, String complaint, Date createdDate, Date resolvedDate,
			String comments, String submitter, String category, String status) {
		super();
		this.mId = id;
		this.mComplaint = complaint;
		this.mCreatedDate = createdDate;
		this.mResolvedDate = resolvedDate;
		this.mComments = comments;
		this.mSubmitter = submitter;
		this.mCategory = category;
		this.mStatus = status;
	}



	public int getId() {
		return this.mId;
	}



	public void setId(int id) {
		this.mId = id;
	}



	public String getComplaint() {
		return this.mComplaint;
	}



	public void setComplaint(String complaint) {
		this.mComplaint = complaint;
	}



	public Date getCreatedDate() {
		return this.mCreatedDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.mCreatedDate = createdDate;
	}



	public Date getResolvedDate() {
		return this.mResolvedDate;
	}



	public void setResolvedDate(Date resolvedDate) {
		this.mResolvedDate = resolvedDate;
	}



	public String getComments() {
		return this.mComments;
	}



	public void setComments(String comments) {
		this.mComments = comments;
	}



	public String getSubmitter() {
		return this.mSubmitter;
	}



	public void setSubmitter(String submitter) {
		this.mSubmitter = submitter;
	}



	public String getCategory() {
		return this.mCategory;
	}



	public void setCategory(String category) {
		this.mCategory = category;
	}



	public String getStatus() {
		return this.mStatus;
	}



	public void setStatus(String status) {
		this.mStatus = status;
	}
	
	public boolean isCaseSolved()
	{
		if( this.mStatus.equals("Pending"))
			return false;
		else
			return true;
	}

}
