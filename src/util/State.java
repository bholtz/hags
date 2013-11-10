package util;

public class State implements Direction{
	
	public final Pos pos;
	public final Dir facing;
	public boolean isActive;
	
	public State(Pos pos, Dir facing, boolean isActive) {
		this.pos = pos;
		this.facing = facing;
		this.isActive = isActive;
	}

	public State move(Dir dir) {
		if (dir == Dir.NONE)
			return this;
		else
			return new State(pos.move(dir), dir, isActive);
	}

	public void switchRole() {
		isActive = !isActive;
	}
}
