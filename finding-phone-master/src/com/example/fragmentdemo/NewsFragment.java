package com.example.fragmentdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NewsFragment extends Fragment {
    private SQLiteDatabase db;
    private DBopenhelper helper;
    private Cursor c;
    
    private Button btn;
    private Button btn_refresh;
    private EditText ques;
    private EditText answ;
    private FragmentManager manager;
    private FragmentTransaction ft;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.news_layout, container,
				false);
		 manager = getFragmentManager();
		
		helper =new DBopenhelper(getActivity(),"main_tb", null, 1);
        db= helper.getWritableDatabase();
		
		ques=(EditText) newsLayout.findViewById(R.id.editText1);
		answ=(EditText) newsLayout.findViewById(R.id.editText2);
		btn=(Button) newsLayout.findViewById(R.id.button1);
		btn_refresh=(Button) newsLayout.findViewById(R.id.button2);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String ss_ques=ques.getText().toString();
				String ss_answ=answ.getText().toString();
				
//				String ROW1 = "INSERT INTO main_tb ('question','answer') Values (ss_ques,ss_answ)";
//				db.execSQL(ROW1);
				ContentValues values = new ContentValues();
				values.put("question", ss_ques); 
				values.put("answer", ss_answ);

				// Inserting Row
				db.insert("main_tb", null, values);
			}
		});
		
		btn_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				SettingFragment myJDEditFragment = new SettingFragment();
//				ft = manager.beginTransaction();
//				//��ǰ��fragment�ᱻmyJDEditFragment�滻
//				ft.replace(R.id.content, myJDEditFragment);//�����content��ʲô��˼����Ҳ��̫����
//				ft.addToBackStack(null);
//				getActivity().getFragmentManager().beginTransaction().detach(NewsFragment.this);
//				ft.commit();
		
//				���ǲ�����fragment��ת��activity�Ĵ��룬���Ǻܺã�����Ǵ�fragment����fragment
				Intent intent_jump=new Intent(getActivity(), MainActivity.class);
				startActivity(intent_jump);
			
//				FragmentTransaction transaction = manager.beginTransaction();
				// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
//				hideFragments(transaction);
			}
		});
		
		return newsLayout;
	}

}
