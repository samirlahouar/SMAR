package smartmeca.smar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCube {
	private float verticies[]={
			10,10,-10,//
			10,-10,-10,
			-10,-10,-10,
			-10,10,-10,
			10,10,10,
			10,-10,10,
			-10,-10,10,
			-10,10,10
	};
	private float verticies2[]={
			3,1,-3,//
			3,-1,-3,
			-3,-1,-3,
			-3,1,-3,
			3,1,-1,
			3,-1,-1,
			-3,-1,-1,
			-3,1,-1
	};

	/*private float rgbaVals[]={
			1,1,0,.5f,//
			.25f,0 , .85f,1,
	};*/
		private FloatBuffer vertBuff,vertBuff2;
		private short[] pIndex ={
				3,4,0, 0,4,1, 3,0,1,
				3,7,4, 7,6,4, 7,3,6,
				3,1,2,  1,6,2, 6,3,2,
				1,4,5, 5,6,1, 6,5,4
		};
		private ShortBuffer pBuff;
		public GLCube(){
			ByteBuffer bBuff=ByteBuffer.allocateDirect(verticies.length * 4);
			bBuff.order(ByteOrder.nativeOrder());
			vertBuff=bBuff.asFloatBuffer();
			vertBuff.put(verticies);
			vertBuff.position(0);
			ByteBuffer pbBuff=ByteBuffer.allocateDirect(pIndex.length * 2);
			pbBuff.order(ByteOrder.nativeOrder());
			pBuff=pbBuff.asShortBuffer();
			pBuff.put(pIndex);
			pBuff.position(0);
			
			//cube2
			bBuff=ByteBuffer.allocateDirect(verticies2.length * 4);
			bBuff.order(ByteOrder.nativeOrder());
			vertBuff2=bBuff.asFloatBuffer();
			vertBuff2.put(verticies2);
			vertBuff2.position(0);
			
		}
		public void draw(GL10 gl){
			/*gl.glFrontFace(GL10.GL_CW);
			 gl.glColor4f(1.0f, 0.0f, 0.0f, 0.25f);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
			gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
			*/
		 gl.glColor4f(0.0f, 1.0f, 0.0f, 0.25f);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff2);
			gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
		
		
	}
