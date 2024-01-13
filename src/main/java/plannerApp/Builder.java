package plannerApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import plannerApp.controllers.Animation;
import plannerApp.controllers.FileHelper;
import plannerApp.controllers.ScreenController;

public class Builder extends Application{
	
	private int mainWindowWidth = 1000, mainWindowHeight = 600;
	private Scene mainScene;
	private ScreenController screenController;
	private Image windowIcon;
	
	public static void main(String[] args) {
		System.setProperty("javafx.preloader", "plannerApp.loader.LoadWindow");
		launch(args);
	}

	@Override
	public void init() {
		// TODO::Delete delay for show (pre)loadingView
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < 300)  ;  // 0.3s

		windowIcon = new Image("/images/logo.png");
		mainScene = createScene();
		screenController = new ScreenController(mainScene);
		FileHelper.loadAllViews(screenController);
	}

	@Override
	public void start(Stage mainWindow) throws Exception {	
		setupWindow(mainWindow, mainScene);
		screenController.activateScreen("appView", Animation.animationStyles.instantShow);
	}



	private Scene createScene() {
		Scene scene = new Scene(new Pane(), mainWindowWidth, mainWindowHeight);
		scene.getStylesheets().add("/css/mainStyles.css");
		return scene;
	}

	private void setupWindow(Stage window, Scene scene) {
		window.initStyle(StageStyle.DECORATED);
		window.centerOnScreen();
		window.sizeToScene();
		window.setScene(scene);
		window.getIcons().add(windowIcon);
		window.setTitle("Personal Growth");
		window.show();
	}
}
