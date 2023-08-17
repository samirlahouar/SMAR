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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class config extends Activity {
	TextView editR,editG,editB;
	SeekBar SR,SG,SB;
	SurfaceView surf;
	ViewGroup vg;
	Button Bok;
	EditText editT1,editT2,editT3,editT4;
	int r,g,b;
	void verifier(){
		
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
      	  }else if(!line.contains("setBackground")){
      		tmp+=line+"\n";
      	  }
      	  while((line = br.readLine()) != null){
      		if(!line.contains("setBackground")){
      		   tmp+=line+"\n";
      		}
      	  }
      	 }
        br.close();
    	  in.close();
      
		}catch(Exception e){
		}
		try{
      	  File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
      	  FileWriter fw=new FileWriter(file,false);//write
      	  BufferedWriter bfw=new BufferedWriter(fw);
		  Name=String.format(Locale.US,"//*%s\nModelisation.setBackground(%d,%d,%d);\n", Name,r,g,b);
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
		setContentView(R.layout.configuration);	
		//definitions:
		SR=(SeekBar)findViewById(R.id.seekBarR);
		SG=(SeekBar)findViewById(R.id.seekBarG);
		SB=(SeekBar)findViewById(R.id.seekBarB);
		editR=(TextView)findViewById(R.id.editR);
		editG=(TextView)findViewById(R.id.editG);
		editB=(TextView)findViewById(R.id.editB);
		surf=(SurfaceView)findViewById(R.id.surfaceView1);
		Bok=(Button)findViewById(R.id.ok);
		//chercher setBackground dans le fichier tmp 1ere ou deuxieme ligne pas la peine de chercher plus loin

		r=255;g=255;b=255;
		try{ 
			FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line="";
			if((line = br.readLine()) != null) {
				line=line.toLowerCase(Locale.US);
				line=line.replaceAll(" ", "");
				if(line.contains("(")){
					String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
					String []vars=line2.split(",");
					if(line.contains("background")){
						if(vars.length==3){
							r=Integer.parseInt(vars[0]);
							g=Integer.parseInt(vars[1]);
							b=Integer.parseInt(vars[2]);
						}
					}
				}else{
					if((line = br.readLine()) != null) {
						line=line.toLowerCase(Locale.US);
						line=line.replaceAll(" ", "");
						if(line.contains("(")){
							String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
							String []vars=line2.split(",");
							if(line.contains("background")){
								if(vars.length==3){
									r=Integer.parseInt(vars[0]);
									g=Integer.parseInt(vars[1]);
									b=Integer.parseInt(vars[2]);
								}
							}
						}
					}
				}
			}
			br.close();
			in.close();
       
		}catch(Exception e){
		
		}
		//changement d'axe
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
				
			
			//verifier les valeurs 
			//verifier();
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
