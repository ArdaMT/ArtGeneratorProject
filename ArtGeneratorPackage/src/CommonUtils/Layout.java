package CommonUtils;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 *  Helper class for creating several predefined controls.
 */
public class Layout {
    
    public static Border createDefaultBorder() {
        return new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT));
    }
    
    public static final GridPane createDefaultGrid() {
        
        GridPane grid = new GridPane();
        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setFillWidth(true);
        columnConstraint.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(columnConstraint);
        
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setFillHeight(true);
        rowConstraint.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(rowConstraint);

        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(5, 10, 10, 10));
        grid.setBorder(createDefaultBorder());
        
        return grid;
    }
 
    public static final Pane createGroup(String name, Pane content) {
        TitledPane pane = new TitledPane(name, content);
        pane.setCollapsible(false);
        pane.setAnimated(false);
        
        HBox box = new HBox(pane);
        HBox.setHgrow(pane, Priority.ALWAYS);
        return box;
    }
    
    public static final Label createCaption(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }
    
    public static void setAllVisible(boolean value, Control... controls) {
        for(Control c : controls) {
            c.setVisible(value);
        }
    }

}
