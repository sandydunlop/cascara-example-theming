package io.github.qishr.cascara.example.theming;

import io.github.qishr.cascara.ui.control.OptionChooser;
import io.github.qishr.cascara.ui.theme.ThemeEngine;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {
    private final ThemeEngine themeEngine = ThemeEngine.instance();
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        OptionChooser themeChooser = new OptionChooser(
            themeEngine.getThemeOptionProvider(),
            themeEngine.getDefaultThemeOption()
        );

        themeChooser.getSelectionModel().selectedItemProperty().addListener((obs, old, theme) -> {
            themeEngine.setTheme(theme);
            themeEngine.applyTheme(scene);
        });

        HBox choserBox = new HBox(16, new Label("Theme:"), themeChooser);
        // choserBox.setPadding(new Insets(16));
        choserBox.setAlignment(Pos.CENTER);

        Samples samples = new Samples();

        VBox layout = new VBox();
        layout.setSpacing(8);
        layout.setPadding(new Insets(16));
        layout.getChildren().addAll(choserBox, samples.getView());

        scene = new Scene(layout, 800, 500);

        themeEngine.applyTheme(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

        String title = String.format(
            "Cascara Themeing Demo (%s)",
            ThemeEngine.class.getModule().getDescriptor().toNameAndVersion()
        );

        primaryStage.setTitle(title);
    }

    @Override
    public void stop() throws Exception {
        // When the OptionProvider is used, the theme engine uses a
        // background thread to watch the theme directory for updates.
        // We close it here to allow the app to close cleanly.
        themeEngine.close();
    }
}

