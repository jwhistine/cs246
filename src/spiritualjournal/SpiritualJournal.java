/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/********************************************************************
 * @author mormon
 *******************************************************************/
public class SpiritualJournal extends Application {
    private FileService fs;
    private Journal journal;
    private TextArea text;
    private ListView<String> list;
    private Entry e;
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
        fs = new FileService();
        journal = new Journal();
        text = new TextArea();
        
        HBox hBox = new HBox();
        GridPane grid1 = new GridPane();
        
        // This will set the alignement of the buttons to make them vertical
        VBox v1 = new VBox();
        Button open = new Button();
        open.setText("Open");
        open.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                File file = fc.showOpenDialog(primaryStage);
                String content = e.getContent();
                
                String filename = file.getPath();
                try {
                    journal.readFile(content, filename);
                } 
                catch (IOException ex) {
                    Logger.getLogger(SpiritualJournal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // makes the save button along with functionality
        Button save = new Button();
        save.setText("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc2 = new FileChooser();
                File file2 = fc2.showSaveDialog(primaryStage);
                String filename = file2.getPath();
                String content = text.getText();
                String date = text.getText();
                
                try {
                    journal.writeTextToFile(content, date, filename);
                } catch (IOException ex) {
                    Logger.getLogger(SpiritualJournal.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        // The export button for the XML file
        Button export = new Button();
        export.setText("Export");
        export.setOnAction(new EventHandler<ActionEvent>() {           
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                File file  = fc.showSaveDialog(primaryStage);
                
                String filename = file.getPath();
                
                try {
                    //fs.buildXML(list, filename);
                } catch (Exception ex) {
                    Logger.getLogger(SpiritualJournal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Import the XML file
        Button imports = new Button();
        imports.setText("Import");
        imports.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // allows the user to 
                FileChooser fc = new FileChooser();
                File file = fc.showOpenDialog(primaryStage);
                
                Thread t = new Thread();
                t.start();
                
                if (file != null) {
                    String filename = file.getPath();
                            
                    List<Entry> tmp;
                    try {
                        tmp = fs.readXML(filename);
                        String txt = "";
                        for (Entry entry : tmp) {
                            txt += entry.getDate() + "\n";
                            txt += entry.getContent() + "\n\n";
                        }
                        text.setText(txt);
                    } 
                    catch (InterruptedException ex) {
                        Logger.getLogger(SpiritualJournal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
        
        // setPadding(top, right, bottom, left)
        v1.setPadding(new Insets(30, 0, 0, 0));
        v1.setSpacing(7);
        v1.getChildren().addAll(save, open, export, imports);
       
        // sets up another vertical box
        VBox v2 = new VBox();
        text = new TextArea();
        text.setPrefWidth(385);
        text.setPrefHeight(125);
        text.setWrapText(true);
        
        // make the labels
        Label label = new Label("Entry: ");
       
        // this is a drop down menu
        ComboBox cb = new ComboBox();
        cb.setPromptText("Search By:");
        cb.getItems().addAll("Topic", "Scripture", "Date");
        
        BorderPane bp = new BorderPane();
        bp.setLeft(cb);
        
        // make a list box
        list = new ListView<String>();
        list.setPrefSize(25, 75);
 
        // setPadding(top, right, bottom, left)
        v2.setPadding(new Insets(10, 10, 10, 10));
        v2.setSpacing(5);
        v2.getChildren().addAll(label, text, bp, list);
        
        // this sets up all of the aligment
        hBox.getChildren().addAll(v2, v1);
        grid1.getChildren().add(hBox);
        
        Scene scene = new Scene(grid1, 475, 300, Color.rgb(0, 128, 128));
        
        primaryStage.setTitle("Spiritual Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    } 
    
    
}