package gov.iti.jets;


import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.BorderPane;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert.*;
import java.util.concurrent.*;


//io
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.scene.control.*;

//stage
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;


public class App extends Application
{

    public static void main(String [] args)

    {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception
    {


        //adding image
        Image img=new Image("icon.png");

        Tab plusTab=new Tab(" + ");

        plusTab.setClosable(false);
        Tab t1=new Tab("Tab 1 ");
        t1=createNewTab("Tab 1 ",primaryStage);


        TabPane tabPane=new TabPane();
        tabPane.getTabs().add(t1);
        tabPane.getTabs().add(plusTab);


        plusTab.setOnSelectionChanged( (event)->
        {

            Tab newTab=new Tab();

            newTab= createNewTab("Tab "+String.valueOf(tabPane.getTabs().size())+" ",primaryStage);
            tabPane.getTabs().add(tabPane.getTabs().size()-1,newTab);
            tabPane.getSelectionModel().select(newTab);
        });


        Scene scene = new Scene(tabPane,500,600);

        primaryStage.setTitle("Notepad");
        primaryStage.getIcons().add(img);
        primaryStage.setScene(scene);
        primaryStage.show();



    }



    private Menu addFileMenu(TextArea textArea, Stage primaryStage)
    {

        Menu fileMenu = new Menu("File");
        MenuItem newItem=new MenuItem("New");
        MenuItem openItem=new MenuItem("Open");
        MenuItem saveItem=new MenuItem("Save");
        MenuItem exitItem=new MenuItem("Exit");

        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));


        fileMenu.getItems().addAll(newItem,openItem,saveItem,exitItem);

        ///file menu
        newItem.setOnAction( (event)->
        {

            textArea.clear();
        });
        //open
        openItem.setOnAction((event)->
        {
            FileChooser fc= new FileChooser();
            File file=fc.showOpenDialog(primaryStage);


            if (file != null)
            {
                try
                    (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
                {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        content.append(line).append("\n");
                    }
                    textArea.setText(content.toString());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });


        //save
        saveItem.setOnAction( (event)->
        {


            FileChooser file = new FileChooser();
            file.setTitle("Save");
            file.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.txt"));
            File f= file.showSaveDialog(primaryStage);

            if(file!=null)
            {
                try
                    (BufferedWriter writer = new BufferedWriter(new FileWriter(f)))
                {
                    writer.write(textArea.getText());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        });



        //exit
        exitItem.setOnAction( (event)->
        {

            primaryStage.close();
        });

        return fileMenu;

    }

    private Menu addEditMenu(TextArea textArea)
    {

        Menu editMenu = new Menu("Edit");
        MenuItem undoItem=new MenuItem("Undo");
        MenuItem redoItem=new MenuItem("Redo");
        MenuItem cutItem=new MenuItem("Cut");
        MenuItem copyItem=new MenuItem("Copy");
        MenuItem pasteItem=new MenuItem("Paste");
        MenuItem deleteItem=new MenuItem("Delete");
        MenuItem selectAllItem=new MenuItem("Select All");


        editMenu.getItems().addAll(undoItem,redoItem,cutItem,copyItem,pasteItem,deleteItem,selectAllItem);


        //clipBoard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();


        undoItem.setOnAction( (event)->
        {
            textArea.undo();
        } );
        redoItem.setOnAction( (event)->
        {
            textArea.redo();
        } );

        //adding event handling
        cutItem.setOnAction( (event)->
        {
            //cut
            String str=textArea.getSelectedText();
            clipboardContent.putString(str);
            clipboard.setContent(clipboardContent);
            textArea.replaceText(textArea.getSelection(),"");
        } );

        copyItem.setOnAction( (event)->
        {

            //copy
            String str=textArea.getSelectedText();
            clipboardContent.putString(str);
            clipboard.setContent(clipboardContent);

        });


        pasteItem.setOnAction( (event) ->
        {

            textArea.insertText(textArea.getCaretPosition(),clipboard.getString());
        });

        deleteItem.setOnAction( (event)->
        {

            textArea.deleteText(textArea.getSelection());
        });


        selectAllItem.setOnAction( (event)->
        {
            textArea.selectAll();
        });

        return editMenu;



    }

    private Menu addHelpMenu(TextArea textArea)
    {
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem=new MenuItem("About Notepad");

        helpMenu.getItems().addAll(aboutItem);


        // create a alert
        Alert a = new Alert(AlertType.INFORMATION);

        aboutItem.setOnAction( (event)->
        {


            a.setTitle("About Notepad");
            a.setHeaderText("About This Notepad Application");
            a.setContentText("This Notepad application is created by Leena Almekkawy\n\nFeatures:\n - Create: Start a new text document.\n - Open: Load an existing text file.\n - Save: Save your text document to a file.\n - Edit: Use Cut, Copy, Paste, Undo, and Redo functionalities for text manipulation.\n - Font Options: Choose from various font styles and sizes to customize your text.\n - Status Bar: View the current line number, column number, and character count dynamically.\n\nEnjoy using this lightweight Notepad!");
                             // show the dialog
                             a.show();
        });



        return helpMenu;


    }

    private Menu addFontMenu(TextArea textArea)
    {

        Menu fontMenu =new Menu("Font");
        MenuItem [] fontType=
        {
            new MenuItem("Times New Roman"),
            new MenuItem("Arial"),
            new MenuItem("Comic Sans MS"),
            new MenuItem("Courier New"),
            new MenuItem("Georgia"),
            new MenuItem("Verdana"),
            new MenuItem("Garamond"),
            new MenuItem("Consolas"),
            new MenuItem("Impact"),
            new MenuItem("Lucida Calligraphy")
        };

        for(MenuItem item:fontType)
        {
            item.setOnAction( (event)->
            {
                double si=textArea.getFont().getSize();
                textArea.setFont(Font.font(item.getText(),si));
            });
        }

        fontMenu.getItems().addAll(fontType);

        return fontMenu;

    }

    private Menu addSizeMenu(TextArea textArea)
    {

        Menu sizeMenu =new Menu("Size");


        MenuItem [] fontSize=new MenuItem[20];
        int ind=0;
        for(int i=5; i<=100; i+=5)
        {
            fontSize[ind]=new MenuItem(String.valueOf(i));
            ind++;
        }

        sizeMenu.getItems().addAll(fontSize);



        for(MenuItem item:fontSize)
        {
            item.setOnAction( (event)->
            {
                String fontFamily=textArea.getFont().getFamily();
                textArea.setFont(Font.font(fontFamily,Double.parseDouble(item.getText()) ) );
            });
        }

        return sizeMenu;


    }

    private Tab createNewTab(String tabTitle,Stage primaryStage)
    {


        //adding status bar
        HBox statusBar = new HBox();
        Label charNum= new Label();

        Label colNum= new Label();

        Label rowNum= new Label();
        charNum.setText(" | "+String.valueOf(0) +" characters");
        rowNum.setText("Ln "+String.valueOf(1));
        colNum.setText(", Col "+String.valueOf(1));


        statusBar.getChildren().add(rowNum);
        statusBar.getChildren().add(colNum);
        statusBar.getChildren().add(charNum);

        //adding text area
        TextArea textArea=new TextArea();



        //  Scene scene =new Scene(root,500,600);
        MenuBar menubar=new MenuBar();



        textArea.textProperty().addListener( (event)->
        {

            int caretPosition = textArea.getCaretPosition();
            //staus bar update
            String str=textArea.getText();
            int charCnt=0;
            int rowCnt=1;
            int colCnt=1;



            for(int i=0; i<caretPosition; i++)
            {
                if(str.charAt(i)=='\n')
                {
                    colCnt=1;
                    rowCnt++;
                }
                else
                {
                    colCnt++;
                }




            }

            for(int i=0; i<str.length(); i++)
            {
                if(str.charAt(i)!=' ' && str.charAt(i)!='\n')
                    charCnt++;
            }
            charNum.setText(" | "+String.valueOf(charCnt) +" characters");
            rowNum.setText("Ln "+String.valueOf(rowCnt));
            colNum.setText(", Col "+String.valueOf(colCnt));

            statusBar.getChildren().add(rowNum);
            statusBar.getChildren().add(colNum);
            statusBar.getChildren().add(charNum);



        });


        textArea.caretPositionProperty().addListener((obs, oldPos, newPos) ->
        {
            int caretPosition = textArea.getCaretPosition();
            //staus bar update
            String str=textArea.getText();
            int charCnt=0;
            int rowCnt=1;
            int colCnt=1;



            for(int i=0; i<caretPosition; i++)
            {
                if(str.charAt(i)=='\n')
                {
                    colCnt=1;
                    rowCnt++;
                }
                else
                {
                    colCnt++;
                }




            }

            for(int i=0; i<str.length(); i++)
            {
                if(str.charAt(i)!=' ' && str.charAt(i)!='\n')
                    charCnt++;
            }
            charNum.setText(" | "+String.valueOf(charCnt) +" characters");
            rowNum.setText("Ln "+String.valueOf(rowCnt));
            colNum.setText(", Col "+String.valueOf(colCnt));

            statusBar.getChildren().add(rowNum);
            statusBar.getChildren().add(colNum);
            statusBar.getChildren().add(charNum);



        });










        Tab tab=new Tab(tabTitle);


        BorderPane root=new BorderPane();

        root.setTop(menubar);
        root.setCenter(textArea);
        root.setBottom(statusBar);

        tab.setContent(root);

        menubar.getMenus().addAll(addFileMenu(textArea,primaryStage),addEditMenu(textArea),addHelpMenu(textArea),addFontMenu(textArea),addSizeMenu(textArea));

        return tab;


    }



}


