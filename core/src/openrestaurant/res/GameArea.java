package openrestaurant.res;

import java.util.Comparator;

import openrestaurant.res.object.Floor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class GameArea extends Group {
	
	public void resize(Rectangle screenCoords, Rectangle logicCoords) {
		float x=screenCoords.x;
		float y=screenCoords.y;
		float width=screenCoords.width;
		float height=screenCoords.height;
		
		if (screenCoords.width / (double) screenCoords.height > logicCoords.width/logicCoords.height) {
			height = screenCoords.height;
			width = (int) (screenCoords.height * logicCoords.width / logicCoords.height);
			y = screenCoords.y;
			x = screenCoords.x + screenCoords.width/2 - width / 2;
		} else {
			width = screenCoords.width;
			height = (int) (screenCoords.width * logicCoords.height / logicCoords.width);
			x = screenCoords.x;
			y = screenCoords.y + screenCoords.height/2 - height / 2;
		}
		
		setBounds(x, y, width, height);
		
		setScale(width / logicCoords.width, height / logicCoords.height);		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		zSort();
		super.draw(batch, parentAlpha);
	}

	private void zSort() {
		getChildren().sort(new Comparator<Actor>() {
			public int compare(Actor o1, Actor o2) {
				if (o1 instanceof Floor) return -1;
				if (o2 instanceof Floor) return 1;
				
				if (o1 instanceof Widget) return 1;
				if (o2 instanceof Widget) return -1;
				
				if (o1 instanceof WidgetGroup) return 1;
				if (o2 instanceof WidgetGroup) return -1;
				
				if (o2.getY()- o1.getY()<0) return -1;
				if (o2.getY() - o1.getY()>0) return 1;
				return 0;
			}
		});
	}
	
}
