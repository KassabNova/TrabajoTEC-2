package game.util;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

import game.util.Matrices;
import game.util.Vectores;

public class Utilidad {
	
	public static Matrices createViewport(
			float worldWidth, float worldHeight, 
			float screenWidth, float screenHeight ) {
		float sx = (screenWidth - 1) / worldWidth;
		float sy = (screenHeight - 1) / worldHeight;
		float tx = (screenWidth - 1) / 2.0f;
		float ty = (screenHeight - 1) / 2.0f;
		Matrices viewport = Matrices.scale(sx, -sy);
		viewport = viewport.mul(Matrices.translate(tx, ty));
		return viewport;
	}

	public static Matrices createReverseViewport(
			float worldWidth, float worldHeight, 
			float screenWidth, float screenHeight ) {
		float sx = worldWidth / (screenWidth - 1);
		float sy = worldHeight / (screenHeight - 1);
		float tx = (screenWidth - 1) / 2.0f;
		float ty = (screenHeight - 1) / 2.0f;
		Matrices viewport = Matrices.translate(-tx, -ty);
		viewport = viewport.mul(Matrices.scale(sx, -sy));
		return viewport;
	}
	
	public static void drawPolygon(Graphics g, Vectores[] polygon) {
		Vectores P;
		Vectores S = polygon[polygon.length - 1];
		for (int i = 0; i < polygon.length; ++i) {
			P = polygon[i];
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	public static void drawPolygon(Graphics g, List<Vectores> polygon) {
		Vectores S = polygon.get(polygon.size() - 1);
		for (Vectores P : polygon) {
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	public static void fillPolygon(Graphics2D g, Vectores[] polygon) {
		Polygon p = new Polygon();
		for (Vectores v : polygon) {
			p.addPoint((int) v.x, (int) v.y);
		}
		g.fill(p);
	}

	public static void fillPolygon(Graphics2D g, List<Vectores> polygon) {
		Polygon p = new Polygon();
		for (Vectores v : polygon) {
			p.addPoint((int) v.x, (int) v.y);
		}
		g.fill(p);
	}

	public static int drawString(Graphics g, int x, int y, String str) {
		return drawString(g, x, y, new String[] { str });
	}

	public static int drawString(Graphics g, int x, int y, List<String> str) {
		return drawString(g, x, y, str.toArray(new String[0]));
	}

	public static int drawString(Graphics g, int x, int y, String... str) {
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		for (String s : str) {
			g.drawString(s, x, y + fm.getAscent());
			y += height;
		}
		return y;
	}
}