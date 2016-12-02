package game.util;

public class Vectores {
	
	public float x;
	public float y;
	public float w;

	public Vectores() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.w = 1.0f;
	}

	public Vectores(Vectores v) {
		this.x = v.x;
		this.y = v.y;
		this.w = v.w;
	}

	public Vectores(float x, float y) {
		this.x = x;
		this.y = y;
		this.w = 1.0f;
	}

	public Vectores(float x, float y, float w) {
		this.x = x;
		this.y = y;
		this.w = w;
	}

	public void translate(float tx, float ty) {
		x += tx;
		y += ty;
	}



	public void rotate(float rad) {
		float tmp = (float) (x * Math.cos(rad) - y * Math.sin(rad));
		y = (float) (x * Math.sin(rad) + y * Math.cos(rad));
		x = tmp;
	}


	public static Vectores polar(float angle, float radius) {
		return new Vectores(radius * (float) Math.cos(angle), radius
				* (float) Math.sin(angle));
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", x, y);
	}
}