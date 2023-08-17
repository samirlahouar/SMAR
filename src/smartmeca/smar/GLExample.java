package smartmeca.smar;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import smartmeca.smar.R;


import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GLExample extends Activity {

	GLSurfaceView ourSurface;
	GLRendererEx gex;
	int xini,x,yini,y,xini1,x1,yini1,y1;
//	EditText t1;
	boolean animVisible,toolbarVisible;
	LinearLayout animBox,animBoxContent,toolbar,dialogBox;
	RelativeLayout mainL;
	public boolean onTouchEvent(MotionEvent event){
		if(event.getPointerCount()==1){
			x=(int)event.getX();
			y=(int)event.getY();
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				xini=x;
				yini=y;
			}else if(event.getAction()==MotionEvent.ACTION_UP){
				if(Math.abs(xini-x)+Math.abs(yini-y)<5){
					if(toolbarVisible){
						toolbarVisible=false;
						toolbar.setVisibility(View.GONE);
					}else{
						toolbarVisible=true;
						toolbar.setVisibility(View.VISIBLE);
							
					}
				}
			}
			gex.posx-=(xini-x)/20;
			gex.posy+=(yini-y)/20;
			
		
		}else if(event.getPointerCount()==2){
			x=(int)event.getX(0);
			y=(int)event.getY(0);
			x1=(int)event.getX(1);
			y1=(int)event.getY(1);
			float tmp=(x-x1)*(x-x1)*(y-y1)*(y-y1)/100000000;
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				xini=x;
				yini=y;
				xini1=x1;
				yini1=y1;
				gex.rectI=0;
			}
				if(gex.rectI==0)
					gex.rectI=tmp;
				if(tmp>0)
				gex.zoom*=gex.rectI/tmp;
	//		t1.setText(String.format("%f", gex.zoom));
			if(gex.zoom>1000)
					gex.zoom=1000;
			if(gex.zoom<0.1f)
				gex.zoom=0.1f;
			gex.rectI=tmp;
			
		}
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//  Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page2);
		mainL=(RelativeLayout)findViewById(R.id.mainLayout);
	//	t1=(EditText) findViewById(R.id.editText1);
		ourSurface =(GLSurfaceView) findViewById(R.id.surfaceView1);//new GLSurfaceView(this);// 
		gex=new GLRendererEx(getApplicationContext(),this);
		ourSurface.setRenderer(gex);
		//setContentView(ourSurface);
		//Cacher la toolbar
		toolbarVisible=false;
		toolbar=(LinearLayout) findViewById(R.id.toolBar);
		toolbar.setVisibility(View.GONE);
		
		//Definir la dialogBox
		dialogBox=(LinearLayout) findViewById(R.id.dialogBox);
		
		
		Button b1=(Button) findViewById(R.id.addGeometry);
		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//  Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "B1 clicked!!", Toast.LENGTH_LONG).show();
				//setContentView(R.layout.primitives);
				Intent i1=new Intent("com.example.smar.geometry");
				startActivity(i1);
				
				
			}
			
		});
		//Initialisation de la fenetre d'animation.
		animBox=(LinearLayout)findViewById(R.id.animationBox);
		animBox.setVisibility(View.GONE);
		animBoxContent=(LinearLayout)findViewById(R.id.LinearAnimation);
		for(int i=1;i<gex.sm.nbrMaillons;i++){
			SeekBar seek=new SeekBar(this);
			seek.setMax((int)(gex.sm.maillons[i].parametres[0].max-gex.sm.maillons[i].parametres[0].min));
			seek.setId(i);
			//seek.
			seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				int savedVal;
	            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
	            	//Toast.makeText(getApplicationContext(), String.format("id : %d",seekBar.getId()), Toast.LENGTH_LONG).show();
	            	gex.sm.maillons[seekBar.getId()].parametres[0].val=(float) progress+gex.sm.maillons[seekBar.getId()].parametres[0].min;
	            //	t1.setText(String.format("maillon %d -> %d",seekBar.getId(),progress));
	            	if(Math.abs(savedVal-progress)>5)gex.sm.calc();
	                
	            }
	 
	            public void onStartTrackingTouch(SeekBar seekBar) {
	                // Auto-generated method stub
	            	savedVal=(int)(gex.sm.maillons[seekBar.getId()].parametres[0].val-gex.sm.maillons[seekBar.getId()].parametres[0].min);
	            	
	            }
	 
	            public void onStopTrackingTouch(SeekBar seekBar) {
	            	gex.sm.calc();
	            }
	        });
			animBoxContent.addView(seek);
			
		};
		
		//Montrer et Cacher la fenetre d'animation Box
		animVisible=false;
		Button b2=(Button) findViewById(R.id.animation);
		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "B1 clicked!!", Toast.LENGTH_LONG).show();
				//setContentView(R.layout.primitives);
				if(animVisible){
					//animBox//.setVisibility(View.INVISIBLE);
					animBox.setVisibility(View.INVISIBLE);
					animVisible=false;
					mainL.invalidate();
				}else{
					
					animBox.setVisibility(View.VISIBLE);
					animVisible=true;
					onResume();
					mainL.invalidate();
				}
			}
			
		});
		
		
		//Nouveau fichier SMAR
		Button b3=(Button)findViewById(R.id.newSMR);
		b3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Auto-generated method stub);
				
				
				
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
		        	  in.close();
		        	  br.close();
		        	  if(line.contains("//*")){
		        		  //Demander si l'utilisateur veut enregistrer le fichier
		        		  dialogBox.setVisibility(View.VISIBLE);
		        		  onResume();
		        		  mainL.invalidate();
		        		  Button yes=(Button)findViewById(R.id.yes);
		        		  Button no=(Button)findViewById(R.id.no);
		        		  yes.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								// Auto-generated method stub
								Intent i1=new Intent("com.example.smar.save");
								startActivity(i1);
							}
		        			  
		        		  });
		        		  no.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View v) {
									// Auto-generated method stub
									File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
						  			//
									if(!file.delete())
										Toast.makeText(getApplicationContext(), "Problem deleting", Toast.LENGTH_LONG).show();
										
									Intent i1=new Intent("com.example.smar.GLExample");
									startActivity(i1);
								}
			        			  
			        	 });
		        	  }else{
		        		//supprimer le fichier tmp
		  				
				        	File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
				  			//
							if(!file.delete())
								Toast.makeText(getApplicationContext(), "Problem deleting", Toast.LENGTH_LONG).show();
								
							Intent i1=new Intent("com.example.smar.GLExample");
							startActivity(i1);
		        	  }
		        	  
		  	    	} 
		          in.close();
				}catch (Exception e){//Catch exception if any
		  	    	//
		  	    }
		          
				
			  }
			});
		
		//OnSAVE
		Button b7=(Button) findViewById(R.id.Save);
		b7.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.save");
				startActivity(i1);
				
			}
		});
		
		//rajouter un nouveau maillon newMaillon
		Button b4=(Button) findViewById(R.id.newMaillon);
		b4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addMaillon");
				startActivity(i1);
				
			}
		});
		
		//Montrer le tree du Smar
				Button b5=(Button) findViewById(R.id.Tree);
				b5.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						Intent i1=new Intent("com.example.smar.showTree");
						startActivity(i1);
						
					}
				});
				//Ouvrir les fichiers
				Button b6=(Button) findViewById(R.id.Open);
				b6.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//verifier enregistrer??
						try{
							  FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
							  DataInputStream in = new DataInputStream(fstream);
					          BufferedReader br = new BufferedReader(new InputStreamReader(in));
					          String line="";
					          if((line = br.readLine()) != null) {
					        	  in.close();
					        	  br.close();
					        	  if(line.contains("//*")){
					        		  //Demander si l'utilisateur veut enregistrer le fichier
					        		  dialogBox.setVisibility(View.VISIBLE);
					        		  onResume();
					        		  mainL.invalidate();
					    				
										 
					        		  Button yes=(Button)findViewById(R.id.yes);
					        		  Button no=(Button)findViewById(R.id.no);
					        		  yes.setOnClickListener(new OnClickListener(){

										@Override
										public void onClick(View v) {
											// Auto-generated method stub
											Intent i1=new Intent("com.example.smar.save");
											startActivity(i1);
										}
					        			  
					        		  });
					        		  no.setOnClickListener(new OnClickListener(){

											@Override
											public void onClick(View v) {
												// Auto-generated method stub
												Intent i1=new Intent("com.example.smar.open");
												startActivity(i1);
											}
						        			  
						        		  });

					        	  }else {
					        		  Intent i1=new Intent("com.example.smar.open");
										startActivity(i1);
										
					        	  }
					          }
						}catch(Exception e){
							Intent i1=new Intent("com.example.smar.open");
							startActivity(i1);
							
						}
						
					}
				});
				
				Button b8=(Button) findViewById(R.id.last);
				b8.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//
						toolbar.scrollBy(200, 0);
						
					}
				});
				Button b9=(Button) findViewById(R.id.next);
				b9.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//
						toolbar.scrollBy(-200, 0);
						
					}
				});
				Button b10=(Button) findViewById(R.id.parameters);
				b10.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//
						Intent i1=new Intent("com.example.smar.config");
						startActivity(i1);
												
					}
				});

		/*.setOnDragListener(new OnDragListener(){

					@Override
					public boolean onDrag(View arg0, DragEvent arg1) {
						// Auto-generated method stub
						arg0.scrollBy((int)arg1.getX(), (int)arg1.getY());
						return false;
						
					}
					
				});*/
			
	}

	@Override
	protected void onPause() {
		// Auto-generated method stub
		super.onPause();
		ourSurface.onPause();
		//finish();
	}

	@Override
	protected void onResume() {
		// Auto-generated method stub
		super.onResume();
		ourSurface.onResume();
	}
	

}
