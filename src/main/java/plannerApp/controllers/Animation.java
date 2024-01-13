package plannerApp.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Animation {
    public enum animationStyles
	{	instantShow,
		rightToLeft,
		leftToRight,
		downToUp
	}

    public static void animate(Pane parent, Parent children, animationStyles style) {
        switch (style) {
            case instantShow:
                instantShowAnimation(parent, children);
                break;
            case rightToLeft:
                rightToLeftAnimation(parent, children);
                break;
            case leftToRight:
                leftToRightAnimation(parent, children);
                break;
            case downToUp:
                downToUpAnimation(parent, children);
                break;
            default:
                System.out.println("Unrecognize style in activateScreen func: " + style);
                break;
            }
    }
	
    private static void instantShowAnimation(Pane parent, Parent children) {
		parent.getChildren().add(children);
	}
	
	private static void rightToLeftAnimation(Pane parent, Parent children) {
		children.translateXProperty().set(parent.getWidth());
		parent.getChildren().add(children);
		
		Timeline timeline = new Timeline();
		KeyValue kv = new KeyValue(children.translateXProperty(), 0, Interpolator.EASE_IN);
		KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
	
	private static void leftToRightAnimation(Pane parent, Parent children) {
		children.translateXProperty().set(-parent.getWidth());
		parent.getChildren().add(children);
		
		Timeline timeline = new Timeline();
		KeyValue kv = new KeyValue(children.translateXProperty(), 0, Interpolator.EASE_IN);
		KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
	
	private static void downToUpAnimation(Pane parent, Parent children) {
		children.translateYProperty().set(parent.getHeight());
		parent.getChildren().add(children);
		
		Timeline timeline = new Timeline();
		KeyValue kv = new KeyValue(children.translateYProperty(), 0, Interpolator.EASE_IN);
		KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
}
