package org.ventanamemoria;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class VentanaMemoria extends Application {

    // Model
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty width = new SimpleDoubleProperty();
    private DoubleProperty height = new SimpleDoubleProperty();
    private IntegerProperty red = new SimpleIntegerProperty();
    private IntegerProperty blue = new SimpleIntegerProperty();
    private IntegerProperty green = new SimpleIntegerProperty();

    @Override
    public void stop() throws Exception {
        System.out.println("Cerrando ventana.");

        File profileDir = new File(System.getProperty("user.home"));
        File configDir = new File(profileDir, ".VentanaConMemoria");
        File configFile = new File(configDir, "config.properties");

        if (!configDir.exists()) {
            configDir.mkdir();
        }

        FileOutputStream fos = new FileOutputStream(configFile);

        Properties props = new Properties();
        props.setProperty("size.width", "" + width.getValue());
        props.setProperty("size.height", "" + height.getValue());
        props.setProperty("location.x", "" + x.getValue());
        props.setProperty("location.y", "" + y.getValue());
        props.setProperty("background.red", "" + red.getValue());
        props.setProperty("background.blue", "" + blue.getValue());
        props.setProperty("background.green", "" + green.getValue());

        props.store(fos, "Estado de la ventana");
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Iniciando ventana");

        File profileDir = new File(System.getProperty("user.home"));
        File configDir = new File(profileDir, ".VentanaConMemoria");
        File configFile = new File(configDir, "config.properties");

        if (configFile.exists()) {

            FileInputStream fis = new FileInputStream(configFile);

            Properties props = new Properties();
            props.load(fis);

            width.set(Double.parseDouble(props.getProperty("size.width", "320")));
            height.set(Double.parseDouble(props.getProperty("size.height", "200")));
            x.set(Double.parseDouble(props.getProperty("location.x", "0")));
            y.set(Double.parseDouble(props.getProperty("location.y", "0")));

            red.set(Integer.parseInt(props.getProperty("background.red", "0")));
            green.set(Integer.parseInt(props.getProperty("background.green", "0")));
            blue.set(Integer.parseInt(props.getProperty("background.blue", "0")));
        } else {
            width.set(320);
            height.set(200);
            x.set(0);
            y.set(0);
            red.set(0);
            blue.set(0);
            green.set(0);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Sliders
        Slider redSlider = new Slider(0, 255, 0);
        Slider greenSlider = new Slider(0, 255, 0);
        Slider blueSlider = new Slider(0, 255, 0);

        // Labels
        Label redLabel = new Label("Rojo");
        Label greenLabel = new Label("Verde");
        Label blueLabel = new Label("Azul");

        // HBoxes para agrupar Labels y Sliders
        HBox redBox = new HBox(10, redLabel, redSlider);
        HBox greenBox = new HBox(10, greenLabel, greenSlider);
        HBox blueBox = new HBox(10, blueLabel, blueSlider);

        // VBox para el layout
        VBox root = new VBox(10, redBox, greenBox, blueBox);
        root.setFillWidth(false);
        root.setAlignment(Pos.CENTER);

        // Color de fondo inicial
        Color color = Color.rgb(red.intValue(), green.intValue(), blue.intValue());
        root.setBackground(Background.fill(color));

        Scene scene = new Scene(root, width.get(), height.get());

        primaryStage.setX(x.get());
        primaryStage.setY(y.get());
        primaryStage.setTitle("Ventana con memoria");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Bindings
        x.bind(primaryStage.xProperty());
        y.bind(primaryStage.yProperty());
        width.bind(primaryStage.widthProperty());
        height.bind(primaryStage.heightProperty());

        // Sliders del color
        redSlider.valueProperty().bindBidirectional(red);
        greenSlider.valueProperty().bindBidirectional(green);
        blueSlider.valueProperty().bindBidirectional(blue);

        // Cambiar el color de fondo cuando se cambian los valores de los sliders
        red.addListener((o, ov, nv) -> {
            Color c = Color.rgb(nv.intValue(), green.intValue(), blue.intValue());
            root.setBackground(Background.fill(c));
        });

        green.addListener((o, ov, nv) -> {
            Color c = Color.rgb(red.intValue(), nv.intValue(), blue.intValue());
            root.setBackground(Background.fill(c));
        });

        blue.addListener((o, ov, nv) -> {
            Color c = Color.rgb(red.intValue(), green.intValue(), nv.intValue());
            root.setBackground(Background.fill(c));
        });
    }
}