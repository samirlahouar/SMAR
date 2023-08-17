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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class showTree extends Activity {
	Button Bok;
	
	LinearLayout mainLay;
	int   mailCrt=1,nbrMaillons=1;
	public class niveauButton extends Button{

		public void rearrange(){
			final float SCALE = getContext().getResources().getDisplayMetrics().density;

			// Convert dips to pixels
			float valueDips = 37.0f;
			int valuePixels = (int)(valueDips * SCALE + 0.5f); // 0.5f for rounding
			LinearLayout.LayoutParams params=(android.widget.LinearLayout.LayoutParams) this.getLayoutParams();
			params.height=valuePixels;
			params.width=valuePixels;
			this.setLayoutParams(params);
			
		}
		public niveauButton(Context context) {
			super(context);
			setBackgroundResource(R.drawable.niveau);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				
				}
				
			});
		}
		
	}
	public class eraseButton extends Button{

		int lNbr;
		String tp;
		public void rearrange(){
			final float SCALE = getContext().getResources().getDisplayMetrics().density;

			// Convert dips to pixels
			float valueDips = 30.0f;
			int valuePixels = (int)(valueDips * SCALE + 0.5f); // 0.5f for rounding
			LinearLayout.LayoutParams params=(android.widget.LinearLayout.LayoutParams) this.getLayoutParams();
			params.height=valuePixels;
			params.width=valuePixels;
			this.setLayoutParams(params);
			
		}
		public eraseButton(Context context,int lineNbr) {
			super(context);
			lNbr=lineNbr;
			setBackgroundResource(R.drawable.supprimer);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
						DataInputStream in = new DataInputStream(fstream);
				        BufferedReader br = new BufferedReader(new InputStreamReader(in));
				        String line="";
				        String tmp="";
				        int ii=0;
				        while((line = br.readLine()) != null){
				        	ii++;
				        	 if(ii!=lNbr)
				        		 tmp+=line+"\n";
				      	}
				      	br.close();
				      	in.close();
				      	 File file=new File(Environment.getExternalStorageDirectory(),"/SMAR2/tmp.smr");
				      	  FileWriter fw=new FileWriter(file,false);//write
				      	  BufferedWriter bfw=new BufferedWriter(fw);
						  bfw.write(tmp);
						  bfw.flush();
						  bfw.close();  
						  Intent i1=new Intent("com.example.smar.GLExample");
						  startActivity(i1);
						
					}catch(Exception e){
						//there is something wrong
					}

				}
				
			});
		}
		
	}
	public class modifButton extends Button{

		int lNbr;
		String tp;
		public void rearrange(){
			final float SCALE = getContext().getResources().getDisplayMetrics().density;

			// Convert dips to pixels
			float valueDips = 30.0f;
			int valuePixels = (int)(valueDips * SCALE + 0.5f); // 0.5f for rounding
			LinearLayout.LayoutParams params=(android.widget.LinearLayout.LayoutParams) this.getLayoutParams();
			params.height=valuePixels;
			params.width=valuePixels;
			this.setLayoutParams(params);
			
		}
		public modifButton(Context context,int lineNbr,String type) {
			super(context);
			lNbr=lineNbr;
			tp=type;
			setBackgroundResource(R.drawable.modifier);
			
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(tp.contentEquals("cylindre")){
						Intent i1=new Intent("com.example.smar.modifCylindre");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("maillon")){
						Intent i1=new Intent("com.example.smar.modifMaillon");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("cone")){
						Intent i1=new Intent("com.example.smar.modifCone");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("coude")){
						Intent i1=new Intent("com.example.smar.modifCoude");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("cube")){
						Intent i1=new Intent("com.example.smar.modifCube");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("parallelepipede")){
						Intent i1=new Intent("com.example.smar.modifPara");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("prisme")){
						Intent i1=new Intent("com.example.smar.modifPrisme");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}else if(tp.contentEquals("sphere")){
						Intent i1=new Intent("com.example.smar.modifSphere");
						Bundle b = new Bundle();
						b.putInt("ligne", lNbr); //Your id
						i1.putExtras(b); //Put your id to your next Intent
						startActivity(i1);
						
					}
				}
				
			});
		}
		
	}
	public class maillonButton extends Button{
		boolean visible=false;
		LinearLayout LG;
		public maillonButton(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			LG=new LinearLayout(context);
			LG.setVisibility(View.GONE);
			LG.setOrientation(LinearLayout.VERTICAL);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(visible){
						LG.setVisibility(View.GONE);
						visible=false;
					}else{
						visible=true;
						LG.setVisibility(View.VISIBLE);
					}
				}
				
			});

		}
		@Override
		public void setOnClickListener(OnClickListener l) {
			// TODO Auto-generated method stub
			super.setOnClickListener(l);
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
		
		maillonButton B1=new maillonButton(this);
		B1.setText("maillon 1");
		
	    int lnbr=0;
		LinearLayout ll1=new LinearLayout(this);
		ll1.addView(B1);
		String Stmp="maillon";
	    modifButton m1=new modifButton(this,lnbr,Stmp);
	    ll1.addView(m1);
	    m1.rearrange();
	    eraseButton e1=new eraseButton(this,lnbr);
	    ll1.addView(e1);
	    e1.rearrange();

		
		
		mainLay.addView(ll1);
		mainLay.addView(B1.LG);	
	//	B1.		//initialisation maillon parent :)
		// TODO rajouter quelque chose ici
		
				
		try{
	       FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
	       DataInputStream in = new DataInputStream(fstream);
	       BufferedReader br = new BufferedReader(new InputStreamReader(in));
	       String line="";
	   
	       while ((line = br.readLine()) != null) {
	    	   	  lnbr++;
	        	  line=line.toLowerCase(Locale.US);
	        	  if(line.contains("(")){
	        	  String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
	        	  String []vars=line2.split(",");
	        	  if(line.contains("cube")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("cube"); 
	        		    ll1.addView(BB);
	        		    Stmp="cube";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        			
	        	  }else if(line.contains("cylindre")){
	        		    ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("cylinder"); 
	        		    ll1.addView(BB);
	        		    Stmp="cylindre";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        			
	        	  }else if(line.contains("sphere")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("sphere"); 
	        		    ll1.addView(BB);
	        		    Stmp="sphere";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        				
	        	  }else if(line.contains("parallelepipede")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("parallelepipede"); 
	        		    ll1.addView(BB);
	        		    Stmp="parallelepipede";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        			
	        	  }else if(line.contains("coude")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("coude"); 
	        		    ll1.addView(BB);
	        		    Stmp="coude";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        	  }else if(line.contains("prisme")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("prisme"); 
	        		    ll1.addView(BB);
	        		    Stmp="prisme";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);
	        	  }else if(line.contains("cone")){
	        		  ll1=new LinearLayout(this);
	        		    niveauButton n1=new niveauButton(this);
	        		    ll1.addView(n1);
	        		    n1.rearrange();
	        		    Button BB=new Button(this);	        			
	        		    BB.setText("cone"); 
	        		    ll1.addView(BB);
	        		    Stmp="cone";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();
	        		    B1.LG.addView(ll1);	
	        	  }else if(line.contains("ferm")){
	        		  
	        	  }else if(line.contains("liaison")){
	        		  if(vars.length==9){
	        			  
	        			 ++nbrMaillons;
	        			 mailCrt=nbrMaillons;//a verifier
	        			 B1=new maillonButton(this);
	        			B1.setText(String.format("maillon %d", nbrMaillons)); 
	        			
	        			
	        			
	        			ll1=new LinearLayout(this);
	        			ll1.addView(B1);
	        			Stmp="maillon";
	        		    m1=new modifButton(this,lnbr,Stmp);
	        		    ll1.addView(m1);
	        		    m1.rearrange();
	        		    e1=new eraseButton(this,lnbr);
	        		    ll1.addView(e1);
	        		    e1.rearrange();

	        			
	        			
	        			mainLay.addView(ll1);
	        			mainLay.addView(B1.LG);	
	        		  }
	        	  }}else if(line.contains("courant")){
	        		  mailCrt=Integer.parseInt(line.substring(line.indexOf("=")+1,line.indexOf(";")));//or -1 a vérifier?
	        		  
	        	  }
	          }
	          
	     
	          in.close();
	    }catch (Exception e){//Catch exception if any
	    	Toast.makeText(getApplicationContext(), "problem "+e.toString(), Toast.LENGTH_LONG).show();
	    }
		}
	

}
