package smartmeca.smar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import smartmeca.smar.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class save extends Activity  {
	String Name;
	Button ok,cancel;
	EditText ed1; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.save);
		//detecter si le fichier contient un titre déja
		Name="";
		File f=null;
		f=new File(Environment.getExternalStorageDirectory()+File.separator+"SMAR2");
		if(!f.exists() && !f.isDirectory()){
		f.mkdirs();
		
		}
		//rajouter le fait de pouvoir enregistrer!!!
		
		try{
		  FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
		  DataInputStream in = new DataInputStream(fstream);
          BufferedReader br = new BufferedReader(new InputStreamReader(in));
          String line="";
          if((line = br.readLine()) != null) {
        	  if(line.contains("//*")){
        		  if(line.length()>3)
        			  Name=line.substring(3);
        		  
        	  }else if(line.contains("//")){

        		  if(line.length()>2)
        			  Name=line.substring(2);
        	  }
        	  br.close();
        	  in.close();
          }
		}catch(Exception e){
			
		}
		
		//definition des views
		ed1=(EditText)findViewById(R.id.editName);
		ok=(Button)findViewById(R.id.ok);
		cancel=(Button)findViewById(R.id.cancel);
		ed1.setText(Name);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i1=new Intent("com.example.smar.GLExample");
				startActivity(i1);
			
			}
		});
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ed1.getText().length()>0){
					Name=String.format("%s",ed1.getText());
					if(!Name.contains(".smr")){
						Name+=".smr";
					}
					try{
						  FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
						  DataInputStream in = new DataInputStream(fstream);
				          BufferedReader br = new BufferedReader(new InputStreamReader(in));
				          String line="";
				          String tmp="";
				          while((line = br.readLine()) != null) {
				        	  if(!line.contains("//")){
				        		  tmp+=line+"\n";
				        	  }
				          }
				          br.close();
				          in.close();
				          File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/"+Name);
						  FileWriter fw=new FileWriter(file,false);//write
						  BufferedWriter bfw=new BufferedWriter(fw);
						  bfw.write(tmp);
						  bfw.flush();
						  bfw.close();
						  file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
						  fw=new FileWriter(file,false);//write
						  bfw=new BufferedWriter(fw);
						  Name=String.format("//%s\n", Name);
						  bfw.write(Name);
						  bfw.flush();
						  bfw.write(tmp);
						  bfw.flush();
						  bfw.close();
							Intent i1=new Intent("com.example.smar.GLExample");
							startActivity(i1);
					
					}catch(Exception e){
						
					}
					
				}else{
					TextView title=(TextView)findViewById(R.id.title);
					title.setText("Please provide a valid File Name");
					
					
				}
			}
			
		});
		
		
	}

	
	
}
