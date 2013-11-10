package util;

public class Pos implements Direction {

	public final int x;
	public final int y;
	
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Pos move(Dir dir) {
		return new Pos(this.x + dir.dx, this.y + dir.dy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pos)) {
			return false;
		}
		Pos other = (Pos) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Pos (" + x + "," + y + ")";
	}
}
