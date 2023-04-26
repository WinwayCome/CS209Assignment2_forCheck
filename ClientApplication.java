package com.winway.onlinechat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application
{
    
    public void run()
    {
        launch();
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("login.fxml"));
        LoginController controller = new LoginController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent ->
        {
            controller.client.close();
            stage.close();
            System.exit(1);
        });
        stage.show();
    }
}
