package game.util;

import game.util.Vectores;

public class Matrices {
	private float[][] m = new float[3][3];

	public Matrices() {
	}

	public Matrices(float[][] m) {
		setMatrix(m);
	}

	public Matrices add(Matrices m1) {
		return new Matrices(new float[][] {
			{ this.m[0][0] + m1.m[0][0], 
			  this.m[0][1] + m1.m[0][1],
			  this.m[0][2] + m1.m[0][2] },
			{ this.m[1][0] + m1.m[1][0],
			  this.m[1][1] + m1.m[1][1],
			  this.m[1][2] + m1.m[1][2] },
			{ this.m[2][0] + m1.m[2][0], 
			  this.m[2][1] + m1.m[2][1],
			  this.m[2][2] + m1.m[2][2] } }
		);
	}

	public Matrices sub(Matrices m1) {
		return new Matrices(new float[][] {
			{ this.m[0][0] - m1.m[0][0], 
			  this.m[0][1] - m1.m[0][1],
			  this.m[0][2] - m1.m[0][2] },
			{ this.m[1][0] - m1.m[1][0], 
			  this.m[1][1] - m1.m[1][1],
			  this.m[1][2] - m1.m[1][2] },
			{ this.m[2][0] - m1.m[2][0], 
			  this.m[2][1] - m1.m[2][1],
			  this.m[2][2] - m1.m[2][2] } }
		);
	}

	public Matrices mul(Matrices m1) {
		return new Matrices( new float[][] { 
			{ this.m[0][0] * m1.m[0][0] // ******
			+ this.m[0][1] * m1.m[1][0] // M[0,0]
			+ this.m[0][2] * m1.m[2][0], // ******
			  this.m[0][0] * m1.m[0][1] // ******
			+ this.m[0][1] * m1.m[1][1] // M[0,1]
			+ this.m[0][2] * m1.m[2][1], // ******
			  this.m[0][0] * m1.m[0][2] // ******
			+ this.m[0][1] * m1.m[1][2] // M[0,2]
			+ this.m[0][2] * m1.m[2][2] },// ******
			{ this.m[1][0] * m1.m[0][0] // ******
			+ this.m[1][1] * m1.m[1][0] // M[1,0]
			+ this.m[1][2] * m1.m[2][0], // ******
			  this.m[1][0] * m1.m[0][1] // ******
			+ this.m[1][1] * m1.m[1][1] // M[1,1]
			+ this.m[1][2] * m1.m[2][1], // ******
			  this.m[1][0] * m1.m[0][2] // ******
			+ this.m[1][1] * m1.m[1][2] // M[1,2]
			+ this.m[1][2] * m1.m[2][2] },// ******
			{ this.m[2][0] * m1.m[0][0] // ******
			+ this.m[2][1] * m1.m[1][0] // M[2,0]
			+ this.m[2][2] * m1.m[2][0], // ******
			  this.m[2][0] * m1.m[0][1] // ******
			+ this.m[2][1] * m1.m[1][1] // M[2,1]
			+ this.m[2][2] * m1.m[2][1], // ******
			  this.m[2][0] * m1.m[0][2] // ******
			+ this.m[2][1] * m1.m[1][2] // M[2,2]
			+ this.m[2][2] * m1.m[2][2] } // ******
		});
	}

	public void setMatrix(float[][] m) {
		this.m = m;
	}

	public static Matrices zero() {
		return new Matrices(new float[][] { 
			{ 0.0f, 0.0f, 0.0f },
			{ 0.0f, 0.0f, 0.0f }, 
			{ 0.0f, 0.0f, 0.0f } 
		});
	}

	public static Matrices identity() {
		return new Matrices( new float[][] { 
			{ 1.0f, 0.0f, 0.0f },
			{ 0.0f, 1.0f, 0.0f }, 
			{ 0.0f, 0.0f, 1.0f } 
		});
	}

	public static Matrices translate(Vectores v) {
		return translate(v.x, v.y);
	}

	public static Matrices translate(float x, float y) {
		return new Matrices(new float[][] { 
			{ 1.0f, 0.0f, 0.0f },
			{ 0.0f, 1.0f, 0.0f },
			{ x, y, 1.0f } 
		});
	}

	public static Matrices scale(Vectores v) {
		return scale(v.x, v.y);
	}

	public static Matrices scale(float x, float y) {
		return new Matrices(new float[][] {
			{ x, 0.0f, 0.0f },
			{ 0.0f, y, 0.0f }, 
			{ 0.0f, 0.0f, 1.0f } 
		});
	}

	public static Matrices shear(Vectores v) {
		return shear(v.x, v.y);
	}

	public static Matrices shear(float x, float y) {
		return new Matrices(new float[][] { 
			{ 1.0f, y, 0.0f },
			{ x, 1.0f, 0.0f }, 
			{ 0.0f, 0.0f, 1.0f } 
		});
	}

	public static Matrices rotate(float rad) {
		return new Matrices(new float[][] {
			{ (float) Math.cos(rad), (float) Math.sin(rad), 0.0f },
			{ (float) -Math.sin(rad), (float) Math.cos(rad), 0.0f },
			{ 0.0f, 0.0f, 1.0f } 
		});
	}

	public Vectores mul(Vectores vec) {
		return new Vectores(
			  vec.x * this.m[0][0] //
			+ vec.y * this.m[1][0] // V.x
			+ vec.w * this.m[2][0],//
			  vec.x * this.m[0][1] //
			+ vec.y * this.m[1][1] // V.y
			+ vec.w * this.m[2][1],//
			  vec.x * this.m[0][2] //
			+ vec.y * this.m[1][2] // V.w
			+ vec.w * this.m[2][2] //
		);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 3; ++i) {
			buf.append("[");
			buf.append(m[i][0]);
			buf.append(",\t");
			buf.append(m[i][1]);
			buf.append(",\t");
			buf.append(m[i][2]);
			buf.append("]\n");
		}
		return buf.toString();
	}
}