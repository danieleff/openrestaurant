package openrestaurant.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class TransitionScreen extends ScreenWithStages {
    public ScreenWithStages inScreen;
    public ScreenWithStages outScreen;

    private Game game;
    
    public TransitionScreen(Game game) {
        this.game=game;
    }

    public void fadeScreens(TransitionType type, ScreenWithStages nextScreen) {
        fadeScreens(type, nextScreen, 0.3f);
    }

    public void fadeScreens(TransitionType type, ScreenWithStages nextScreen, float duration) {
    	ScreenWithStages inScreen = (ScreenWithStages) game.getScreen();
        if (inScreen instanceof TransitionScreen) {
            inScreen = this.outScreen;
        }
        fadeScreens(type, inScreen, nextScreen, duration);
    }

    public void fadeScreens(TransitionType type, final ScreenWithStages currentScreen, final ScreenWithStages nextScreen, float duration) {
        this.inScreen = currentScreen;
        this.outScreen = nextScreen;

        game.setScreen(this);

        outScreen.clear=true;
        inScreen.clear=false;

        currentScreen.clearActions();
        nextScreen.clearActions();
        
        currentScreen.hide();
        nextScreen.show();

        for (Stage stage : inScreen.getStages()) {
        	stage.getRoot().setPosition(0, 0);
        	stage.getRoot().setRotation(0);
        	stage.getRoot().setScale(1, 1);
        	stage.getRoot().setColor(Color.WHITE);
        }
        for (Stage stage : outScreen.getStages()) {
        	stage.getRoot().setPosition(0, 0);
        	stage.getRoot().setRotation(0);
        	stage.getRoot().setScale(1, 1);
        	stage.getRoot().setColor(Color.WHITE);
        }
        
        RunnableAction endAction = Actions.run(new EndTransition());
        
        switch (type) {
        /*
        case SLIDE_LEFT_RIGHT:
            nextScreen.stage.getRoot().setPosition(-nextScreen.stage.getWidth(), 0);
            nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, 0, duration, Interpolation.exp10Out)));
            currentScreen.stage.addAction(Actions.sequence(Actions.moveTo(currentScreen.stage.getWidth(), 0, duration, Interpolation.exp10Out), endAction));
            break;
        case SLIDE_RIGHT_LEFT:
            nextScreen.stage.getRoot().setPosition(nextScreen.stage.getWidth(), 0);
            nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, 0, duration, Interpolation.exp10Out)));
            currentScreen.stage.addAction(Actions.sequence(
                    Actions.moveTo(-currentScreen.stage.getWidth(), 0, duration, Interpolation.exp10Out), endAction));
            break;
        case SLIDE_UP_DOWN:
            nextScreen.stage.getRoot().setPosition(0, -nextScreen.stage.getHeight());
            nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, 0, duration, Interpolation.exp10Out)));
            currentScreen.stage.addAction(Actions.sequence(
                    Actions.moveTo(0, currentScreen.stage.getHeight(), duration, Interpolation.exp10Out),
                    endAction));
            break;
        case SLIDE_DOWN_UP:
            nextScreen.stage.getRoot().setPosition(0, nextScreen.stage.getHeight());
            nextScreen.stage.addAction(Actions.sequence(Actions.moveTo(0, 0, duration, Interpolation.exp10Out)));
            currentScreen.stage.addAction(Actions.sequence(
                    Actions.moveTo(0, -currentScreen.stage.getHeight(), duration, Interpolation.exp10Out),
                    endAction));
            break;
        case FADE:
            nextScreen.stage.addAction(Actions.sequence(Actions.fadeIn(duration)));
            currentScreen.stage.addAction(Actions.sequence(Actions.fadeOut(duration), endAction, Actions.color(Color.WHITE)));
            break;   */     
        case ROTATE_OUT:
            //nextScreen.stage.addAction(Actions.sequence(Actions.fadeIn(duration)));
        	
        	for (Stage stage : currentScreen.getStages()) {
        		stage.addAction(Actions.sequence(Actions.parallel(Actions.fadeOut(duration), Actions.rotateBy(5, duration), Actions.scaleTo(0.2f, 0.2f, duration, Interpolation.swingIn)), endAction));
			}
        	
            break;
        }
    }
    
    private class EndTransition implements Runnable {
        public void run() {
            game.setScreen(outScreen);
        }
    }
    
    @Override
    public void resize(int width, int height) {
        if (inScreen!=null) inScreen.resize(width, height);
        if (outScreen!=null) outScreen.resize(width, height);
    }

    @Override
    public void render(float delta) {
    	super.render(delta);
        
        outScreen.render(delta);
        inScreen.render(delta);
    }

    public static enum TransitionType {
        FADE, SLIDE_LEFT_RIGHT, SLIDE_RIGHT_LEFT, SLIDE_UP_DOWN, SLIDE_DOWN_UP, ROTATE_OUT
    }

}
