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

	public void scale(float sx, float sy) {
		x *= sx;
		y *= sy;
	}

	public void rotate(float rad) {
		float tmp = (float) (x * Math.cos(rad) - y * Math.sin(rad));
		y = (float) (x * Math.sin(rad) + y * Math.cos(rad));
		x = tmp;
	}

	public void shear(float sx, float sy) {
		float tmp = x + sx * y;
		y = y + sy * x;
		x = tmp;
	}

	public Vectores add(Vectores v) {
		return new Vectores(x + v.x, y + v.y);
	}

	public Vectores sub(Vectores v) {
		return new Vectores(x - v.x, y - v.y);
	}

	public Vectores mul(float scalar) {
		return new Vectores(scalar * x, scalar * y);
	}

	public Vectores div(float scalar) {
		return new Vectores(x / scalar, y / scalar);
	}

	public Vectores inv() {
		return new Vectores(-x, -y);
	}

	public Vectores norm() {
		return div(len());
	}

	public float dot(Vectores v) {
		return x * v.x + y * v.y;
	}

	public float len() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float lenSqr() {
		return x * x + y * y;
	}

	public Vectores perp() {
		return new Vectores(-y, x);
	}

	public float angle() {
		return (float) Math.atan2(y, x);
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