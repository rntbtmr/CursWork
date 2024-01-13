package plannerApp.loader;

import javafx.application.Preloader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import plannerApp.controllers.FileHelper;

public class LoadWindow extends Preloader {
    
    private Stage loadWindow;
	private int loadWindowSize = 350;

    public void start(Stage loadWindow) throws Exception {
        this.loadWindow = loadWindow;
        Image logo = new Image("/images/logo.png");
        Scene loadScene = createLoadScene();
        setupLoadWindow(loadWindow, loadScene, logo);
    }

    private Scene createLoadScene() {
        Parent root = FileHelper.loadView(new Object(), "loadingView");
        Scene scene = new Scene(root, loadWindowSize, loadWindowSize);
        scene.setFill(null);
        return scene;
    }

    private void setupLoadWindow(Stage window, Scene loadScene, Image icon) {
        window.initStyle(StageStyle.TRANSPARENT);
        window.getIcons().add(icon);
        window.setScene(loadScene);
        window.sizeToScene();
        setStageCenter(window);
        window.show();
    }

    private void setStageCenter(Stage window) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((screenBounds.getWidth() - loadWindowSize) / 2); 
        window.setY((screenBounds.getHeight() - loadWindowSize) / 2);
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        // TODO::Track load status by -> pn.getProgress()
    }
 
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            loadWindow.hide();
        }
    }    
}