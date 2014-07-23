package com.example.personaldairy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.R;
import android.R.bool;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personaldairy.adapters.LVDairyNote;
import com.example.personaldairy.dto.NoteItem;
import com.example.personaldairy.model.DairyNotes;

public class Dairy extends ListActivity implements OnClickListener{

	private DairyNotes mDairyNotes ;
	private Context mContext;
	private Cursor mCursor;
	private Button mSave;
	private Button mCancel;

	private Button btnConfirm;
	private Button btnCancel;

	private Dialog addDairyNoteDialog;
	private Dialog deleteDairyNoteDialog;
	private EditText mDairyNote;
	private String mDairyNote_string;
	private LVDairyNote mLVDairyNote;	
	ArrayList <NoteItem> Notes = new ArrayList<NoteItem>();



	private Boolean mSearch=true;
	private int delId;
	private Button mESave;
	private Button mECancel;
	private String NoteText;
	private ListView list;
	private Button mBtnView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent()); 
		setContentView(R.layout.activity_dairy);

		

		

	}

	@Override
	protected void onResume() {
		if(mSearch)
		{
			Notes.clear();
			mDairyNotes = new DairyNotes(this);
			mContext = Dairy.this;		
			mCursor= mDairyNotes.getSqlData();
			showSqlData(mCursor);
			mLVDairyNote = new LVDairyNote(mContext, Notes);		 
			setListAdapter(mLVDairyNote);
		}
		super.onResume();
	}
	public void onNewIntent(Intent intent) { 
		setIntent(intent); 
		handleIntent(intent); 
	} 

	private void handleIntent(Intent intent) { 
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
			String query = intent.getStringExtra(SearchManager.QUERY); 
			doSearch(query); 
		} 
	}    


	private void doSearch(String queryStr) { 
		System.out.println("queryStr"+queryStr);

		if(mDairyNotes==null)
			mDairyNotes = new DairyNotes(this);
		mCursor= mDairyNotes.getSqlSearchData(queryStr);
		Notes.clear();
		showSqlData(mCursor);

		Bundle b = new Bundle();



		mLVDairyNote = new LVDairyNote(this, Notes);
		setListAdapter(mLVDairyNote);
		mSearch = false;
	} 


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		int itemPosition     = position;
		delId = Notes.get(itemPosition).getId();

		NoteText = Notes.get(itemPosition).getNote();
		deleteNotesDialog();
	}



	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dairy, menu);
		MenuItem searchItem = menu.findItem(R.id.search);		
		SearchView searchView = (SearchView) searchItem.getActionView();
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if(null!=searchManager ) {   
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}
		searchView.setIconifiedByDefault(false);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addNote:
			Intent in = new Intent(this,AddNotes.class);
			startActivity(in);
			break;
		}
		return true;
	}
	private void addNotesDialog()
	{
		addDairyNoteDialog = new Dialog(this,R.style.FullHeightDialog);
		addDairyNoteDialog.setContentView(R.layout.dialog_adddairynote);		 
		mDairyNote = (EditText)addDairyNoteDialog.findViewById(R.id.etDairyNote);
		mSave = (Button)addDairyNoteDialog.findViewById(R.id.btnSave);
		mCancel = (Button)addDairyNoteDialog.findViewById(R.id.btnCancel);

		mSave.setOnClickListener(this);
		mCancel.setOnClickListener(this);

		addDairyNoteDialog.show();
	}
	private void deleteNotesDialog()
	{
		deleteDairyNoteDialog = new Dialog(this,R.style.FullHeightDialog);
		deleteDairyNoteDialog.setContentView(R.layout.dialog_alert);		 
		btnConfirm = (Button)deleteDairyNoteDialog.findViewById(R.id.btnAlertConfirm);
		btnCancel = (Button)deleteDairyNoteDialog.findViewById(R.id.btnAlertCancel);
		mBtnView = (Button)deleteDairyNoteDialog.findViewById(R.id.btnView);
		btnConfirm.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		mBtnView.setOnClickListener(this);
		deleteDairyNoteDialog.show();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSave:
			mDairyNote_string=mDairyNote.getText().toString();
			Calendar c = Calendar.getInstance(); 
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			String date = df.format(new Date());
			//	mDairyNotes.addSqlData(mDairyNote_string,date);
			mCursor= mDairyNotes.getSqlData();
			Notes.clear();
			showSqlData(mCursor);	
			mLVDairyNote.updateAdapter(Notes);
			addDairyNoteDialog.dismiss();
			break;
		case R.id.btnCancel:
			addDairyNoteDialog.dismiss();
			break;
		case R.id.btnAlertConfirm:
			String id = ""+delId;
			UpdateNotes(id,NoteText);
			deleteDairyNoteDialog.dismiss();
			break;
		case R.id.btnView:
			Intent in = new Intent(this,ShowNotes.class);
			in.putExtra("id", delId);
			startActivity(in);
			deleteDairyNoteDialog.dismiss();
			break;
		case R.id.btnAlertCancel:
			mDairyNotes.deleterow(delId);
			mCursor= mDairyNotes.getSqlData();
			Notes.clear();
			showSqlData(mCursor);	
			mLVDairyNote.updateAdapter(Notes);
			deleteDairyNoteDialog.dismiss();
			Toast.makeText(this, "deleted successfully.", 3000).show();
			break;
		default:
			break;
		}
	}
	private void showSqlData(Cursor cursor) {
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String message = cursor.getString(1);
				String date = cursor.getString(2);
				NoteItem n1= new NoteItem(id, message, date);
				Notes.add(n1);
				System.out.println("message"+message);
			} while (cursor.moveToNext());
		}

	}

	public void UpdateNotes(final String delId, String Note)
	{
		addDairyNoteDialog = new Dialog(this,R.style.FullHeightDialog);
		addDairyNoteDialog.setContentView(R.layout.dialog_adddairynote);		 
		mDairyNote = (EditText)addDairyNoteDialog.findViewById(R.id.etDairyNote);
		mDairyNote.setText(Note);
		mESave = (Button)addDairyNoteDialog.findViewById(R.id.btnSave);
		mECancel = (Button)addDairyNoteDialog.findViewById(R.id.btnCancel);
		mESave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDairyNotes.UpdateStatus(delId, mDairyNote.getText().toString());
				mCursor= mDairyNotes.getSqlData();
				Notes.clear();
				showSqlData(mCursor);	
				mLVDairyNote.updateAdapter(Notes);
				addDairyNoteDialog.dismiss();

			}
		});
		mECancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addDairyNoteDialog.dismiss();
			}
		});
		addDairyNoteDialog.show();
	}


}

