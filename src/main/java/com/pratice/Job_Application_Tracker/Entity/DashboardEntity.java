package com.pratice.Job_Application_Tracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dashboard")
public class DashboardEntity {

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalApplied() {
		return totalApplied;
	}

	public void setTotalApplied(int totalApplied) {
		this.totalApplied = totalApplied;
	}

	public int getInterviewsScheduled() {
		return interviewsScheduled;
	}

	public void setInterviewsScheduled(int interviewsScheduled) {
		this.interviewsScheduled = interviewsScheduled;
	}

	public int getOffersReceived() {
		return offersReceived;
	}

	public void setOffersReceived(int offersReceived) {
		this.offersReceived = offersReceived;
	}

	public int getRejections() {
		return rejections;
	}

	public void setRejections(int rejections) {
		this.rejections = rejections;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_applied")
    private int totalApplied;

    @Column(name = "interviews_scheduled")
    private int interviewsScheduled;

    @Column(name = "offers_received")
    private int offersReceived;

    @Column(name = "rejections")
    private int rejections;

    @Column(name = "user_name")
    private String userName;

 
}
