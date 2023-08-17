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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class modifPrisme extends Activity{

	RadioButton Rx,Ry,Rz;
	TextView edit1text,edit2text,edit3text,edit4text,edit5text,edit6text,edit7text,editR,editG,editB,actualpoint;
	SeekBar SR,SG,SB;
	SurfaceView surf;
	ViewGroup vg;
	Button Bok,previous,next;
	EditText editT1,editT2,editT3,editT4,editT5,editT6,editT7;
	int axe=0;
	int r,g,b;
	float edit1,edit2,edit3,edit4,edit5,edit6;
    float []points;
    int pointsNbr,actualPoint;
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
		if(editT5.getText().length()==0)
			editT5.setText("0");
		if(editT6.getText().length()==0)
			editT6.setText("0");
		if(editT7.getText().length()==0)
			editT7.setText("0");
		
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
	        		String tpts="";
					for (int j=0;j<2*pointsNbr;j++)
						tpts=String.format(Locale.US,"%s%f,", tpts,points[j]);
	        		tmp+=String.format(Locale.US,"Modelisation.NouveauPrisme(%d,%s,%s,%s,%s,%s,%s%d,%d,%d);\n",axe,editT1.getText(),editT2.getText(),editT3.getText(),editT4.getText(),editT5.getText(),tpts,r,g,b);
					
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
	        		if(line.contains("prisme")){
	        			axe=Integer.parseInt(vars[0]);
	        			editT1.setText(vars[1]);
	        			editT2.setText(vars[2]);
	        			editT3.setText(vars[3]);
	        			editT4.setText(vars[4]);
	        			editT5.setText(vars[5]);
	        			pointsNbr=Integer.parseInt(vars[2]);
	        			int k=0;
	        			if(pointsNbr>0){
	        				actualPoint=0;
	        				actualpoint.setText(String.format("%d/%d",actualPoint+1,pointsNbr));
							
	        				editT6.setText(vars[6]);
	        				editT7.setText(vars[7]);
	        				points=new float[pointsNbr*2];
							
		        			for (int j=0;j<pointsNbr;j++){
		        				points[k]=Float.parseFloat(vars[k+6]);k++;
		        				points[k]=Float.parseFloat(vars[k+6]);k++;

		        			}
//							tpts=String.format(Locale.US,"%s%f,", tpts,);
	        			}
	        			//tmp+=String.format(Locale.US,"Modelisation.NouveauPrisme(%d,%s,%s,%s,%s,%s,%s%d,%d,%d);\n",axe,editT1.getText(),editT2.getText(),editT3.getText(),editT4.getText(),editT5.getText(),tpts,r,g,b);

	        			r=Integer.parseInt(vars[k+6]);k++;
	        			g=Integer.parseInt(vars[k+6]);k++;
	        			b=Integer.parseInt(vars[k+6]);k++;
	        			
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
	        	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		        
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
		setContentView(R.layout.prisme);
		//Changer le titre :)
		TextView tmpTV=(TextView)findViewById(R.id.prismetitle);
		tmpTV.setText("Change Prisme");
		//Récupérer les parametres
		Bundle bb=getIntent().getExtras();
		line=bb.getInt("ligne");

		
		pointsNbr=0; 
		actualPoint=0;
		Rx=(RadioButton)findViewById(R.id.axeX);
		Ry=(RadioButton)findViewById(R.id.axeY);
		Rz=(RadioButton)findViewById(R.id.axeZ);
		edit1text=(TextView)findViewById(R.id.textViewEdit1);
		edit2text=(TextView)findViewById(R.id.textViewEdit2);
		edit3text=(TextView)findViewById(R.id.textViewEdit3);
		edit4text=(TextView)findViewById(R.id.textViewEdit4);
		edit5text=(TextView)findViewById(R.id.textViewEdit5);
		edit6text=(TextView)findViewById(R.id.textViewEdit6);
		edit7text=(TextView)findViewById(R.id.textViewEdit7);
		actualpoint=(TextView)findViewById(R.id.actualpoint);
		
		SR=(SeekBar)findViewById(R.id.seekBarR);
		SG=(SeekBar)findViewById(R.id.seekBarG);
		SB=(SeekBar)findViewById(R.id.seekBarB);
		editR=(TextView)findViewById(R.id.editR);
		editG=(TextView)findViewById(R.id.editG);
		editB=(TextView)findViewById(R.id.editB);
		surf=(SurfaceView)findViewById(R.id.surfaceView1);
		Bok=(Button)findViewById(R.id.ok);
		previous=(Button)findViewById(R.id.previous);
		next=(Button)findViewById(R.id.next);
		editT1=(EditText)findViewById(R.id.editText1);
		editT2=(EditText)findViewById(R.id.editText2);
		editT3=(EditText)findViewById(R.id.editText3);
		editT4=(EditText)findViewById(R.id.editText4);
		editT5=(EditText)findViewById(R.id.editText5);
		editT6=(EditText)findViewById(R.id.editText6);
		editT7=(EditText)findViewById(R.id.editText7);
		
		recuperer(line);
		switch(axe){
			case 0:
				Rx.setChecked(true);
				Ry.setChecked(false);
				Rz.setChecked(false);
				
				edit1text.setText("X");
				edit6text.setText("Y");
				edit7text.setText("Z");
				break;
			case 1:
				Rx.setChecked(false);
				Ry.setChecked(true);
				Rz.setChecked(false);
				edit1text.setText("Y");
				edit6text.setText("X");
				edit7text.setText("Z");
				
				break;
			case 2: 

				Rz.setChecked(true);
				Ry.setChecked(false);
				Rx.setChecked(false);
				edit1text.setText("Z");
				edit6text.setText("X");
				edit7text.setText("Y");
				break;
		}
		//changement d'axe
				
				Rx.setOnClickListener( new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Ry.setChecked(false);
						Rz.setChecked(false);
						edit1text.setText("X");
						edit6text.setText("Y");
						edit7text.setText("Z");
						axe=0;		
					}
					
				});
				Ry.setOnClickListener( new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Rx.setChecked(false);
						Rz.setChecked(false);
						edit1text.setText("Y");
						edit6text.setText("X");
						edit7text.setText("Z");
						axe=1;						
					}
					
				});
				Rz.setOnClickListener( new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Ry.setChecked(false);
						Rx.setChecked(false);
						edit1text.setText("Z");
						edit6text.setText("X");
						edit7text.setText("Y");
						axe=2;
					}
					
				});
				//def de seekbar Red min max?
						SR.setMax(255);
						SR.setProgress(r);
						editR.setText(String.format("%d", r));
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
						SG.setMax(255);
						SG.setProgress(g);
						editG.setText(String.format("%d", g));
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
						SB.setMax(255);
						SB.setProgress(b);
						editB.setText(String.format("%d", b));
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
							
								verifier();
								modifierLigne(line);
			 					RajouterTmp();
								
							
							Intent i1=new Intent("com.example.smar.GLExample");
							startActivity(i1);
								
							}
							
						});
						
						editT2.addTextChangedListener(new TextWatcher(){
							@Override
							public void afterTextChanged(Editable s) {
								int tmpnbr=pointsNbr;
								float tmppoints[]={};
								if(pointsNbr>0){
									tmppoints=new float[pointsNbr*2];
									for(int i=0;i<pointsNbr;i++){
										tmppoints[i]=points[i];
									}
								}		
								pointsNbr=Integer.parseInt(s.toString());
								points=new float[pointsNbr*2];
								if(pointsNbr<tmpnbr){
									tmpnbr=pointsNbr;
									if(actualPoint>pointsNbr){
										actualPoint=pointsNbr;
										editT6.setText(String.format(Locale.US,"%3f",tmppoints[actualPoint*2]));
										editT7.setText(String.format(Locale.US,"%3f",tmppoints[actualPoint*2+1]));
									}
								}
								for(int i=0;i<tmpnbr;i++){
									points[i]=tmppoints[i];
								}
								actualpoint.setText(String.format("%d/%d",actualPoint+1,pointsNbr));
							}
					        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
					        public void onTextChanged(CharSequence s, int start, int before, int count){}
							
					    }); 		
			//	previous
			previous.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
				 if(actualPoint>0){
					 actualPoint--;
					 actualpoint.setText(String.format("%d/%d",actualPoint+1,pointsNbr));
					 editT6.setText(String.format(Locale.US,"%3f",points[actualPoint*2]));
					 editT7.setText(String.format(Locale.US,"%3f",points[actualPoint*2+1]));
					 
				 }
				}
			});
			//next
			next.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
				 if(actualPoint<pointsNbr-1){
					 actualPoint++;
					 actualpoint.setText(String.format("%d/%d",actualPoint+1,pointsNbr));
					 editT6.setText(String.format(Locale.US,"%3f",points[actualPoint*2]));
					 editT7.setText(String.format(Locale.US,"%3f",points[actualPoint*2+1]));
					 
				 }
				}
			});
			//Changement des valeurs des points
			editT6.addTextChangedListener(new TextWatcher(){
				@Override
				public void afterTextChanged(Editable s) {
					try{
					points[actualPoint*2]=Float.parseFloat(s.toString());
					}catch(Exception e){
					//	points[actualPoint*2]=0;
				//		 Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
					        
					}
				}
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		        public void onTextChanged(CharSequence s, int start, int before, int count){}
				
		    });
			editT7.addTextChangedListener(new TextWatcher(){
				@Override
				public void afterTextChanged(Editable s) {
					try{
					points[actualPoint*2+1]=Float.parseFloat(s.toString());
					}catch(Exception e){
				//		 Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				        	
						//points[actualPoint*2]=0;
					}
				}
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		        public void onTextChanged(CharSequence s, int start, int before, int count){}
				
		    });
			
	}
	
}
