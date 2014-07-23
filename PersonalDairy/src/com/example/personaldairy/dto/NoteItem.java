package com.example.personaldairy.dto;

public class NoteItem {

	int Id;
	String Note;
	String Date;
	
	public NoteItem(int id,String Note,String Date)
	{
		this.Id=id;
		this.Note =Note;
		this.Date =Date;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	
}
