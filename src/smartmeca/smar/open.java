 package smartmeca.smar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import smartmeca.smar.R;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class open extends Activity {
	Button Bok;
	fileopnerUser B1;
	LinearLayout mainLay;
	class fileopner extends Button{
		String name;
		public fileopner(Context context,String title) {
			super(context);
			// TODO Auto-generated constructor stub
			name=title;
			setText(name);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					InputStream in = null;
				    OutputStream out = null;
				    try {
				        in = getResources().getAssets().open(name);
				    	File f=null;
						f=new File(Environment.getExternalStorageDirectory()+File.separator+"SMAR2");
						if(!f.exists() && !f.isDirectory()){
						f.mkdirs();
						
						}
						
					
						File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
					
				        out = new FileOutputStream(file);

				        byte[] buffer = new byte[1024];
				        int read;
				        while ((read = in.read(buffer)) != -1) {
				            out.write(buffer, 0, read);
				        }
				        in.close();
				        in = null;
				        out.flush();
				        out.close();
				        out = null;
						Intent i1=new Intent("com.example.smar.GLExample");
						startActivity(i1);

				    } catch (Exception e) {
				    	e.printStackTrace();
				    }
				}
				
			});
			
		}
		
	}
	class fileopnerUser extends Button{
		String name;
		public fileopnerUser(Context context,String title) {
			super(context);
			// TODO Auto-generated constructor stub
			name=title;
			setText(name);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				    try {
				    	  FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/"+name);
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
						  File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
						  FileWriter fw=new FileWriter(file,false);//write
						  BufferedWriter bfw=new BufferedWriter(fw);
						  name=String.format("//%s\n", name);
						  bfw.write(name);
						  bfw.flush();
						  bfw.write(tmp);
						  bfw.flush();
						  bfw.close();
						 Intent i1=new Intent("com.example.smar.GLExample");
						startActivity(i1);

				    } catch (Exception e) {
				    	e.printStackTrace();
				    }
				}
				
			});
			
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tree);	
		//definitions:
	//	Bok=(Button)findViewById(R.id.ok);
		mainLay=(LinearLayout)findViewById(R.id.listeMaillons);
		TextView t1=new TextView(this);
		t1.setText("Examples");
		mainLay.addView(t1);
		
		 try {
			for(int i=0;i<getResources().getAssets().list("").length;i++){
				if(getResources().getAssets().list("")[i].contains(".smr")){
				 Bok=new fileopner(this,getResources().getAssets().list("")[i]);
				 mainLay.addView(Bok);
				}
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView t2=new TextView(this);
		t2.setText("User Files");
		mainLay.addView(t2);
		try{
			File f=new File(Environment.getExternalStorageDirectory()+File.separator+"SMAR2");
			for(int i=0;i<f.list().length;i++){
				
				if(f.list()[i].contains(".smr") && f.list()[i].compareTo("tmp.smr")!=0){
				  B1=new fileopnerUser(this,f.list()[i]);
				  mainLay.addView(B1);
				}
			}
		}catch(Exception e){
			
		}
	}
		

}
