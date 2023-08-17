package smartmeca.smar;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

public class smar {
	float lx1=1000,lx2=-1000,ly1=1000,ly2=-1000,lz1=1000,lz2=-1000,dd=0;
	Context csmar,csmara;
	boolean calcencours=false;
	public class maillon{
		// un maillon contient des primitives un parent une matrice position/orientation , une matrice de la liaison
		int parent;
		int liaison;
		primitive primitives[];
		int nbprim;
		matriceH mp,ml,mt;
		parametre parametres[];
		public maillon(int par,int li,float posx,float posy,float posz,float rx,float ry,float rz){
			nbprim=0;
			mp=new matriceH();
			matriceH mtmp=new matriceH();
			mp.rotX(rx);
			mtmp=new matriceH();
			mtmp.rotY(ry);
			mp=mp.mult(mtmp);
			mtmp=new matriceH();
			mtmp.rotZ(rz);
			mp=mp.mult(mtmp);
			mp.tran(posx, posy, posz);
			ml=new matriceH();
			
			parent =par;
			liaison=li;
			switch(li){
				case 0://0-liaison rotoide selonX
					parametres=new parametre[1];
					parametres[0]=new parametre(0,360,0);
					break;
				case 1://0-liaison rotoide selonY
					parametres=new parametre[1];
					parametres[0]=new parametre(0,360,0);
					break;

				case 2://0-liaison rotoide selonZ
					parametres=new parametre[1];
					parametres[0]=new parametre(0,360,0);
					break;
				case 3://0-liaison glissiere selonX
					parametres=new parametre[1];
					parametres[0]=new parametre(0,100,0);
					break;
				case 4://0-liaison glissiere selonY
					parametres=new parametre[1];
					parametres[0]=new parametre(0,100,0);
					break;
				case 5://0-liaison glissiere selonZ
					parametres=new parametre[1];
					parametres[0]=new parametre(0,100,0);
					break;	

			}
			calc();
		}
		void calc(){
			matriceH mpa=new matriceH();
			if(parent!=-1){
				mpa=maillons[parent].mt;
			}
			switch(liaison){
			case 0:
				ml.rotX(parametres[0].val);
				break;
			case 1:
				ml.rotY(parametres[0].val);
				break;
			case 2:
				ml.rotZ(parametres[0].val);
				break;
			case 3:
				ml.tran(parametres[0].val,0,0);
				break;
			case 4:
				ml.tran(0,parametres[0].val,0);
				break;
			case 5:
				ml.tran(0,0,parametres[0].val);
				break;
			}
			matriceH res=mpa.mult(mp);
			mt=res.mult(ml);
			if(nbprim>0){
				for(int i=0;i<nbprim;i++)
					primitives[i].calculer(mt);
			}
		}
		
	}
	public class parametre{
		float min,max,val;
		public parametre(float m1,float m2,float v){
			min=m1; max=m2; val=v;
		}
	}
	public class matriceH{
		float[] values;
		public matriceH(){
			values=new float[]{1,0,0,0,
								0,1,0,0,
								0,0,1,0,
								0,0,0,1};
			
		}
		void rotX(float x){
			values[5]=(float)Math.cos(x*Math.PI/180);values[6]=-(float)Math.sin(x*Math.PI/180);
			values[9]=(float)Math.sin(x*Math.PI/180);values[10]=(float)Math.cos(x*Math.PI/180);
		}
		void rotY(float y){
			values[0]=(float)Math.cos(y*Math.PI/180);values[2]=(float)Math.sin(y*Math.PI/180);
			values[8]=-(float)Math.sin(y*Math.PI/180);values[10]=(float)Math.cos(y*Math.PI/180);
		}
		void rotZ(float z){
			values[0]=(float)Math.cos(z*Math.PI/180);values[1]=-(float)Math.sin(z*Math.PI/180);
			values[4]=(float)Math.sin(z*Math.PI/180);values[5]=(float)Math.cos(z*Math.PI/180);
		}
		void tran(float x, float y, float z){
			values[3]=x;values[7]=y;values[11]=z;
		}
		matriceH mult(matriceH m2){
			matriceH mt=new matriceH();
			mt.values[0]=values[0]*m2.values[0]+values[1]*m2.values[4]+values[2]*m2.values[8];
			mt.values[1]=values[0]*m2.values[1]+values[1]*m2.values[5]+values[2]*m2.values[9];
			mt.values[2]=values[0]*m2.values[2]+values[1]*m2.values[6]+values[2]*m2.values[10];
			mt.values[3]=values[0]*m2.values[3]+values[1]*m2.values[7]+values[2]*m2.values[11]+values[3];
			mt.values[4]=values[4]*m2.values[0]+values[5]*m2.values[4]+values[6]*m2.values[8];
			mt.values[5]=values[4]*m2.values[1]+values[5]*m2.values[5]+values[6]*m2.values[9];
			mt.values[6]=values[4]*m2.values[2]+values[5]*m2.values[6]+values[6]*m2.values[10];
			mt.values[7]=values[4]*m2.values[3]+values[5]*m2.values[7]+values[6]*m2.values[11]+values[7];
			mt.values[8]=values[8]*m2.values[0]+values[9]*m2.values[4]+values[10]*m2.values[8];
			mt.values[9]=values[8]*m2.values[1]+values[9]*m2.values[5]+values[10]*m2.values[9];
			mt.values[10]=values[8]*m2.values[2]+values[9]*m2.values[6]+values[10]*m2.values[10];
			mt.values[11]=values[8]*m2.values[3]+values[9]*m2.values[7]+values[10]*m2.values[11]+values[11];
			
			return mt;
		}
		float[] mult(float[] v){
			float[] tmp=new float[v.length];
			int i=0;
			while(i+2<v.length){
				tmp[i]=values[0]*v[i]+values[1]*v[i+1]+values[2]*v[i+2]+values[3];
				tmp[i+1]=values[4]*v[i]+values[5]*v[i+1]+values[6]*v[i+2]+values[7];
				tmp[i+2]=values[8]*v[i]+values[9]*v[i+1]+values[10]*v[i+2]+values[11];
				i+=3;
				
			}
			return tmp;
		}
	}
	public class stl extends primitive{
		public stl(String fname,float r,float g,float b){
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			//detecter si le fichier est binaire ou ascii
			
			//ouvrir le fichier
			vertices=new float[100000];
			pIndex= new short[100000];
/*			File f=new File("/");
			File[] files=f.listFiles();
			String s=new String("");
			for(int i=0;i<files.length;i++)
				s=String.format("%s %s",s, files[i].getName());
			Toast.makeText(csmar,String.format("%s hello %d files : %s", fname,files.length,s),Toast.LENGTH_LONG).show();*/
			//android progress bar??
			//tester sur un fichier il faut arriver a ouvrir un fichier stl et voir comment ca fonctionne
			// Il y a deux méthode la plus simple mais moins performante consiste a prendre tous les points pour faire les triangle
			//L'autre méthode consiste a trouver les points redandants et les éliminer ca prend plus de temps mais ca charge moins
			// La ram... avoir!
			float x,y,z,nx,ny,nz;
	//		String filename=String.format("sdcard/smar/billes.stl");
			try{
			LineNumberReader reader = new LineNumberReader(new FileReader("/sdcard/smar/p3.stl"));
		    String line;
		    int i=0,j=0,k=0;
//		    ArrayList<float> vertexs=new ArrayList<float>(); 
		    if((line = reader.readLine()) != null){
		    	if(line.contains("solid")){
		    		
		    	
		    
		    while ((line = reader.readLine()) != null) {
		    	if(line.contains("normal")){
		    		line=line.substring(line.indexOf("normal")+7);
		    		String[] vals=line.split(" ");
		    		if(vals.length!=3){
		    			Toast.makeText(csmar,String.format("normale error %d", vals.length),Toast.LENGTH_LONG).show();
		    		}else{
		    			nx=Float.parseFloat(vals[0]);
		    			ny=Float.parseFloat(vals[1]);
		    			nz=Float.parseFloat(vals[2]);		    			
		    		}
		    	}
		    	if(line.contains("vertex")){
		    		line=line.substring(line.indexOf("vertex")+7);
		    		String[] vals=line.split(" ");
		    		if(vals.length!=3){
		    			Toast.makeText(csmar,String.format("vertex error %d",vals.length),Toast.LENGTH_LONG).show();
		    			return;
		    		}else{
		    			x=Float.parseFloat(vals[0]);y=Float.parseFloat(vals[1]);z=Float.parseFloat(vals[2]);
		    			vertices[j]=x;j++;
		    			vertices[j]=y;j++;
		    			vertices[j]=z;j++;
		    			if(x<x1)x1=x;if(x>x2)x2=x;
		    			if(y<y1)y1=y;if(y>y2)y2=y;
		    			if(z<z1)z1=z;if(z>z2)z2=z;
		    			i++;
		    		}
		    	}
		    	if(line.contains("endfacet")){//tester le sens de la normale
		    		pIndex[k]=(short)(i-1);k++;
		    		pIndex[k]=(short)(i-2);k++;
		    		pIndex[k]=(short)(i-3);k++;
		    	}

		    }
		    reader.close();
		    	}else{//fichier binaire??
	    			
		    		reader.close();
		    		
		    		 FileInputStream fin = new FileInputStream("/sdcard/smar/p3.stl");
		    	       DataInputStream din = new DataInputStream(fin);
		    	       //read header :80Bytes
		    	      // byte header[]=new byte[80];
		    	       String s="";
		    	       for(i=0;i<100;i++){
		    	    	   int c=din.readUnsignedByte();
		    	    	   s=String.format(Locale.US,"%s %d", s,c);
		    	       }
		    	       Toast.makeText(csmar, s, Toast.LENGTH_LONG).show();
		    	       long l1=(long)din.readUnsignedShort();
		    	       long l2=(long)din.readUnsignedShort();
			    	   int ii=0,lt;
			    	   lt=(int)((l2<<16)+(l1<<0));
			  //  	   Toast.makeText(csmar,String.format("%d %d %d",(int)lt,(int)l1, (int)l2),Toast.LENGTH_LONG).show();
			    	   
			    	   //  float x,y,z;
			    	//   float nx,ny,nz;
			    	   
			    	 /* ProgressDialog progressBar;
			    	   progressBar = new ProgressDialog(csmara);
						progressBar.setCancelable(true);
						progressBar.setMessage("STL File opening ...");
						progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressBar.setProgress(0);
						progressBar.setMax((int)lt);
						progressBar.show();
						*/
			    	   nx=din.readFloat();
		    		   ny=din.readFloat();
		    		   nz=din.readFloat();
		    		   x=din.readFloat();y=din.readFloat();z=din.readFloat();
			    	   Toast.makeText(csmar,String.format("n: %f %f %f -- xyz: %f %f %f",nx,ny,nz,x,y,z),Toast.LENGTH_LONG).show();
			    	   
			    /*	   while(ii<lt){
			    		  // progressBar.setProgress(ii);
			    		 //  Sleep(10);
			    		   nx=din.readFloat();
			    		   ny=din.readFloat();
			    		   nz=din.readFloat();
			    		   for(i=0;i<3;i++){ 
			    			   x=din.readFloat();y=din.readFloat();z=din.readFloat();
			    			vertices[j]=x;j++;
			    			vertices[j]=y;j++;
			    			vertices[j]=z;j++;
			    			if(x<x1)x1=x;if(x>x2)x2=x;
			    			if(y<y1)y1=y;if(y>y2)y2=y;
			    			if(z<z1)z1=z;if(z>z2)z2=z;
			    		   }
			    		    pIndex[k]=(short)(ii-1);k++;
				    		pIndex[k]=(short)(ii-2);k++;
				    		pIndex[k]=(short)(ii-3);k++;
				    		din.readUnsignedShort();
				    		
			    	   }*/
			    	   din.close();
			    //	   progressBar.cancel();
		    		
		    	}
		    }
		    
			}catch (Exception e){//Catch exception if any
				Toast.makeText(csmar, e.toString()+e.getMessage(), Toast.LENGTH_LONG).show();
			}
			Toast.makeText(csmar, String.format("x:%f -%f,  y:%f-%f, z:%f-%f",x1,x2,y1,y2,z1,z2), Toast.LENGTH_LONG).show();
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
		}
	}

	public class prisme extends primitive{
		public prisme(int axe, float coteface, int nbrpts,float points[],float vx,float vy, float vz, float r,float g,float b){
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			vertices=new float[nbrpts*2*3];
			int j=0;
			int k=0;
			switch(axe){
				case 0:
					if(vx>0){
						x1=coteface;
						x2=coteface+vx;
					}else{
						x2=coteface;
						x1=coteface+vx;	
					}
					y1=points[0];y2=points[0];
					z1=points[1];z2=points[1];
					for(int i=0;i<nbrpts;i++){
						vertices[k]=coteface;k++;
						vertices[k]=points[j];k++;
						if(points[j]<y1)
							y1=points[j];
						if(points[j]>y2)
							y2=points[j];
						
						j++;
						vertices[k]=points[j];k++;
						if(points[j]<z1)
							z1=points[j];
						if(points[j]>z2)
							z2=points[j];
						j++;
						
					}
					j=0;
					for(int i=0;i<nbrpts;i++){
						vertices[k]=coteface+vx;k++;
						vertices[k]=points[j]+vy;k++;
						if(points[j]+vy<y1)
							y1=points[j]+vy;
						if(points[j]+vy>y2)
							y2=points[j]+vy;
					
						j++;
						vertices[k]=points[j]+vz;k++;
						if(points[j]+vz<z1)
							z1=points[j]+vz;
						if(points[j]+vz>z2)
							z2=points[j]+vz;
						j++;
					}
					
				break;
				case 1:
					if(vy>0){
						y1=coteface;
						y2=coteface+vy;
					}else{
						y2=coteface;
						y1=coteface+vy;	
					}
					x1=points[0];x2=points[0];
					z1=points[1];z2=points[1];

					for(int i=0;i<nbrpts;i++){
						vertices[k]=points[j];k++;
						if(points[j]<x1)
							x1=points[j];
						if(points[j]>x2)
							x2=points[j];
						j++;
						vertices[k]=coteface;k++;
						vertices[k]=points[j];k++;
						if(points[j]<z1)
							z1=points[j];
						if(points[j]>z2)
							z2=points[j];
						j++;
					}
					j=0;
					for(int i=0;i<nbrpts;i++){
						vertices[k]=points[j]+vx;k++;
						if(points[j]+vx<x1)
							x1=points[j]+vx;
						if(points[j]+vx>x2)
							x2=points[j]+vx;
						j++;
						vertices[k]=coteface+vy;k++;
						vertices[k]=points[j]+vz;k++;
						if(points[j]+vz<z1)
							z1=points[j]+vz;
						if(points[j]+vz>z2)
							z2=points[j]+vz;
						j++;
					}
					
				break;
				case 2:
					if(vz>0){
						z1=coteface;
						z2=coteface+vz;
					}else{
						z2=coteface;
						z1=coteface+vz;	
					}
					x1=points[0];x2=points[0];
					y1=points[1];y2=points[1];

					for(int i=0;i<nbrpts;i++){
						vertices[k]=points[j];k++;
						if(points[j]<x1)
							x1=points[j];
						if(points[j]>x2)
							x2=points[j];
						j++;
						vertices[k]=points[j];k++;
						if(points[j]<y1)
							y1=points[j];
						if(points[j]>y2)
							y2=points[j];
						j++;
						vertices[k]=coteface;k++;
					}
					j=0;
					for(int i=0;i<nbrpts;i++){
						vertices[k]=points[j]+vx;k++;
						if(points[j]+vx<x1)
							x1=points[j]+vx;
						if(points[j]+vx>x2)
							x2=points[j]+vx;
						j++;
						vertices[k]=points[j]+vy;k++;
						if(points[j]+vy<y1)
							y1=points[j]+vy;
						if(points[j]+vy>y2)
							y2=points[j]+vy;
						j++;
						vertices[k]=coteface+vz;k++;
					}
					
				break;
			}
			

			
			pIndex= new short[4*(nbrpts-1)*3];//sur le cote N facettes=2N triangles, un sommet c N-2 triangle
			k=0;
			for(j=1;j<nbrpts-1;j++){//sommet bat
				
				pIndex[k]=(short)(j + 1);k++;
				pIndex[k]=(short)j; k++;
				pIndex[k]=0;k++;
				
				
			}
			for(j=1;j<nbrpts-1;j++){//sommet haut
				pIndex[k]=(short)nbrpts;k++;					
				pIndex[k]=(short)(nbrpts+j); k++;
				pIndex[k]=(short)(nbrpts+j + 1);k++;
				
				
			}
			for(j=0;j<nbrpts-1;j++){//facettes
				pIndex[k]=(short)j;k++;
				pIndex[k]=(short)(j+1);k++;
				pIndex[k]=(short)(nbrpts+j+1);k++;

				pIndex[k]=(short)(nbrpts+j+1);k++;
				pIndex[k]=(short)(nbrpts+j);k++;
				pIndex[k]=(short)j;k++;
				
					

			}
			pIndex[k]=(short)(nbrpts-1);k++;
			pIndex[k]=0;k++;
			pIndex[k]=(short)(nbrpts);k++;
			
			pIndex[k]=(short)(nbrpts);k++;
			pIndex[k]=(short)(2*nbrpts-1);k++;
			pIndex[k]=(short)(nbrpts-1);k++;

			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		}
	}
	public class coude extends primitive{
		public coude(int axe, int type, float X1, float X2, float Y1, float Y2, float Z1, float Z2, float R1, float R2, float r,float g,float b ){
			int	Nb_Pts=0;
			switch(type){
			case 0:
				Nb_Pts=48;
				break;
			case 1:
				Nb_Pts=28;
				break;
			case 2:
				Nb_Pts=28;
				break;
			}
			int i,j,k;
			float Angle1;
			float Angle;
			
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			float maxR=(R1>R2?R1:R2);
			vertices=new float[Nb_Pts*3];
			j=0;
			switch(axe){
			case 0:
				
				if(Z1==Z2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(Y2-Y1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2-R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2-R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						x1=X1;x2=X2;
						y1=Y1-R1;y2=Y2+R2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(Y2-Y1))-Math.acos(R1/Math.sqrt((Y2-Y1)*(Y2-Y1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						vertices[j]=X1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2-R2;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2+R2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2-R2;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2+R2;j++;
						
						x1=X1;x2=X2;
						y1=Y1-R1;y2=Y2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					case 2:
						vertices[j]=X1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1+R1;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1-R1;j++;
						Angle1=(float)(Math.atan(R1/(Y2-Y1))+Math.acos(R2/Math.sqrt((Y2-Y1)*(Y2-Y1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2-R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1+R1;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1-R1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2-R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						x1=X1;x2=X2;
						y1=Y1;y2=Y2+R2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					}
					
				}
				
				else if(Y1==Y2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(Z2-Z1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.sin(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.sin(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						x1=X1;x2=X2;
						y1=Y1-maxR;y2=Y2+maxR;
						z1=Z1-R1;z2=Z2+R2;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(Z2-Z1))-Math.acos(R1/Math.sqrt((Z2-Z1)*(Z2-Z1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.sin(Angle1-Angle*i);j++;
						}
						vertices[j]=X1;j++;
						vertices[j]=Y2-R2;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y2+R2;j++;
						vertices[j]=Z2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1+R1*(float)Math.sin(Angle1-Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y2-R2;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y2+R2;j++;
						vertices[j]=Z2;j++;
						
						x1=X1;x2=X2;
						y1=Y1-maxR;y2=Y2+maxR;
						z1=Z1-R1;z2=Z2;

						break;
					case 2:
						vertices[j]=X1;j++;
						vertices[j]=Y1+R1;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y1-R1;j++;
						vertices[j]=Z1;j++;
						Angle1=(float)(Math.atan(R1/(Z2-Z1))+Math.acos(R2/Math.sqrt((Z2-Z1)*(Z2-Z1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1;j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y1+R1;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y1-R1;j++;
						vertices[j]=Z1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2;j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						x1=X1;x2=X2;
						y1=Y1-maxR;y2=Y2+maxR;
						z1=Z1;z2=Z2+R2;

						break;
					}
				}
			break;
			case 1:

				if(Z1==Z2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(X2-X1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z1-R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z2+R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z1-R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z2+R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						x1=X1-R1;x2=X2+R2;
						y1=Y1;y2=Y2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(X2-X1))-Math.acos(R1/Math.sqrt((X2-X1)*(X2-X1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z1-R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2+R2;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2-R2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z1-R1*(float)Math.cos(Angle1-Angle*i);j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2+R2;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2-R2;j++;
						
						x1=X1-R1;x2=X2;
						y1=Y1;y2=Y2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					case 2:
						vertices[j]=X1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1-R1;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1+R1;j++;
						Angle1=(float)(Math.atan(R1/(X2-X1))+Math.acos(R2/Math.sqrt((X2-X1)*(X2-X1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z2+R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						vertices[j]=X1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1-R1;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1+R1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z2+R2*(float)Math.cos(Angle1+Angle*i);j++;
						}
						x1=X1;x2=X2+R2;
						y1=Y1;y2=Y2;
						z1=Z1-maxR;z2=Z2+maxR;

						break;
					}
					
				}
				
				else if(X1==X2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(Z2-Z1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(-Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z1-R1*(float)Math.sin(-Angle1+Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(-Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z1-R1*(float)Math.sin(-Angle1+Angle*i);j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1;y2=Y2;
						z1=Z1-R1;z2=Z2+R2;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(Z2-Z1))-Math.acos(R1/Math.sqrt((Z2-Z1)*(Z2-Z1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(-Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z1-R1*(float)Math.sin(-Angle1+Angle*i);j++;
						}
						vertices[j]=X2-R2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X2+R2;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(-Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z1-R1*(float)Math.sin(-Angle1+Angle*i);j++;
						}
						vertices[j]=X2-R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X2+R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2;j++;
						
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1;y2=Y2;
						z1=Z1-R1;z2=Z2;

						break;
					case 2:
						vertices[j]=X1+R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X1-R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1;j++;
						Angle1=(float)(Math.atan(R1/(Z2-Z1))+Math.acos(R2/Math.sqrt((Z2-Z1)*(Z2-Z1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y1;j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						vertices[j]=X1+R1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X1-R1;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2;j++;
							vertices[j]=Z2+R2*(float)Math.sin(Angle1+Angle*i);j++;
						}
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1;y2=Y2;
						z1=Z1;z2=Z2+R2;

						break;
					}
				}
			break;
			case 2:
				
				if(Y1==Y2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(X2-X1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1-R1;y2=Y2+R2;
						z1=Z1;z2=Z2;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(X2-X1))-Math.acos(R1/Math.sqrt((X2-X1)*(X2-X1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y2-R2;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y2+R2;j++;
						vertices[j]=Z1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						vertices[j]=X2;j++;
						vertices[j]=Y2-R2;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X2;j++;
						vertices[j]=Y2+R2;j++;
						vertices[j]=Z2;j++;
						
						x1=X1-R1;x2=X2;
						y1=Y1-maxR;y2=Y2+maxR;
						z1=Z1;z2=Z2;

						break;
					case 2:
						vertices[j]=X1;j++;
						vertices[j]=Y1+R1;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y1-R1;j++;
						vertices[j]=Z1;j++;
						Angle1=(float)(Math.atan(R1/(X2-X1))+Math.acos(R2/Math.sqrt((X2-X1)*(X2-X1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						vertices[j]=X1;j++;
						vertices[j]=Y1+R1;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X1;j++;
						vertices[j]=Y1-R1;j++;
						vertices[j]=Z2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Y2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						x1=X1;x2=X2+R2;
						y1=Y1-maxR;y2=Y2+maxR;
						z1=Z1;z2=Z2;

						break;
					}
					
				}
				
				else if(X1==X2){
					switch(type){
					case 0://coude complet
						Angle1=(float)(Math.asin((R1-R2)/(Y2-Y1)));
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1-R1;y2=Y2+R2;
						z1=Z1;z2=Z2;

						break;
					case 1://demi coude gauche
						Angle1=(float)(-Math.atan(R2/(Y2-Y1))-Math.acos(R1/Math.sqrt((Y2-Y1)*(Y2-Y1)+R2*R2))+Math.PI/2);
						Angle=(float)(2*Angle1+Math.PI)/11;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						vertices[j]=X2-R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X2+R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z1;j++;
						for(i=0;i<12;i++){
							vertices[j]=X1+R1*(float)Math.cos(Angle1-Angle*i);j++;
							vertices[j]=Y1+R1*(float)Math.sin(Angle1-Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						vertices[j]=X2-R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X2+R2;j++;
						vertices[j]=Y2;j++;
						vertices[j]=Z2;j++;
						
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1-R1;y2=Y2;
						z1=Z1;z2=Z2;

						break;
					case 2:
						vertices[j]=X1+R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1;j++;
						vertices[j]=X1-R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z1;j++;
						Angle1=(float)(Math.atan(R1/(Y2-Y1))+Math.acos(R2/Math.sqrt((Y2-Y1)*(Y2-Y1)+R1*R1))-Math.PI/2);
						Angle=(float)(Math.PI-2*Angle1)/11;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z1;j++;
						}
						vertices[j]=X1+R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2;j++;
						vertices[j]=X1-R1;j++;
						vertices[j]=Y1;j++;
						vertices[j]=Z2;j++;
						for(i=0;i<12;i++){
							vertices[j]=X2-R2*(float)Math.cos(Angle1+Angle*i);j++;
							vertices[j]=Y2+R2*(float)Math.sin(Angle1+Angle*i);j++;
							vertices[j]=Z2;j++;
						}
						x1=X1-maxR;x2=X2+maxR;
						y1=Y1;y2=Y2+R2;
						z1=Z1;z2=Z2;

						break;
					}
				}
			break;				
			}
			pIndex= new short[(2*Nb_Pts-4)*3];//sur le cote N facettes=2N triangles, un sommet c N-2 triangle
			k=0;
			for(j=1;j<Nb_Pts/2-1;j++){//sommet bat
				
				pIndex[k]=(short)(j + 1);k++;
				pIndex[k]=(short)j; k++;
				pIndex[k]=0;k++;
				
				
			}
			for(j=1;j<Nb_Pts/2-1;j++){//sommet haut
				pIndex[k]=(short)(Nb_Pts/2);k++;					
				pIndex[k]=(short)(Nb_Pts/2+j); k++;
				pIndex[k]=(short)(Nb_Pts/2+j + 1);k++;
				
				
			}
			for(j=0;j<Nb_Pts/2-1;j++){//facettes
				pIndex[k]=(short)j;k++;
				pIndex[k]=(short)(j+1);k++;
				pIndex[k]=(short)(Nb_Pts/2+j+1);k++;

				pIndex[k]=(short)(Nb_Pts/2+j+1);k++;
				pIndex[k]=(short)(Nb_Pts/2+j);k++;
				pIndex[k]=(short)j;k++;
				
					

			}
			pIndex[k]=(short)(Nb_Pts/2-1);k++;
			pIndex[k]=0;k++;
			pIndex[k]=(short)(Nb_Pts/2);k++;
			
			pIndex[k]=(short)(Nb_Pts/2);k++;
			pIndex[k]=(short)(Nb_Pts-1);k++;
			pIndex[k]=(short)(Nb_Pts/2-1);k++;
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
	}
	
	
	public class sphere extends primitive{
		public sphere(int axe, int type, int discret, float ed1, float ed2, float ed3, float ed4,float r,float g,float b){
			float x=ed1,y=ed2,z=ed3,rayon=ed4;
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			int i,j,k;
			int Nb_Pts=24-(discret*6);
			int Nb_Cone_Tronque=0;
			float Angle_Finesse=0.f;
			float 	Angle=(float)((360./Nb_Pts)*(Math.PI/180.));
			switch(discret){
				case 0:
					Angle_Finesse=10.f;
					Nb_Cone_Tronque=8;
					break;
				case 1:
					Angle_Finesse=15.f;
					Nb_Cone_Tronque=5;
					break;
				case 3:
					Angle_Finesse=30.f;
					Nb_Cone_Tronque=2;
					break;
			}
			switch(type){
				case 0://sphere complete
					x1=x-rayon;x2=x+rayon;
					y1=y-rayon;y2=y+rayon;
					z1=z-rayon;z2=z+rayon;
					vertices=new float[(Nb_Pts*(Nb_Cone_Tronque*2+1)+2)*3];
					
	//				switch(axe){ Je ne fait pas la difference sur l'axe quand c une sphere complete
	//					case 0:
							vertices[0]=x-rayon;vertices[1]=y;vertices[2]=z;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float xx=(float)(x-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=xx;j++;
									vertices[j]=(float)(y+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							//sommet haut
							vertices[j]=x+rayon;j++;
							vertices[j]=y;j++;
							vertices[j]=z;j++;
							pIndex= new short[(2*Nb_Pts+4*Nb_Pts*Nb_Cone_Tronque)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque*2;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							short sommet2=(short) ((Nb_Pts*(Nb_Cone_Tronque*2+1)+2)-1);
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=sommet2;k++;
								pIndex[k]=(short)(sommet2-j);k++;
								pIndex[k]=(short)(sommet2-j-1); k++;
							}
							pIndex[k]=sommet2;k++;
							pIndex[k]=(short)(sommet2-Nb_Pts);k++;
							pIndex[k]=(short)(sommet2-1);k++;
							
							
		//				break;
		//			}
					
					break;
				case 1://demi sphere gauche
					vertices=new float[(Nb_Pts*(Nb_Cone_Tronque+1)+1)*3];
					switch(axe){
						case 0:
							x1=x-rayon;x2=x;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							vertices[0]=x-rayon;vertices[1]=y;vertices[2]=z;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float xx=(float)(x-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=xx;j++;
									vertices[j]=(float)(y+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
						case 1: //selon l'axe y
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y;
							z1=z-rayon;z2=z+rayon;
							vertices[0]=x;vertices[1]=y-rayon;vertices[2]=z;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float yy=(float)(y-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=yy;j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
						case 2: //selon l'axe z
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z;
							vertices[0]=x;vertices[1]=y;vertices[2]=z-rayon;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float zz=(float)(z-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(y+rayon2*Math.sin(Angle*k));j++;
									vertices[j]=zz;j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
					}
					break;
				case 2://demi sphere droit
					vertices=new float[(Nb_Pts*(Nb_Cone_Tronque+1)+1)*3];
					switch(axe){
						case 0:
							x1=x;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float xx=(float)(x-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=xx;j++;
									vertices[j]=(float)(y+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							vertices[j]=x+rayon;j++;vertices[j]=y;j++;vertices[j]=z;j++;//sommet haut
							
							break;
						case 1: //selon l'axe y
							x1=x-rayon;x2=x+rayon;
							y1=y;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float yy=(float)(y-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=yy;j++;
									vertices[j]=(float)(z-rayon2*Math.sin(Angle*k));j++;
								}
							}
							vertices[j]=x;j++;vertices[j]=y+rayon;j++;vertices[j]=z;j++;//sommet haut
					
							break;
						case 2: //selon l'axe z
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)/(180./Math.PI)));
								float zz=(float)(z-rayon*Math.cos(Angle_Finesse*(i+1)/(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(y+rayon2*Math.sin(Angle*k));j++;
									vertices[j]=zz;j++;
								}
							}
							vertices[j]=x;j++;vertices[j]=y;j++;vertices[j]=z+rayon;j++;//sommet haut
							
							break;
					}
					pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
					k=0;
					for(j=0;j<Nb_Pts-2;j++){//face basse de la demi sphere
						pIndex[k]=0;k++;
						pIndex[k]=(short)(2+j);k++;
						pIndex[k]=(short)(1+j);k++;
					}

					for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
						for(j=0;j<Nb_Pts-1;j++){
							pIndex[k]=(short)(j+i*Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+i*Nb_Pts);k++;
						}

						pIndex[k]=(short)((i+1)*Nb_Pts-1);k++;
						pIndex[k]=(short)(i*Nb_Pts);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts);k++;
						pIndex[k]=(short)((i+2)*Nb_Pts-1);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts-1);k++;
						
					}
					for(j=0;j<Nb_Pts-1;j++){//sommet bas
						pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+j);k++;
						pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+j + 1);k++;
						pIndex[k]=(short)(Nb_Pts*(Nb_Cone_Tronque+1)); k++;
					}
					pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+Nb_Pts-1);k++;
					pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts);k++;
					pIndex[k]=(short)(Nb_Pts*(Nb_Cone_Tronque+1));k++;
				


			}
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
		
	}
	public class sphere2 extends primitive{
		public sphere2(int axe, int type, int discret, float ed1, float ed2, float ed3, float ed4,float r,float g,float b){
			float x=ed1,y=ed2,z=ed3,rayon=ed4;
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			int i,j,k;
			float Angle_Finesse=15.f;
			int NbPts=(int) (360.f/Angle_Finesse);
			switch(type){
				case 0://sphere complete
					x1=x-rayon;x2=x+rayon;
					y1=y-rayon;y2=y+rayon;
					z1=z-rayon;z2=z+rayon;
					vertices=new float[(NbPts*(NbPts/2-1)+2)*3];
					vertices[0]=x;vertices[1]=y;vertices[2]=z+rayon;//sommet haut
					j=3;
					for(i=0;i<NbPts/2-1;i++){
						float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(Math.PI/180.)));
						float zz=(float)(z+rayon*Math.cos(Angle_Finesse*(i+1)*(Math.PI/180.)));
						for(k=0;k<NbPts;k++){
							vertices[j]=(float)(x+rayon2*Math.cos(Angle_Finesse*k*Math.PI/180.));j++;
							vertices[j]=(float)(y+rayon2*Math.sin(Angle_Finesse*k*Math.PI/180.));j++;
							vertices[j]=zz;j++;
						}
					}
					//sommet bas
					vertices[j]=x;j++;
					vertices[j]=y;j++;
					vertices[j]=z-rayon;j++;
					pIndex= new short[(2*NbPts+(NbPts/2-2)*NbPts*2)*3];
							k=0;
							for(j=1;j<NbPts;j++){//sommet haut
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j);k++;
								pIndex[k]=(short)(j+1); k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=(short)NbPts;k++;
							pIndex[k]=1;k++;
							for(i=0;i<NbPts/2-2;i++){//facette laterales des cones
								for(j=1;j<NbPts;j++){
									pIndex[k]=(short)(j+i*NbPts);k++;
									pIndex[k]=(short)(j+i*NbPts+NbPts);k++;
									pIndex[k]=(short)(j+i*NbPts+NbPts+1);k++;
									pIndex[k]=(short)(j+i*NbPts+NbPts+1);k++;
									pIndex[k]=(short)(j+i*NbPts+1);k++;
									pIndex[k]=(short)(j+i*NbPts);k++;
								}
								pIndex[k]=(short)(NbPts+i*NbPts);k++;
								pIndex[k]=(short)(NbPts+i*NbPts+NbPts);k++;
								pIndex[k]=(short)(i*NbPts+NbPts+1);k++;
								pIndex[k]=(short)(i*NbPts+NbPts+1);k++;
								pIndex[k]=(short)(i*NbPts+1);k++;
								pIndex[k]=(short)(NbPts+i*NbPts);k++;
									
							}
							short sommet2=(short) (NbPts*(NbPts/2-1)+1);
							for(j=1;j<NbPts;j++){//sommet bas
								pIndex[k]=sommet2;k++;
								pIndex[k]=(short)(sommet2-j);k++;
								pIndex[k]=(short)(sommet2-j-1); k++;
							}
							pIndex[k]=sommet2;k++;
							pIndex[k]=(short)(sommet2-NbPts);k++;
							pIndex[k]=(short)(sommet2-1);k++;
							
							
		//				break;
		//			}
					
					break;
				case 1://demi sphere gauche
	/*				vertices=new float[(Nb_Pts*(Nb_Cone_Tronque+1)+1)*3];
					switch(axe){
						case 0:
							x1=x-rayon;x2=x;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							vertices[0]=x-rayon;vertices[1]=y;vertices[2]=z;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float xx=(float)(x-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=xx;j++;
									vertices[j]=(float)(y+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
						case 1: //selon l'axe y
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y;
							z1=z-rayon;z2=z+rayon;
							vertices[0]=x;vertices[1]=y-rayon;vertices[2]=z;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float yy=(float)(y-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=yy;j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
						case 2: //selon l'axe z
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z;
							vertices[0]=x;vertices[1]=y;vertices[2]=z-rayon;//sommet bas
							j=3;
							for(i=0;i<Nb_Cone_Tronque+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float zz=(float)(z-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
									vertices[j]=zz;j++;
								}
							}
							pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
							k=0;
							for(j=1;j<Nb_Pts;j++){//sommet bas
								pIndex[k]=0;k++;
								pIndex[k]=(short)(j + 1);k++;
								pIndex[k]=(short)j; k++;
							}
							pIndex[k]=0;k++;
							pIndex[k]=1;k++;
							pIndex[k]=(short)Nb_Pts;k++;
							for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
								for(j=1;j<Nb_Pts;j++){
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
									pIndex[k]=(short)(j+i*Nb_Pts);k++;
								}

								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								pIndex[k]=(short)(i*Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)(i*Nb_Pts+Nb_Pts+1);k++;
								pIndex[k]=(short)((i+2)*Nb_Pts);k++;
								pIndex[k]=(short)((i+1)*Nb_Pts);k++;
								
							}
							for(j=1;j<Nb_Pts-1;j++){
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+1+j);k++;
								pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+2+j);k++;
							}

							break;
					}*/
					break;
				case 2://demi sphere droit
	/*				vertices=new float[(Nb_Pts*(Nb_Cone_Tronque+1)+1)*3];
					switch(axe){
						case 0:
							x1=x;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float xx=(float)(x-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=xx;j++;
									vertices[j]=(float)(y+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(z+rayon2*Math.sin(Angle*k));j++;
								}
							}
							vertices[j]=x+rayon;j++;vertices[j]=y;j++;vertices[j]=z;j++;//sommet haut
							
							break;
						case 1: //selon l'axe y
							x1=x-rayon;x2=x+rayon;
							y1=y;y2=y+rayon;
							z1=z-rayon;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float yy=(float)(y-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=yy;j++;
									vertices[j]=(float)(z-rayon2*Math.sin(Angle*k));j++;
								}
							}
							vertices[j]=x;j++;vertices[j]=y+rayon;j++;vertices[j]=z;j++;//sommet haut
					
							break;
						case 2: //selon l'axe z
							x1=x-rayon;x2=x+rayon;
							y1=y-rayon;y2=y+rayon;
							z1=z;z2=z+rayon;
							j=0;
							for(i=Nb_Cone_Tronque;i< Nb_Cone_Tronque*2+1;i++){
								float rayon2=(float)(rayon*Math.sin(Angle_Finesse*(i+1)*(180./Math.PI)));
								float zz=(float)(z-rayon*Math.cos(Angle_Finesse*(i+1)*(180./Math.PI)));
								for(k=0;k<Nb_Pts;k++){
									vertices[j]=(float)(x+rayon2*Math.cos(Angle*k));j++;
									vertices[j]=(float)(y+rayon2*Math.sin(Angle*k));j++;
									vertices[j]=zz;j++;
								}
							}
							vertices[j]=x;j++;vertices[j]=y;j++;vertices[j]=z+rayon;j++;//sommet haut
							
							break;
					}
					pIndex= new short[(2*Nb_Pts+2*Nb_Pts*Nb_Cone_Tronque-2)*3];
					k=0;
					for(j=0;j<Nb_Pts-2;j++){//face basse de la demi sphere
						pIndex[k]=0;k++;
						pIndex[k]=(short)(2+j);k++;
						pIndex[k]=(short)(1+j);k++;
					}

					for(i=0;i<Nb_Cone_Tronque;i++){//facette laterales des cones
						for(j=0;j<Nb_Pts-1;j++){
							pIndex[k]=(short)(j+i*Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+1+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+i*Nb_Pts+Nb_Pts);k++;
							pIndex[k]=(short)(j+i*Nb_Pts);k++;
						}

						pIndex[k]=(short)((i+1)*Nb_Pts-1);k++;
						pIndex[k]=(short)(i*Nb_Pts);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts);k++;
						pIndex[k]=(short)((i+2)*Nb_Pts-1);k++;
						pIndex[k]=(short)((i+1)*Nb_Pts-1);k++;
						
					}
					for(j=0;j<Nb_Pts-1;j++){//sommet bas
						pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+j);k++;
						pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+j + 1);k++;
						pIndex[k]=(short)(Nb_Pts*(Nb_Cone_Tronque+1)); k++;
					}
					pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts+Nb_Pts-1);k++;
					pIndex[k]=(short)(Nb_Cone_Tronque*Nb_Pts);k++;
					pIndex[k]=(short)(Nb_Pts*(Nb_Cone_Tronque+1));k++;
		*/		


			}
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
		
	}

	public class parallelepipede extends primitive{
		public parallelepipede(int axe, float ed1, float ed2, float ed3, float ed4, float ed5, float ed6,float r, float g, float b){
			float x,y,z,x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			switch(axe){
				case 0://selon x
					y=ed1;
					z=ed2;
					x1=ed3;
					x2=ed4;
					y1=y-ed5/2;
					y2=y+ed5/2;
					z1=z-ed6/2;
					z2=z+ed6/2;
					break;
				case 1://selon y
					x=ed1;
					z=ed2;
					y1=ed3;
					y2=ed4;
					x1=x-ed5/2;
					x2=x+ed5/2;
					z1=z-ed6/2;
					z2=z+ed6/2;
					break;
				case 2://selon z	
					x=ed1;
					y=ed2;
					z1=ed3;
					z2=ed4;
					y1=y-ed5/2;
					y2=y+ed5/2;
					x1=x-ed6/2;
					x2=x+ed6/2;
					break;		
			}
			vertices=new float[24];
			vertices[0]=x1;vertices[1]=y1;vertices[2]=z1;
			vertices[3]=x1;vertices[4]=y2;vertices[5]=z1;
			vertices[6]=x2;vertices[7]=y2;vertices[8]=z1;
			vertices[9]=x2;vertices[10]=y1;vertices[11]=z1;
			vertices[12]=x1;vertices[13]=y1;vertices[14]=z2;
			vertices[15]=x1;vertices[16]=y2;vertices[17]=z2;
			vertices[18]=x2;vertices[19]=y2;vertices[20]=z2;
			vertices[21]=x2;vertices[22]=y1;vertices[23]=z2;
			pIndex =new short[]{
					3,4,0, 0,4,1, 3,0,1,
					3,7,4, 7,6,4, 7,3,6,
					3,1,2,  1,6,2, 6,3,2,
					1,4,5, 5,6,1, 6,5,4
			};
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
	}
	public class cylindre extends primitive{
		public cylindre(int axe, int type, int discret, float ed1, float ed2, float ed3, float ed4, float ed5, float ed6,float r,float g,float b ){
			int	Nb_Pts=24-(discret*6);
			int i,j,k;
			float Angle=(float)(360/Nb_Pts)*((float)Math.PI/180);
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			float Rayon=ed5;
			vertices=new float[Nb_Pts*6];
			switch(axe){
			case 0:
				j=0;
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=ed3;j++;
					vertices[j]=-Rayon*((float)Math.cos(i*Angle))+ed1; j++;
					vertices[j]=Rayon*((float)Math.sin(i*Angle))+ed2;j++;
				}
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=ed4;j++;
					vertices[j]=-Rayon*((float)Math.cos(i*Angle))+ed1; j++;
					vertices[j]=Rayon*((float)Math.sin(i*Angle))+ed2;j++;
				}
				x1=ed3;x2=ed4;
				y1=ed1-Rayon;y2=ed1+Rayon;
				z1=ed2-Rayon;z2=ed2+Rayon;
			break;
			case 1:
				j=0;
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=Rayon*((float)Math.cos(i*Angle))+ed1;j++;
					vertices[j]=ed3; j++;
					vertices[j]=-Rayon*((float)Math.sin(i*Angle))+ed2;j++;
				}
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=Rayon*((float)Math.cos(i*Angle))+ed1;j++;
					vertices[j]=ed4; j++;
					vertices[j]=-Rayon*((float)Math.sin(i*Angle))+ed2;j++;
				}
				y1=ed3;y2=ed4;
				x1=ed1-Rayon;x2=ed1+Rayon;
				z1=ed2-Rayon;z2=ed2+Rayon;
			break;
			case 2:
				j=0;
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=Rayon*((float)Math.cos(i*Angle))+ed1;j++;
					vertices[j]=Rayon*((float)Math.sin(i*Angle))+ed2;j++;
					vertices[j]=ed3; j++;
					
				}
				for (i=0;i<Nb_Pts;i++){
					vertices[j]=Rayon*((float)Math.cos(i*Angle))+ed1;j++;
					vertices[j]=Rayon*((float)Math.sin(i*Angle))+ed2;j++;
					vertices[j]=ed4; j++;
					
				}
				z1=ed3;z2=ed4;
				x1=ed1-Rayon;x2=ed1+Rayon;
				y1=ed2-Rayon;y2=ed2+Rayon;
			break;				
			}
			pIndex= new short[(4*Nb_Pts-4)*3];//sur le cote N facettes=2N triangles, un sommet c N-2 triangle
			k=0;
			for(j=1;j<Nb_Pts-1;j++){//sommet bat
				
				pIndex[k]=(short)(j + 1);k++;
				pIndex[k]=(short)j; k++;
				pIndex[k]=0;k++;
				
				
			}
			for(j=1;j<Nb_Pts-1;j++){//sommet haut
				pIndex[k]=(short)Nb_Pts;k++;					
				pIndex[k]=(short)(Nb_Pts+j); k++;
				pIndex[k]=(short)(Nb_Pts+j + 1);k++;
				
				
			}
			for(j=0;j<Nb_Pts-1;j++){//facettes
				pIndex[k]=(short)j;k++;
				pIndex[k]=(short)(j+1);k++;
				pIndex[k]=(short)(Nb_Pts+j+1);k++;

				pIndex[k]=(short)(Nb_Pts+j+1);k++;
				pIndex[k]=(short)(Nb_Pts+j);k++;
				pIndex[k]=(short)j;k++;
				
					

			}
			pIndex[k]=(short)(Nb_Pts-1);k++;
			pIndex[k]=0;k++;
			pIndex[k]=(short)(Nb_Pts);k++;
			
			pIndex[k]=(short)(Nb_Pts);k++;
			pIndex[k]=(short)(2*Nb_Pts-1);k++;
			pIndex[k]=(short)(Nb_Pts-1);k++;
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
	}
	public class cone extends primitive{
		public cone(int axe, int tronq, int discret, float ed1, float ed2, float ed3, float ed4, float ed5, float ed6,float r,float g,float b){
			int 	Nb_Pts=24-(discret*6);
			int i,j,k;
			float Angle=(float)(360/Nb_Pts)*((float)Math.PI/180);
			float x1=0,x2=0,y1=0,y2=0,z1=0,z2=0;
			float Rayon1=ed5;
			float Rayon2=ed6;
			float Rayon;
			
			switch(tronq){
			case 0://cone complet
				vertices=new float[Nb_Pts*3+3];
				switch(axe){
				case 0:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=ed3;j++;
						vertices[j]=Rayon1*((float)Math.cos(i*Angle))+ed1; j++;
						vertices[j]=-Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
					}
					vertices[j]=ed4;j++;
					vertices[j]=ed1;j++;
					vertices[j]=ed2;j++;
					x1=ed3;x2=ed4;
					y1=ed1-Rayon1;y2=ed1+Rayon1;
					z1=ed2-Rayon1;z2=ed2+Rayon1;
				break;
				case 1:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon1*((float)Math.cos(i*Angle))+ed1; j++;
						vertices[j]=ed3;j++;
						vertices[j]=-Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
					}
					vertices[j]=ed1;j++;
					vertices[j]=ed4;j++;
					vertices[j]=ed2;j++;
					y1=ed3;y2=ed4;
					x1=ed1-Rayon1;x2=ed1+Rayon1;
					z1=ed2-Rayon1;z2=ed2+Rayon1;
				break;
				case 2:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon1*((float)Math.cos(i*Angle))+ed1; j++;
						vertices[j]=Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
						vertices[j]=ed3;j++;
						
					}
					vertices[j]=ed1;j++;
					vertices[j]=ed2;j++;
					vertices[j]=ed4;j++;

					z1=ed3;z2=ed4;
					x1=ed1-Rayon1;x2=ed1+Rayon1;
					y1=ed2-Rayon1;y2=ed2+Rayon1;
				break;
				
				}
				pIndex= new short[(2*Nb_Pts-2)*3];
				k=0;
				for(j=1;j<Nb_Pts-1;j++){
					pIndex[k]=0;k++;
					pIndex[k]=(short)(j + 1);k++;
					
					pIndex[k]=(short)j; k++;
				}
				for(j=0;j<Nb_Pts-1;j++){
					pIndex[k]=(short)j;k++;
					pIndex[k]=(short)(j+1);k++;
					pIndex[k]=(short)Nb_Pts;k++;
				}
				pIndex[k]=(short)(Nb_Pts-1);k++;
				pIndex[k]=0;k++;
				pIndex[k]=(short)Nb_Pts;k++;
			
				break;
			case 1: //cone tronqué
				vertices=new float[Nb_Pts*6];
				switch(axe){
				case 0:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=ed3;j++;
						vertices[j]=-Rayon1*((float)Math.cos(i*Angle))+ed1; j++;
						vertices[j]=Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
					}
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=ed4;j++;
						vertices[j]=-Rayon2*((float)Math.cos(i*Angle))+ed1; j++;
						vertices[j]=Rayon2*((float)Math.sin(i*Angle))+ed2;j++;
					}
					x1=ed3;x2=ed4;
					Rayon=(Rayon1>Rayon2?Rayon1:Rayon2);
					y1=ed1-Rayon;y2=ed1+Rayon;
					z1=ed2-Rayon;z2=ed2+Rayon;
				break;
				case 1:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon1*((float)Math.cos(i*Angle))+ed1;j++;
						vertices[j]=ed3; j++;
						vertices[j]=-Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
					}
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon2*((float)Math.cos(i*Angle))+ed1;j++;
						vertices[j]=ed4; j++;
						vertices[j]=-Rayon2*((float)Math.sin(i*Angle))+ed2;j++;
					}
					y1=ed3;y2=ed4;
					Rayon=(Rayon1>Rayon2?Rayon1:Rayon2);
					x1=ed1-Rayon;x2=ed1+Rayon;
					z1=ed2-Rayon;z2=ed2+Rayon;
				break;
				case 2:
					j=0;
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon1*((float)Math.cos(i*Angle))+ed1;j++;
						vertices[j]=Rayon1*((float)Math.sin(i*Angle))+ed2;j++;
						vertices[j]=ed3; j++;
						
					}
					for (i=0;i<Nb_Pts;i++){
						vertices[j]=Rayon2*((float)Math.cos(i*Angle))+ed1;j++;
						vertices[j]=Rayon2*((float)Math.sin(i*Angle))+ed2;j++;
						vertices[j]=ed4; j++;
						
					}
					z1=ed3;z2=ed4;
					Rayon=(Rayon1>Rayon2?Rayon1:Rayon2);
					x1=ed1-Rayon;x2=ed1+Rayon;
					y1=ed2-Rayon;y2=ed2+Rayon;
				break;				
				}
				pIndex= new short[(4*Nb_Pts-4)*3];//sur le cote N facettes=2N triangles, un sommet c N-2 triangle
				k=0;
				for(j=1;j<Nb_Pts-1;j++){//sommet bat
					
					pIndex[k]=(short)(j + 1);k++;
					pIndex[k]=(short)j; k++;
					pIndex[k]=0;k++;
					
					
				}
				for(j=1;j<Nb_Pts-1;j++){//sommet haut
					pIndex[k]=(short)Nb_Pts;k++;					
					pIndex[k]=(short)(Nb_Pts+j); k++;
					pIndex[k]=(short)(Nb_Pts+j + 1);k++;
					
					
				}
				for(j=0;j<Nb_Pts-1;j++){//facettes
					pIndex[k]=(short)j;k++;
					pIndex[k]=(short)(j+1);k++;
					pIndex[k]=(short)(Nb_Pts+j+1);k++;

					pIndex[k]=(short)(Nb_Pts+j+1);k++;
					pIndex[k]=(short)(Nb_Pts+j);k++;
					pIndex[k]=(short)j;k++;
					
						

				}
				pIndex[k]=(short)(Nb_Pts-1);k++;
				pIndex[k]=0;k++;
				pIndex[k]=(short)(Nb_Pts);k++;
				
				pIndex[k]=(short)(Nb_Pts);k++;
				pIndex[k]=(short)(2*Nb_Pts-1);k++;
				pIndex[k]=(short)(Nb_Pts-1);k++;
				
							
			}
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
	
	}
	public class cube extends primitive{
		
		/**
		 * @param axe
		 * @param edit1
		 * @param edit2
		 * @param edit3
		 * @param edit4
		 * @param r
		 * @param g
		 * @param b
		 */
		public cube(int axe, float edit1, float edit2, float edit3, float edit4,float r,float g,float b){
			float x,y,z,x1=0,x2=0,y1=0,y2=0,z1=0,z2=0,dimension;
			switch(axe){
				case 0://selon x
					y=edit1;
					z=edit2;
					x1=edit3;
					x2=edit4;
					dimension=Math.abs(x2-x1);
					y1=y-dimension/2;
					y2=y+dimension/2;
					z1=z-dimension/2;
					z2=z+dimension/2;
					break;
				case 1://selon y
					x=edit1;
					z=edit2;
					y1=edit3;
					y2=edit4;
					dimension=Math.abs(y2-y1);
					x1=x-dimension/2;
					x2=x+dimension/2;
					z1=z-dimension/2;
					z2=z+dimension/2;
					break;
				case 2://selon z	
					x=edit1;
					y=edit2;
					z1=edit3;
					z2=edit4;
					dimension=Math.abs(z2-z1);
					y1=y-dimension/2;
					y2=y+dimension/2;
					x1=x-dimension/2;
					x2=x+dimension/2;
					break;		
			}
			vertices=new float[24];
			vertices[0]=x1;vertices[1]=y1;vertices[2]=z1;
			vertices[3]=x1;vertices[4]=y2;vertices[5]=z1;
			vertices[6]=x2;vertices[7]=y2;vertices[8]=z1;
			vertices[9]=x2;vertices[10]=y1;vertices[11]=z1;
			vertices[12]=x1;vertices[13]=y1;vertices[14]=z2;
			vertices[15]=x1;vertices[16]=y2;vertices[17]=z2;
			vertices[18]=x2;vertices[19]=y2;vertices[20]=z2;
			vertices[21]=x2;vertices[22]=y1;vertices[23]=z2;
			pIndex =new short[]{
					3,4,0, 0,4,1, 3,0,1,
					3,7,4, 7,6,4, 7,3,6,
					3,1,2,  1,6,2, 6,3,2,
					1,4,5, 5,6,1, 6,5,4
			};
			R=r/255.f;
			G=g/255.f;
			B=b/255.f;
			extendbox(x1,x2,y1,y2,z1,z2);
		//	preparegeometry();
		}
		
	}
	public class primitive{
		int typeP;// 0 cube, 1 parallélépipède, 2 prisme
		float[] vertices;
		float[] vertices2;
		short[] pIndex;
		float R=0.0f,G=1.0f,B=0.0f;
		FloatBuffer vertBuff;
		ShortBuffer pBuff;
		public void draw(GL10 gl){
			gl.glFrontFace(GL10.GL_CW);
		// gl.glColor4f(R, G, B, 0.25f);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_FRONT);//GL_BACK //GL_FRONT_AND_BACK //GL_FRONT
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_LIGHTING);//rajoutéé pour le shadow :)
			gl.glEnable(GL10.GL_LIGHT0);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
			 gl.glColor4f(R, G, B, 1f);
			gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
			gl.glDisable(GL10.GL_DEPTH_TEST);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
		public void preparegeometry(){
			ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices2.length * 4);
			bBuff.order(ByteOrder.nativeOrder());
			vertBuff=bBuff.asFloatBuffer();
			vertBuff.put(vertices2);
			vertBuff.position(0);
			ByteBuffer pbBuff=ByteBuffer.allocateDirect(pIndex.length * 2);
			pbBuff.order(ByteOrder.nativeOrder());
			pBuff=pbBuff.asShortBuffer();
			pBuff.put(pIndex);
			pBuff.position(0);			
		}
		public void calculer(matriceH m){
			vertices2=m.mult(vertices);
			preparegeometry();
		}
	//	object geometrie;
	}
//	cube c1,c2,c3;
	cone c1,c2;
//	primitive[] primitives;
	maillon[] maillons;
	int nbrMaillons,mailCrt;
	float bgR,bgG,bgB;
	//cube c2;
	public smar(Context c, Context c2){
		//definition des couleurs du background
		bgR=1;bgG=1;bgB=1;
		csmar=c;
		csmara=c2;
		nbrMaillons=0;
		maillons=new maillon[30];
		nbrMaillons=1;
		mailCrt=0;
		maillons[0]=new maillon(-1,-1,0,0,0,0,0,0);
		maillons[0].nbprim=0;
		maillons[0].primitives=new primitive[50];
		try{
	          // Open the file that is the first 
	          // command line parameter
			//Environment.getExternalStorageDirectory
	          FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory()+"/SMAR2/tmp.smr");
	          // Get the object of DataInputStream
	          DataInputStream in = new DataInputStream(fstream);
	          BufferedReader br = new BufferedReader(new InputStreamReader(in));
	          String line="";
	          while ((line = br.readLine()) != null) {
	        	  line=line.toLowerCase(Locale.US);
	        	  line=line.replaceAll(" ", "");
	        	  if(line.contains("(")){
	        	  String line2=line.substring(line.indexOf("(")+1, line.indexOf(")"));
	        	  String []vars=line2.split(",");
	        	  if(line.contains("cube")){
	        		  if(vars.length==8){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new cube(Integer.parseInt(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]),Float.valueOf(vars[5]),Float.valueOf(vars[6]),Float.valueOf(vars[7]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "cube "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("cylindre")){
	        		  if(vars.length==12){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new cylindre(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),Integer.parseInt(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]),Float.valueOf(vars[9]),Float.valueOf(vars[10]),Float.valueOf(vars[11]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "cylindre "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("sphere")){
	        		  if(vars.length==10){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new sphere(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),Integer.parseInt(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]),Float.valueOf(vars[9]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "sphere "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("parallelepipede")){
	        		  if(vars.length==10){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new parallelepipede(Integer.parseInt(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]),Float.valueOf(vars[5]),Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]),Float.valueOf(vars[9]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "parallelepipede "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        		  
	        	  }else if(line.contains("coude")){
	        		  if(vars.length==13){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new coude(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]), Float.valueOf(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]),Float.valueOf(vars[9]),Float.valueOf(vars[10]),Float.valueOf(vars[11]),Float.valueOf(vars[12]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "coude "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("prisme")){
	        		  if(vars.length==Integer.parseInt(vars[2])*2+9){
	        			  float points[]=new float[vars.length-9];
	        			  for(int i=0;i<vars.length-9;i++){
	        				  points[i]=Float.valueOf(vars[6+i]);
	        			  }
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new prisme(Integer.parseInt(vars[0]), Float.valueOf(vars[1]),Integer.parseInt(vars[2]),points , Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]),Float.valueOf(vars[vars.length-3]),Float.valueOf(vars[vars.length-2]),Float.valueOf(vars[vars.length-1]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "prisme "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("cone")){
	        		  if(vars.length==12){
	        			  maillons[mailCrt].primitives[maillons[mailCrt].nbprim]=new cone(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),Integer.parseInt(vars[2]), Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]),Float.valueOf(vars[9]),Float.valueOf(vars[10]),Float.valueOf(vars[11]));
	        			  ++maillons[mailCrt].nbprim;
	        		  }else{
	        			  Toast.makeText(csmar, "cone "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }else if(line.contains("background")){
	        		  if(vars.length==3){
	        			bgR=Float.valueOf(vars[0])/255;
	        			bgG=Float.valueOf(vars[1])/255;
	        			bgB=Float.valueOf(vars[2])/255;
	        		  }
	        	  }else if(line.contains("ferm")){
	        		  
	        	  }else if(line.contains("liaison")){
	        		  maillons[mailCrt].calc();
	        		  if(vars.length==9){
	        			  
	        			  maillons[nbrMaillons]=new maillon(mailCrt,Integer.parseInt(vars[0]),Float.valueOf(vars[3]), Float.valueOf(vars[4]), Float.valueOf(vars[5]), Float.valueOf(vars[6]),Float.valueOf(vars[7]),Float.valueOf(vars[8]));//je teste et je regarde:)
	        			  maillons[nbrMaillons].parametres[0].min=Float.valueOf(vars[1]);
	        			  maillons[nbrMaillons].parametres[0].max=Float.valueOf(vars[2]);
	        			  maillons[nbrMaillons].primitives=new primitive[50];
	        			  mailCrt=nbrMaillons;
	        			  ++nbrMaillons;
	        			  maillons[mailCrt].nbprim=0;
	        			  
	        				
	        		  }else{
	        			  Toast.makeText(csmar, "liaison "+vars.length, Toast.LENGTH_LONG).show();
	        		  }
	        	  }
	        	  }else if(line.contains("courant")){
	        		  maillons[mailCrt].calc();
	        		  mailCrt=Integer.parseInt(line.substring(line.indexOf("=")+1,line.indexOf(";")))-1;//je verifie//or -1 a vérifier?
	        		  
	        	  }
	          }
	          
	          maillons[mailCrt].calc();//averifier

	          in.close();
	    }catch (Exception e){//Catch exception if any
	  //  	Toast.makeText(csmar, "haha"+e.toString(), Toast.LENGTH_LONG).show();
	    }
		
		
/*		maillons=new maillon[3];
		maillons[0]=new maillon(-1,-1,0,0,0,0,0,0);
		maillons[0].primitives=new primitive[10];
	//	maillons[0].primitives[0]=new  cylindre(0,0,0,0,0,0,5,10,0,200,200,200);//cube(0,0,0,0,5,255,0,255);
		maillons[0].primitives[0]=new  cylindre(2,0,0,0,0,0,3,49,0,255,255,0);//cube(0,0,0,0,5,255,0,255);
		maillons[0].primitives[1]=new  cylindre(2,0,0,0,0,46,43,49,0,255,255,0); 
		maillons[0].primitives[2]=new  cylindre(2,0,0,38.97f,22.5f,3,43,3,0,0,0,0);
		maillons[0].primitives[3]=new  cylindre(2,0,0,-38.97f,22.5f,3,43,3,0,0,0,0);
		maillons[0].primitives[4]=new  cylindre(2,0,0,38.97f,-22.5f,3,43,3,0,0,0,0);
		maillons[0].primitives[5]=new  cylindre(2,0,0,-38.97f,-22.5f,3,43,3,0,0,0,0);
		maillons[0].primitives[6]=new  cylindre(2,0,0,0,45,3,43,3,0,0,0,0);
		maillons[0].primitives[7]=new  cylindre(2,0,0,0,-45,3,43,3,0,0,0,0);		
		maillons[0].primitives[8]=new  parallelepipede(2,10,0,20,54,40,20,0,0,0);
		maillons[0].primitives[9]=new  cylindre(2,0,0,0,0,54,55,10,0,0,0,0);
		maillons[0].nbprim=10;
		maillons[0].calc();
		maillons[1]=new maillon(0,2,0,0,60,0,0,0);
		maillons[1].primitives=new primitive[10];
		maillons[1].primitives[0]=new cylindre(2,0,0,0,0,-5,-4,7,0,204,204,204);
		maillons[1].primitives[1]=new cylindre(2,0,0,0,0,-4,-3,12,0,204,204,204);
		maillons[1].primitives[2]=new cylindre(2,0,0,0,0,0,-3,44.5f,0,255,255,0);
		maillons[1].primitives[3]=new parallelepipede(2,-10,-51,0,-3,42,32,255,255,0);
		maillons[1].primitives[4]=new parallelepipede(2,-10,0,0,23,40,30,0,0,0);
		maillons[1].primitives[5]=new cylindre(1,0,0,0,13,15,17,10,0,0,0,0);
		maillons[1].primitives[6]=new cylindre(1,0,0,0,13,17,20,7,0,0,0,0);
		maillons[1].primitives[7]=new parallelepipede(2,-10,-51,0,23,40,30,0,0,0);
		maillons[1].primitives[8]=new cylindre(1,0,0,0,13,-36,-34,10,0,0,0,0);
		maillons[1].primitives[9]=new cylindre(1,0,0,0,13,-34,-31,7,0,0,0,0);
		maillons[1].nbprim=10;
		maillons[1].calc();
		maillons[2]=new maillon(1,1,0,0,13,0,0,0);
		maillons[2].primitives=new primitive[13];
		maillons[2].primitives[0]=new cylindre(1,0,0,0.f,0.f,20.f,22.f,7.f,0,204,204,204);
		maillons[2].primitives[1]=new cylindre(1,0,0,0,0,22,24,12,0,204,204,204);
		maillons[2].primitives[2]=new cylindre(1,0,0,0,0,-31,-29,7,0,204,204,204);
		maillons[2].primitives[3]=new cylindre(1,0,0,0,0,-29,-27,12,0,204,204,204);
		maillons[2].primitives[6]=new cylindre(1,0,0,-8,47.5f,-24,24,3,0,0,0,0);
		maillons[2].primitives[7]=new cylindre(1,0,0,8,47.5f,-24,24,3,0,0,0,0);
		maillons[2].primitives[8]=new cylindre(1,0,0,-8,73.5f,-24,24,3,0,0,0,0);
		maillons[2].primitives[9]=new cylindre(1,0,0,8,73.5f,-24,24,3,0,0,0,0);
		maillons[2].primitives[10]=new cylindre(1,0,0,0,121,20,22,7,0,204,204,204);
		maillons[2].primitives[11]=new cylindre(1,0,0,0,121,22,24,12,0,204,204,204);
	
		maillons[2].primitives[12]=new cylindre(1,0,0,0,121,-22,-24,7,0,204,204,204);
		maillons[2].primitives[4]=new coude(1,0,0,0,27,24,0,121,12,12,255,255,0);
		maillons[2].primitives[5]=new coude(1,0,0,0,-27,-24,0,121,12,12,255,255,0);
	
		maillons[2].nbprim=13;
		maillons[2].calc();

		*/
		
		/*
		maillons[1]=new maillon(0,3,6,0,0,0,0,0);
		maillons[1].primitives=new primitive[2];
		maillons[1].primitives[0]=new  cylindre(0,0,0,0,0,0,5,10,0,200,200,0);//cube(0,0,0,0,5,230,0,25);
		maillons[1].primitives[1]=new  cube(0,0,0,5,15,230,0,25);
		maillons[1].nbprim=2;
		maillons[1].calc();
		
		maillons[2]=new maillon(0,1,0,10,0,0,0,0);
		maillons[2].primitives=new primitive[2];
		maillons[2].primitives[0]=new  cylindre(1,0,0,0,0,0,10,10,0,200,200,0);//cube(0,0,0,0,5,230,0,25);
		maillons[2].primitives[1]=new  cube(1,0,0,10,15,230,0,25);
		maillons[2].nbprim=2;
		maillons[2].calc();*/
//		SeekBar seekbar=new SeekBar(csmara); 
		//seekbar.setX(1);
	//	seekbar.setY(2);
	//	seekbar.
		//seekbar.show();
		Button b1=new Button(csmar);
		b1.setText("hello");
		
				
	//	primitives=new primitive[1];
	//	primitives[1]=new cylindre(0,0,0,0,0,0,100,18,20,155,155,10);
	//	primitives[1]=new parallelepipede(0,22,30,0,0,0,20,10,155,10);
	//	primitives[0]=new cube(0,0,0,-1,5,255,0,255);
	//	primitives[0]=new sphere(0,0,0,0,0,0,20,255,0,255);
	//	primitives[0]=new stl("billes.stl",0,200,240);
	//	primitives[2]=new cone(0,1,0,10,0,9,1,10,20,15,155,10);
	
		
		//primitives[2]=new parallelepipede(0,-10,10,0,50,10,20,10,155,10);
	//	
	//	c1=new cube(0,0,0,-1,1,255,0,255);
	/*	c2=new cube(2,0,0,-3,-2,100,0,255);
		c3=new cube(1,0,0,-3,-20,255,0,100);
		*/
	//	c1=new cone(2,0,0,10,0,-9,0,10,20,155,155,10);//
		//c2=new cone(0,0,0,0,0,11,60,10,0,155,15,10);//
	}
	public void draw(GL10 gl){
	//	primitives[0].draw(gl);
		//if(!calcencours){
		try{
		for(int i=0;i<nbrMaillons;i++){
			for(int j=0;j<maillons[i].nbprim;j++)
				maillons[i].primitives[j].draw(gl);
			
		}
		}catch(Exception e){
			
		}
		//}
		//c1.draw(gl);
	//	c2.draw(gl);
		/*c2.draw(gl);
		c3.draw(gl);*/
	}
	public void calculdd(){
		float tmp=(float)Math.abs(lx1-lx2);
		if(tmp>dd)
			dd=tmp;
		tmp=(float)Math.abs(ly1-ly2);
		if(tmp>dd)
			dd=tmp;
		tmp=(float)Math.abs(lz1-lz2);
		if(tmp>dd)
			dd=tmp;
	}
	public void extendbox(float x1,float x2,float y1,float y2,float z1,float z2){
		if(x1<lx1)
			lx1=x1;
		if(x2<lx1)
			lx1=x2;
		if(x1>lx2)
			lx2=x1;
		if(x2>lx2)
			lx2=x2;
		if(y1<ly1)
			ly1=y1;
		if(y2<ly1)
			ly1=y2;
		if(y1>ly2)
			ly2=y1;
		if(y2>ly2)
			ly2=y2;
		if(z1<lz1)
			lz1=z1;
		if(z2<lz1)
			lz1=z2;
		if(z1>lz2)
			lz2=z1;
		if(z2>lz2)
			lz2=z2;
		calculdd();
	}

	public void calc(){
		if(!calcencours){
			calcencours=true;
		
			for(int i=0;i<nbrMaillons;i++){
				maillons[i].calc();
			}
			calcencours=false;
		}
	}
}
