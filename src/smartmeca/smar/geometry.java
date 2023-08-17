package smartmeca.smar;


import smartmeca.smar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class geometry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.primitives);
		
		
		Button bc=(Button) findViewById(R.id.addCube);
		bc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addCube");
				startActivity(i1);
				
			}
			
		});
		Button bpara=(Button) findViewById(R.id.addParallelipipede);
		bpara.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addPara");
				startActivity(i1);
				
			}
			
		});
		Button bprisme=(Button) findViewById(R.id.addPrisme);
		bprisme.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addPrisme");
				startActivity(i1);
				
			}
			
		});
		Button bcylindre=(Button) findViewById(R.id.addCylindre);
		bcylindre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addCylindre");
				startActivity(i1);
				
			}
			
		});
		Button bsphere=(Button) findViewById(R.id.addSphere);
		bsphere.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addSphere");
				startActivity(i1);
				
			}
			
		});	
		Button bcone=(Button) findViewById(R.id.addCone);
		bcone.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addCone");
				startActivity(i1);
				
			}
			
		});	
		Button bcoude=(Button) findViewById(R.id.addCoude);
		bcoude.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i1=new Intent("com.example.smar.addCoude");
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
