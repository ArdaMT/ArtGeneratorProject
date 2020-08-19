/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

import CommonUtils.GenTextField;
import Generators.RandomFunctionTree.Functions.FunctionGenerator;
import MainWindow.Application;
import MainWindow.MainStatus;
import MainWindow.MainWindow;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.util.Random;

/**
 * This class implements the GUI elemends and parts of the logic of random function tree algorithm to produce
 * colourful images of different shapes
 *
 * @author Ardamelih
 */
public class RandomFunctionTreeGenerator extends Generators.Generator {
    private Label lblExplanation;
    private int minDepth;
    private int maxDepth;
    private int depth = -1;
    private int seed;
    private int hue = -1;
    private Label lblMinDepth;
    private Label lblMaxDepth;
    private Label lblHue;
    private Label lblSeed;
    private Label lblDepth;
    private GenTextField tfMinDepth;
    private GenTextField tfMaxDepth;
    private GenTextField tfHue;
    private TextField tfSeed;
    private GenTextField tfDepth;
    private Button automaticGeneratorButton;
    private Random rn;
    private CheckBox customImageBox;
    public static FunctionGenerator functionGenerator;
    public Node root;

    public RandomFunctionTreeGenerator(Canvas canvas, String name) {
        super(name, canvas);
        this.setTitle(name);
        rn = new Random(8);
        setStopParameters();
        initLabels();
        initTextFields();
        initButtons();
        setGridLayout();
        Scene scene = new Scene(grid);
        setScene(scene);
    }

    private void initLabels() {
        lblExplanation=new Label("This program generates colourful images and shapes.   " +
                "\nLower values for depth produce more interesting results.  " +
                "\nYou can generate a single image or a series of 9 images.  " +
                "\nOnce the images appear on the screen, you can select   " +
                "\none and save it. If you choose the \"save as\" option,  " +
                "\nprogram suggests this file name: depth_seed_hue.png.   " +
                "\nThe metrics from the file name can be used to recreate     " +
                "\nthe same image, when generating a custom image. ");
        lblExplanation.setFont(Font.font("verdana",12));
        lblHeight = new Label("Canvas Height (50-800): ");
        lblHeight.setFont(Font.font("verdana",12));
        lblWidth = new Label("Canvas Width (50-800): ");
        lblWidth.setFont(Font.font("verdana",12));

        lblMinDepth = new Label("Minimum Depth (1-4): ");
        lblMinDepth.setFont(Font.font("verdana",12));

        lblMaxDepth = new Label("Maximum Depth (2-5): ");
        lblMaxDepth.setFont(Font.font("verdana",12));

        lblHue = new Label("Colour Hue (0-360): ");
        lblHue.setFont(Font.font("verdana",12));
        lblDepth = new Label("Depth  (1-5): ");
        lblDepth.setFont(Font.font("verdana",12));
        lblSeed = new Label("Seed : ");
        lblSeed.setFont(Font.font("verdana",12));
    }

    private void initTextFields() {
        tfHeight = new GenTextField();
        tfHeight.setMaxWidth(70);
        tfHeight.setText("400");
        tfWidth = new GenTextField();
        tfWidth.setMaxWidth(70);
        tfWidth.setText("400");
        tfMinDepth = new GenTextField();
        tfMinDepth.setMaxWidth(70);
        tfMinDepth.setText("1");
        tfMaxDepth = new GenTextField();
        tfMaxDepth.setMaxWidth(70);
        tfMaxDepth.setText("2");
        tfSeed = new TextField();
        tfSeed.setMaxWidth(70);
        tfSeed.setDisable(true);
        tfHue = new GenTextField();
        tfHue.setMaxWidth(70);
        tfDepth = new GenTextField();
        tfDepth.setMaxWidth(70);
        tfDepth.setDisable(true);
        tfDepth.setText("2");
    }
    
    private void initButtons() {
        customImageBox = new CheckBox("Create a custom image");
        customImageBox.setFont(Font.font("Verdana", 12));
        generateButton = new Button("Generate");
        generateButton.setFont(Font.font("Verdana", 14));
        generateButton.setTextFill(Color.DARKBLUE);
        automaticGeneratorButton = new Button("Generate series of Images");
        automaticGeneratorButton.setFont(Font.font("Verdana", 14));
        automaticGeneratorButton.setTextFill(Color.DARKBLUE);
        stopButton = new Button("Stop");
        stopButton.setFont(Font.font("Verdana", 14));
        stopButton.setTextFill(Color.DARKBLUE);
        //Event Handler for the buttons
        generateButton.setOnAction(e -> {
            e.consume();
            generate();
        });
        automaticGeneratorButton.setOnAction(e -> automaticGenerate()
        );
        customImageBox.setOnAction(e -> {
            if (customImageBox.isSelected()) {
                tfMinDepth.setDisable(true);
                tfMaxDepth.setDisable(true);
                tfSeed.setDisable(false);
                tfDepth.setDisable(false);
            }
            else {
                tfSeed.setDisable(true);
                tfDepth.setDisable(true);
                tfMinDepth.setDisable(false);
                tfMaxDepth.setDisable(false);
            }
        });
    }

    @Override
    protected void generate() {
        clearCanvas(canvas);
        setStopParameters();
        Application.getMainWindow().setStatus(name + " " + MainStatus.GENERATE.toString());
        Application.getMainWindow().setSaveMenuDisabled(false);
        MainWindow.saveAs.setDisable(false);
        if (isInputValid()) {
            //compute depth using the min and max.  values for depth, if the image is not customised.
            if (!customImageBox.isSelected()) {
                depth = computeDepth(minDepth, maxDepth);
            }
            createTree(seed, depth);
            computeImage(canvas.getHeight(), canvas.getWidth());
            Application.getMainWindow().setStatus(name + " " + MainStatus.FINISHED.toString());
            Application.getMainWindow().setDepth(depth);
            Application.getMainWindow().setSeed(seed);
            Application.getMainWindow().setHue(hue);
        } else {
            setAlwaysOnTop(true);
        }
    }

    //generate 9 automatic images
    private void automaticGenerate() {
        if(customImageBox.isSelected()){
            JOptionPane.showMessageDialog(null, "Please uncheck the \"Create a custom image\" checkbox first.");
            setAlwaysOnTop(true);

        }
       else{ if (isInputValid()) {
            clearCanvas(canvas);
            setStopParameters();
            Application.getMainWindow().setStatus(name + " " + MainStatus.GENERATE.toString());
            Application.getMainWindow().setSaveMenuDisabled(false);

            MainWindow.saveAs.setDisable(false);
            //each miniature image has a size of 200x200. there is an empty space of 10 pixel  between the images
            canvas.setWidth(620);
            canvas.setHeight(620);
            setSeedDeptHueForNine();
        } else {
            setAlwaysOnTop(true);
        }}
    }

    private void setSeedDeptHueForNine() {
        int[] seedArray = new int[9];
        int[] depthArray = new int[9];
        int[] hueArray = new int[9];
        int a = 5;
        for (int i = 0; i < seedArray.length; i++) {
            seedArray[i] = Math.abs((int) System.currentTimeMillis() * a);
            rn = new Random(seedArray[i]);
            a += 1;
            depthArray[i] = rn.nextInt(maxDepth - minDepth+1) + minDepth;
            if (isEmpty(tfHue)) {
                hueArray[i] = rn.nextInt(360);
            } else {
                hueArray[i] = hue;
            }
        }
        createSeriesOfNine(seedArray,depthArray,hueArray);
        //get the coordinates of the mouse click on the image series
        canvas.setOnMouseClicked((MouseEvent t) -> {
            int x = (int) t.getX();
            int y = (int) t.getY();
            //determine the image matching the above coordinates
            int imageIndex = chooseImage(x, y);
            seed = seedArray[imageIndex];
            depth = depthArray[imageIndex];
            hue = hueArray[imageIndex];
            //enlarge the chosen image
            canvas.setWidth(400);
            canvas.setHeight(400);
            createTree(seed, depth);
            computeImage(canvas.getHeight(), canvas.getWidth());
            Application.getMainWindow().setStatus(name + " " + MainStatus.FINISHED.toString());
            Application.getMainWindow().setDepth(depth);
            Application.getMainWindow().setSeed(seed);
            Application.getMainWindow().setHue(hue);
            canvas.setOnMouseClicked(null);
        });
    }

    private void createSeriesOfNine(int[] seedArray, int[]depthArray, int[]hueArray) {
        OffsetPair o;
        for (int i = 0; i < seedArray.length; i++) {
            switch (i) {
                case 0:
                    o=new OffsetPair(0,0);
                    break;
                case 1:
                    o=new OffsetPair(210,0);
                    break;
                case 2:
                    o=new OffsetPair(420,0);
                    break;
                case 3:
                    o=new OffsetPair(0,210);
                    break;
                case 4:
                    o=new OffsetPair(210,210);
                    break;
                case 5:
                    o=new OffsetPair(420,210);
                    break;
                case 6:
                    o=new OffsetPair(0,420);
                    break;
                case 7:
                    o=new OffsetPair(210,420);
                    break;
                default:
                    o=new OffsetPair(420,420);
            }
            //set the current seed and depth values;
            seed = seedArray[i];
            depth = depthArray[i];
            hue = hueArray[i];
            createTree(seed, depth);
            computeImage(200, 200, o.getOffSetX(), o.getOffSetY());
        }
    }



    private void createTree(int seed, int depth) {
        functionGenerator = new FunctionGenerator(seed);
        root = new Node(functionGenerator.getRandomFunction());
        root.createChildren(depth - 1);
    }

    private void computeImage(double height, double width) {
        computeImage(height, width, 0, 0);
    }

    private void computeImage(double height, double width, int offsetX, int offsetY) {
        System.out.println("depth: " + depth + " seed: " + seed + " hue: " + hue);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //initialise the root and start building the random function tree
                double result = root.calculate(j / width, i / height,
                        (j / width + i / height) / 2);
                if (result < 0) {
                    System.out.println("small");
                }
                if (result > 1) {
                    System.out.println("big");
                }
                Color color = Color.hsb(hue, 0.5, result);
                gc.setFill(color);
                gc.fillRect(j + offsetX, i + offsetY, 1, 1);
            }
        }
    }

    private void setGridLayout() {
        grid = new GridPane();
        GridPane.setConstraints(lblExplanation, 1, 0, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblWidth, 1, 2, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfWidth, 1, 2, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblHeight, 1, 3, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHeight, 1, 3, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblMinDepth, 1, 4, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfMinDepth, 1, 4, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblMaxDepth, 1, 5, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfMaxDepth, 1, 5, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblHue, 1, 6, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfHue, 1, 6, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(customImageBox, 1, 7, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(lblSeed, 1, 8, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfSeed, 1, 8, 1, 1,
                HPos.RIGHT, VPos.BASELINE);
        GridPane.setConstraints(lblDepth, 1, 9, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(tfDepth, 1, 9, 1, 1,
                HPos.RIGHT, VPos.BASELINE);

        GridPane.setConstraints(generateButton, 1, 10, 1, 1,
                HPos.LEFT, VPos.BASELINE);
        GridPane.setConstraints(automaticGeneratorButton, 1, 10, 1, 1,
                HPos.RIGHT, VPos.BASELINE);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().addAll(lblExplanation,lblWidth, tfWidth, lblHeight, tfHeight, lblMinDepth, tfMinDepth, lblMaxDepth,
                tfMaxDepth, lblSeed, tfSeed, generateButton, automaticGeneratorButton,
                lblHue, tfHue, customImageBox, lblDepth, tfDepth);
    }


    //check the validity of the input values for the textfields.
    private boolean isInputValid() {
        if (!isHeightInputValid()) {
            return false;
        }

        if (!isWidthInputValid()) {
            return false;
        }

        if (customImageBox.isSelected()) {
            if (!isSeedInputValid()) {
                return false;
            }
            if (!isDepthInputValid()) {
                return false;
            }

        } else {
            seed = (int) System.currentTimeMillis();
            if (!isMinDepthValid()) {
                return false;
            }
            if (!isMaxDepthValid()) {
                return false;
            }
            if(minDepth>=maxDepth){
                JOptionPane.showMessageDialog(null, "Invalid input! Minimum depth must be smaller than the maximum depth.");
                return false;
            }
        }

        //if no hue was selected, a random hue will be assigned.
        if (isEmpty(tfHue)) {
            if (rn == null) {
                rn = new Random();
            }
            hue = rn.nextInt(360);

        } else {
            hue = Integer.parseInt(tfHue.getText()) % 360;
        }
        return true;
    }

    private boolean isHeightInputValid() {
        if (isEmpty(tfHeight)) {
            JOptionPane.showMessageDialog(null, "Height cannot be empty! Please enter a number between 50 and 800 for this field.");
            return false;
        } else {
            canvas.setHeight(Integer.parseInt(tfHeight.getText()));
        }
        if ((canvas.getHeight() < 50) || (canvas.getHeight() > 800)) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 50 and 800 for the height.");
            return false;
        }
        return true;
    }

    private boolean isWidthInputValid() {
        if (isEmpty(tfWidth)) {
            JOptionPane.showMessageDialog(null, "Width cannot be empty! Please enter a number between 50 and 800 for this field.");
            return false;
        } else {
            canvas.setWidth(Integer.parseInt(tfWidth.getText()));
        }
        if ((canvas.getWidth() < 50) || (canvas.getWidth() > 800)) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 50 and 800 for the width.");
            return false;
        }
        return true;
    }

    private boolean isMinDepthValid() {
        if (isEmpty(tfMinDepth)) {
            JOptionPane.showMessageDialog(null, "Minimum depth cannot be empty! Please enter a " +
                    "\nnumber  between 1 and 4 for this field.");
            return false;
        } else {
            minDepth = Integer.parseInt(tfMinDepth.getText());
        }
        if (minDepth < 1 || minDepth > 4) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between" +
                    "\n1 and 4 for minimum depth.");
            return false;
        }
        return true;
    }

    private boolean isMaxDepthValid() {
        if (isEmpty(tfMaxDepth)) {
            JOptionPane.showMessageDialog(null, "Maximum depth cannot be empty! Please enter a " +
                    "\nnumber  between 2 and 5 for this field.");
            return false;
        } else {
            maxDepth = Integer.parseInt(tfMaxDepth.getText());
        }
        if (maxDepth < 2 || maxDepth > 5) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between" +
                    "\n2 and 5 for maximum depth.");
            return false;
        }
        return true;
    }

    private boolean isSeedInputValid() {
        if (tfSeed.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seed cannot be empty! Please enter an integer number for this field.");
            return false;
        }
        try {
            seed = Integer.parseInt(tfSeed.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter an integer number for the seed.");
            return false;
        }
        return true;
    }

    private boolean isDepthInputValid() {
        if (tfDepth.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Depth cannot be empty! Please enter a " +
                    "\nnumber between 1 and 5 for this field.");
            return false;
        }
        try {
            depth = Integer.parseInt(tfDepth.getText());
            if (depth == 0) {
                JOptionPane.showMessageDialog(null, "Depth cannot be zero! Please enter a " +
                        "\nnumber between 1 and 5 for this field.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 1 and 5 for depth.");
            return false;
        }
        return true;
    }

    //computes depth from a given minimum and maximum depth
    private int computeDepth(int mindep, int maxdep) {
        if (rn == null) {
            rn = new Random();
        }
        return rn.nextInt(maxdep - mindep+1) + mindep;
    }
    //locates the x and y coordinates of a mouse click on the selected image among the series of 9 images.
    private int chooseImage(int x, int y) {
        if (y < 200) {
            if (x < 200) {
                return 0;
            } else if (x < 410) {
                return 1;
            } else {
                return 2;
            }
        } else if (y < 410) {
            if (x < 200) {
                return 3;
            } else if (x < 410) {
                return 4;
            } else {
                return 5;
            }
        } else {
            if (x < 200) {
                return 6;
            } else if (x < 410) {
                return 7;
            } else {
                return 8;
            }
        }
    }

}
