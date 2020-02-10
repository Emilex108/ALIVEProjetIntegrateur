package listeners;

import java.util.EventListener;

public interface DistanceChangedListener extends EventListener{
	public void distanceChanged(int left, int forward, int right);
}
