package MainWindow;

import javafx.stage.Stage;

/**
 *  Class for initializing the application and MainWindow. Also provides global access to the MainWindow that can be used by generators.
 */
public class Application extends javafx.application.Application {

    private static MainWindow window;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = new MainWindow();
        window.show();
    }
    
    /**
     * @return the instance of the application main window
     */
    public static MainWindow getMainWindow() {
        return window;
    }
    
}
