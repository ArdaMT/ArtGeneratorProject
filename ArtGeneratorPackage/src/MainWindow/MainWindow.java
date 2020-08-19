package MainWindow;

import Generators.Generator;
import Generators.RandomFunctionTree.RandomFunctionTreeGenerator;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main window of the application with menu bar for accessing the different graphical generators.
 */
public class MainWindow extends Stage {

    public static void setSaveMenuStatus(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int hue;
    private int depth;
    private int seed;
    private final ScrollPane scrollPane;
    private final BorderPane canvasPane;
    private Canvas canvas = new Canvas();
    private String generatorName = "Random Function Tree Generator";


    private static Label statusLine = new Label();
    private String[] generatorNames = new String[]{"Function Tree Generator"
    };

    private Menu generatorMenu;


    private MenuItem randomFunctionTreeGenerator = new MenuItem(generatorName);

    private static MenuItem save = new MenuItem("Save Image");
    public static MenuItem saveAs = new MenuItem("Save as");
    //Konstanten für Fenster Größe
    private final int HEIGHT = 600, WIDTH = 800;

    public MainWindow() {

        //Menu
        Menu fileMenu = new Menu("_File");
        generatorMenu = new Menu("_Generator");

        //Menu Items
        MenuItem clear = new MenuItem("Clear");
        fileMenu.getItems().add(clear);
        save.setDisable(true);
        saveAs.setDisable(true);
        SeparatorMenuItem separator = new SeparatorMenuItem();
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(separator);
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        fileMenu.getItems().add(saveAs);
        fileMenu.getItems().add(separator2);
        MenuItem quit = new MenuItem("Quit");
        fileMenu.getItems().add(quit);
        generatorMenu.getItems().add(new SeparatorMenuItem());
        generatorMenu.getItems().add(randomFunctionTreeGenerator);
        //Main Menu Bar
        MenuBar mainbar = new MenuBar();
        mainbar.getMenus().addAll(fileMenu, generatorMenu);

        //ScrollPane
        scrollPane = new ScrollPane();
        canvasPane = new BorderPane();
        canvasPane.setCenter(canvas);
        scrollPane.setContent(canvasPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        //Layout
        BorderPane pane = new BorderPane();
        pane.setTop(mainbar);
        pane.setCenter(scrollPane);
        HBox bottom = new HBox(statusLine);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(5, 5, 5, 5));
        pane.setBottom(bottom);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);

        setTitle("Art Generator");
        setScene(scene);

        //EventHandler der MenuItems mit Lamdas realisiert
        setOnCloseRequest(e -> {
            // Both needed to make sure Threads are stopped.
            Platform.exit();
            System.exit(0);
        });

        createGeneratorMenuEntries();

        // Event Handler
        clear.setOnAction((ActionEvent t) -> {
            setSaveAsMenuDisabled(true);
            setSaveMenuDisabled(true);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setHeight(0);
            canvas.setWidth(0);
        });
        save.setOnAction((ActionEvent t) -> {
            saveFileDialog();
        });
        saveAs.setOnAction((ActionEvent t) -> {
            saveFixName(depth, seed, hue);
        });
        quit.setOnAction((ActionEvent t) -> {
            close();
            // Both needed to make sure Threads are stopped.
            Platform.exit();
            System.exit(0);
        });
    }

    private void createGeneratorMenuEntries() {

        randomFunctionTreeGenerator.setOnAction((ActionEvent t) -> {
            initGenerator(RandomFunctionTreeGenerator.class);
        });
    }

    /**
     * Use this method for creating a generator window.
     * A generator instance is created, menubar is locked until generator is closed, and status is updated.
     *
     * @param generatorClass
     */
    private void initGenerator(Class generatorClass) {
        setStatus(generatorName + " " + MainStatus.READY.toString());
        setMenuItemsDisabled(true);
        try {
            Constructor c = generatorClass.getConstructor(Canvas.class, String.class);
            Generator generator = (Generator) c.newInstance(canvas, generatorName);
            generator.show();
            generator.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (event) -> {
                generator.setStopParameters();
                setStatus(MainStatus.OK.toString());
                setMenuItemsDisabled(false);
            });
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Speicher Dialog für Menu
    // Only file type png supported for now.
    private void saveFileDialog() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setTitle("Save to File");
        File file = fc.showSaveDialog(new Stage());
        if (file != null && !file.toString().endsWith(".png")) {
            file = new File(file.toString() + ".png");
        }
        saveFile(file);
    }

    public void saveFixName(int depth, int seed, int hue) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setInitialFileName(depth + "_" + seed + "_" + hue);
        fc.setTitle("Save to File");
        File file = fc.showSaveDialog(new Stage());
        String name = "";
        try {
            name = file.getAbsolutePath();
        } catch (NullPointerException e) {
        }
        if (!name.equals("")) {
            File newFile = new File(name + "\\" + depth + "_" + seed);
            if (newFile != null && !newFile.toString().endsWith(".png")) {
                newFile = new File(newFile.toString() + ".png");
            }
            saveFile(file);
        }
    }

    //Speicher Methode für Generatoren -> public
    public void saveFile(File file) {
        if (file != null) {
            WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, image), null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void runOnThread(Runnable method) {
        if (Platform.isFxApplicationThread()) {
            method.run();
        } else {
            Platform.runLater(method);
        }
    }

    public void setStatus(String text) {
        runOnThread(() -> {
            statusLine.setText(text);
        });
    }

    public void setMenuItemsDisabled(boolean status) {
        generatorMenu.setDisable(status);
    }

    public void setSaveMenuDisabled(boolean status) {
        save.setDisable(status);
    }

    public void setSaveAsMenuDisabled(boolean status) {
        saveAs.setDisable(status);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        runOnThread(() -> {
            this.canvas = canvas;
            canvasPane.setCenter(canvas);
        });
    }

    public void setDepth(int d) {
        depth = d;
    }

    public void setSeed(int s) {
        seed = s;
    }

    public void setHue(int h) {
        hue = h;
    }
}


