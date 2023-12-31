package smartmeca.smar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Locale;

import smartmeca.smar.R;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class modifCube extends Activity {
	RadioButton Rx,Ry,Rz;
	TextView edit1text,edit2text,edit3text,edit4text,editR,editG,editB;
	SeekBar SR,SG,SB;
	SurfaceView surf;
	ViewGroup vg;
	Button Bok;
	EditText editT1,editT2,editT3,editT4;
	int axe=0;
	int r,g,b;
	float edit1,edit2,edit3,edit4;
	int line;
	void verifier(){
		if(editT1.getText().length()==0)
			editT1.setText("0");
		if(editT2.getText().length()==0)
			editT2.setText("0");
		if(editT3.getText().length()==0)
			editT3.setText("0");
		if(editT4.getText().length()==0)
			editT4.setText("0");
		
	}
void modifierLigne(int ligne){
		
		try{
			FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
			  DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String line="";
	        String tmp="";
	        int i=0;
	        while((line = br.readLine()) != null){
	        	i++;
	        	if(i==ligne){
	        		tmp+=String.format(Locale.US,"Modelisation.NouveauCube(%d,%s,%s,%s,%s,%d,%d,%d);\n",axe,editT1.getText(),editT2.getText(),editT3.getText(),editT4.getText(),r,g,b);
	    				
	        	}else{
	      		 tmp+=line+"\n";
	        	}
	      	}
	      	  br.close();
	      	  in.close();
	      	  File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
	      	  FileWriter fw=new FileWriter(file,false);//write
	      	  BufferedWriter bfw=new BufferedWriter(fw);
			  bfw.write(tmp);
			  bfw.flush();
			  bfw.close();
			
			}catch(Exception e){
			}
			
	}
	
	
	void recuperer(int ligne){
		int i=0;
	
		try{
				FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line="",linem1="";
				while(i<ligne && (line = br.readLine()) != null) {
					i++;
				}	linem1=line;
				br.close();
				in.close();
				if(line==null){
					line=linem1;
				}
				line=line.toLowerCase(Locale.US);
	        	line=line.replaceAll(" ", "");
	        	if(line.contains("(")){
	        		String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
	        		String []vars=line2.split(",");
	        		if(line.contains("cube")){
	        			axe=Integer.parseInt(vars[0]);
	        			editT1.setText(vars[1]);
	        			editT2.setText(vars[2]);
	        			editT3.setText(vars[3]);
	        			editT4.setText(vars[4]);
	        			r=Integer.parseInt(vars[5]);
	        			g=Integer.parseInt(vars[6]);
	        			b=Integer.parseInt(vars[7]);
	        			
//	        				String s1=String.format(Locale.US,"Modelisation.NouveauCone(%d,%d,0,%s,%s,%s,%s,%s,%s,%d,%d,%d);\n",axe,type,editT1.getText(),editT2.getText(),editT3.getText(),editT4.getText(),editT5.getText(),editT6.getText(),r,g,b);
	    				
	        		}else{
	        			Intent i1=new Intent("com.example.smar.GLExample");
						startActivity(i1);
					
	        		}
	        	}	else{
	        		Intent i1=new Intent("com.example.smar.GLExample");
					startActivity(i1);
				
	        	}
	        }catch(Exception e){
	        	Intent i1=new Intent("com.example.smar.GLExample");
				startActivity(i1);
			
	        }
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
		setContentView(R.layout.cube);	
		//Changer le titre :)
				TextView tmpTV=(TextView)findViewById(R.id.cubetitle);
				tmpTV.setText("Change Cube");
				//R�cup�rer les parametres
				Bundle bb=getIntent().getExtras();
				line=bb.getInt("ligne");
				
		//definitions:
		Rx=(RadioButton)findViewById(R.id.axeX);
		Ry=(RadioButton)findViewById(R.id.axeY);
		Rz=(RadioButton)findViewById(R.id.axeZ);
		edit1text=(TextView)findViewById(R.id.textViewEdit1);
		edit2text=(TextView)findViewById(R.id.textViewEdit2);
		edit3text=(TextView)findViewById(R.id.textViewEdit3);
		edit4text=(TextView)findViewById(R.id.textViewEdit4);
		SR=(SeekBar)findViewById(R.id.seekBarR);
		SG=(SeekBar)findViewById(R.id.seekBarG);
		SB=(SeekBar)findViewById(R.id.seekBarB);
		editR=(TextView)findViewById(R.id.editR);
		editG=(TextView)findViewById(R.id.editG);
		editB=(TextView)findViewById(R.id.editB);
		surf=(SurfaceView)findViewById(R.id.surfaceView1);
		Bok=(Button)findViewById(R.id.ok);
		editT1=(EditText)findViewById(R.id.editText1);
		editT2=(EditText)findViewById(R.id.editText2);
		editT3=(EditText)findViewById(R.id.editText3);
		editT4=(EditText)findViewById(R.id.editText4);
		
		//changement d'axe
		recuperer(line);
		switch(axe){
			case 0:
				Rx.setChecked(true);
				Ry.setChecked(false);
				Rz.setChecked(false);
				edit1text.setText("Y");
				edit2text.setText("Z");
				edit3text.setText("X1");
				edit4text.setText("X2");
			break;
			case 1:
				Rx.setChecked(false);
				Ry.setChecked(true);
				Rz.setChecked(false);
				edit1text.setText("X");
				edit2text.setText("Z");
				edit3text.setText("Y1");
				edit4text.setText("Y2");
				break;
			case 2:
				Ry.setChecked(false);
				Rx.setChecked(false);
				edit1text.setText("X");
				edit2text.setText("Y");
				edit3text.setText("Z1");
				edit4text.setText("Z2");
				break;
		}
		
		Rx.setOnClickListener( new OnClickListener(){
				@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				axe=0;
				Ry.setChecked(false);
				Rz.setChecked(false);
				edit1text.setText("Y");
				edit2text.setText("Z");
				edit3text.setText("X1");
				edit4text.setText("X2");
				
			}
			
		});
		Ry.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				axe=1;
				Rx.setChecked(false);
				Rz.setChecked(false);
				edit1text.setText("X");
				edit2text.setText("Z");
				edit3text.setText("Y1");
				edit4text.setText("Y2");
			
			}
			
		});
		Rz.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				axe=2;
				Ry.setChecked(false);
				Rx.setChecked(false);
				edit1text.setText("X");
				edit2text.setText("Y");
				edit3text.setText("Z1");
				edit4text.setText("Z2");
			}
			
		});
		//def de seekbar Red min max?
	    editR.setText(String.format("%d", r));
		SR.setMax(255);
		SR.setProgress(r);
		//Changement de seekBar
		SR.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                r= progress;
                editR.setText(String.format("%d", r));
                surf.setBackgroundColor(Color.rgb(r, g, b));
                
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
             
            }
        });
		//def de seekbar Green min max?
		editG.setText(String.format("%d", g));
		SG.setMax(255);
		SG.setProgress(g);
		//Changement de seekBar
		SG.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                g= progress;
                editG.setText(String.format("%d", g));
                surf.setBackgroundColor(Color.rgb(r, g, b));
                
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
             
            }
        });
		//def de seekbar Blue min max?
		editB.setText(String.format("%d", b));
		SB.setMax(255);
		SB.setProgress(b);
		surf.setBackgroundColor(Color.rgb(r, g, b));
          
		//Changement de seekBar
		SB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                b= progress;
                editB.setText(String.format("%d", b));
                surf.setBackgroundColor(Color.rgb(r, g, b));
                
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
             
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
			
			//verifier les valeurs 
			verifier();
			modifierLigne(line);
			RajouterTmp();
			
			Intent i1=new Intent("com.example.smar.GLExample");
			startActivity(i1);
				
			}
			
		});
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
