package smartmeca.smar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import smartmeca.smar.R;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class addMaillon extends Activity {
	Button Bok,D,D1,D2,D3,D4,D5,D6;
	EditText editT1,editT2,editT3,editT4,editT5,editT6,editT7,editT8,editT9;
	int type=0;
	boolean visible=false;
	float edit1,edit2,edit3,edit4,edit5,edit6;
	LinearLayout Drawer,Container;
	int   mailCrt=1,nbrMaillons=1;
	
	
	void verifier(){
		if(editT1.getText().length()==0)
			editT1.setText("0");
		if(editT2.getText().length()==0)
			editT2.setText("0");
		if(editT3.getText().length()==0)
			editT3.setText("0");
		if(editT4.getText().length()==0)
			editT4.setText("0");
		if(editT5.getText().length()==0)
			editT5.setText("0");
		if(editT6.getText().length()==0)
			editT6.setText("0");
		if(editT7.getText().length()==0)
			editT7.setText("0");
		if(editT8.getText().length()==0)
			editT8.setText("-180");
		if(editT9.getText().length()==0)
			editT9.setText("180");
	}
	void RajouterTmp(){
		String Name="",tmp="";
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
      	  }else{
      		tmp+=line+"\n";
      	  }
      	  while((line = br.readLine()) != null){
      		 tmp+=line+"\n";
      	  }
      	  br.close();
      	  in.close();
        }
		}catch(Exception e){
		}
		try{
      	  File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
      	  FileWriter fw=new FileWriter(file,false);//write
      	  BufferedWriter bfw=new BufferedWriter(fw);
		  Name=String.format("//*%s\n", Name);
		  bfw.write(Name);
		  bfw.flush();
		  bfw.write(tmp);
		  bfw.flush();
		  bfw.close();
		}catch(Exception e){
			
		}

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maillon);	
		//definitions:
		Bok=(Button)findViewById(R.id.ok);
		editT1=(EditText)findViewById(R.id.editText1);
		editT2=(EditText)findViewById(R.id.editText2);
		editT3=(EditText)findViewById(R.id.editText3);
		editT4=(EditText)findViewById(R.id.editText4);
		editT5=(EditText)findViewById(R.id.editText5);
		editT6=(EditText)findViewById(R.id.editText6);
		editT7=(EditText)findViewById(R.id.editText7);
		editT8=(EditText)findViewById(R.id.editText8);
		editT9=(EditText)findViewById(R.id.editText9);
		Drawer=(LinearLayout)findViewById(R.id.drawerTitle);
		Container=(LinearLayout)findViewById(R.id.drawerContent);
		D=(Button)findViewById(R.id.drawerB);
		D1=(Button)findViewById(R.id.drawerB1);
		D2=(Button)findViewById(R.id.drawerB2);
		D3=(Button)findViewById(R.id.drawerB3);
		D4=(Button)findViewById(R.id.drawerB4);
		D5=(Button)findViewById(R.id.drawerB5);
		D6=(Button)findViewById(R.id.drawerB6);	
		//initialisation maillon parent :)
		// TODO rajouter quelque chose ici
		
				
		try{
	       FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
	       DataInputStream in = new DataInputStream(fstream);
	       BufferedReader br = new BufferedReader(new InputStreamReader(in));
	       String line="";
	       while ((line = br.readLine()) != null) {
	        	  line=line.toLowerCase(Locale.US);
	        	  if(line.contains("(")){
	        	  String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
	        	  String []vars=line2.split(",");
	        	  if(line.contains("ferm")){
	        		  
	        	  }else if(line.contains("liaison")){
	        		  if(vars.length==9){
	        			  
	        		//	  maillons[nbrMaillons]=new maillon(mailCrt,Integer.parseInt(vars[0]),Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]));
	        		//	  maillons[nbrMaillons].parametres[0].min=Float.valueOf(vars[1]);
	        		//	  maillons[nbrMaillons].parametres[0].max=Float.valueOf(vars[2]);
	        		//	  maillons[nbrMaillons].primitives=new primitive[50];
	        			 ++nbrMaillons;
	        			 mailCrt=nbrMaillons;//a verifier
	        			  
	        				
	        		  }
	        	  }}else if(line.contains("courant")){
	        		  mailCrt=Integer.parseInt(line.substring(line.indexOf("=")+1,line.indexOf(";")));//or -1 a vérifier?
	        		  
	        	  }
	          }
	          
	     
	          in.close();
	    }catch (Exception e){//Catch exception if any
	    	Toast.makeText(getApplicationContext(), "problem "+e.toString(), Toast.LENGTH_LONG).show();
	    }
	//	Toast.makeText(getApplicationContext(), String.format("maillon %d / %d",mailCrt,nbrMaillons), Toast.LENGTH_LONG).show();
		editT1.setText(String.format("%d", mailCrt));
		
		//initilaisation container
		Container.setVisibility(View.GONE);
		D.setText(R.string.xpivot);
		//Changer l'etat de visibilité du container:)
		Drawer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.VISIBLE);
				
			}
			
		});
		D.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.VISIBLE);
				
			}
			
		});
		D1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=0;
				D.setText(R.string.xpivot);
			}
			
		});
		D2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=1;
				D.setText(R.string.ypivot);
			}
			
		});
		D3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=2;
				D.setText(R.string.zpivot);
			}
			
		});
		D4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=3;
				D.setText(R.string.xslide);
			}
			
		});
		D5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=4;
				D.setText(R.string.yslide);
			}
			
		});
		D6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Container.setVisibility(View.GONE);
				type=5;
				D.setText(R.string.zslide);
			}
			
		});
		Bok.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//.getExternalStorageDirectory()
						
						File f=null;
						f=new File(Environment.getExternalStorageDirectory()+File.separator+"SMAR2");
						if(!f.exists() && !f.isDirectory()){
						f.mkdirs();
						
						}
						
						verifier();
	 					RajouterTmp();
			
					File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
					try{
					FileWriter fw=new FileWriter(file,true);//append
					BufferedWriter bfw=new BufferedWriter(fw);
					int ii=Integer.parseInt(editT1.getText().toString());
					String s1;
					if(ii!=mailCrt){
						s1=String.format(Locale.US,"Modelisation.MAILLON_COURANT=%d;\n",ii);
						bfw.write(s1);
						bfw.flush();
						
					}
					s1=String.format(Locale.US,"Modelisation.NouveauLiaison(%d,%s,%s,%s,%s,%s,%s,%s,%s);\n",type,editT8.getText(),editT9.getText(),editT2.getText(),editT3.getText(),editT4.getText(),editT5.getText(),editT6.getText(),editT7.getText());
					bfw.write(s1);
					bfw.flush();
					bfw.close();
					}catch(IOException ioe){
						Toast.makeText(getApplicationContext(), ioe.toString(), Toast.LENGTH_LONG).show();
						
						
					}
					Intent i1=new Intent("com.example.smar.GLExample");
					startActivity(i1);
						
					}
					
				});
	}
	

}
