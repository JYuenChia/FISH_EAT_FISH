/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.githubtry;

/**
 *
 * @author Jing Yuen
 */
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class FishEatFishGame extends Application {
 abstract class Fish {
        Image image;
        double x, y;
        double speed;
        double size;

        Fish(String filePath, double size, double speed) {
            this.image = new Image("file:" + filePath); // Use the absolute file path
            this.x = 800;
            this.y = new Random().nextInt(600 - (int) size);
            this.size = size;
            this.speed = speed;
        }

        void move() {
            x -= speed;
            if (x < -size) {
                x = 800;
                y = new Random().nextInt(600 - (int) size);
            }
        }

        void draw(GraphicsContext gc) {
            gc.drawImage(image, x, y, size, size);
        }
    }

    // EnemyFish class inherits from Fish
    class EnemyFish extends Fish {
        boolean moveLeft;

        EnemyFish(String filePath, double size, double speed, boolean moveLeft) {
            super(filePath, size, speed);
            this.moveLeft = moveLeft;
            if (!moveLeft) {
                this.x = -size;
            }
        }

        @Override
        void move() {
            if (moveLeft) {
                super.move();
            } else {
                x += speed;
                if (x > 800 + size) {
                    x = -size;
                    y = new Random().nextInt(600 - (int) size);
                }
            }
        }


    }

    // PlayerFish class
    class PlayerFish extends Fish {
        double growthRate = 1.02; // Slower growth multiplier

        PlayerFish(String filePath, double size, double speed) {
            super(filePath, size, speed);
            this.x = 100; // Starting x position for the player
            this.y = 300; // Centered y position for the player
        }

        void grow() {
            size *= growthRate;
        }

        @Override
        void move() {
            // Player movement will be controlled by user input (to be implemented)
        }
    }

    private ArrayList<EnemyFish> enemyFishList;
    private Canvas canvas;
    private PlayerFish playerFish;
    private int level = 1;
    private int score = 0;
    private Timeline gameLoop;

    private boolean checkCollision(Fish player, Fish enemy) {
        double distance = Math.sqrt(Math.pow(player.x - enemy.x, 2) + Math.pow(player.y - enemy.y, 2));
        return distance < (player.size * 0.4 + enemy.size * 0.4); // Adjusted collision radius
    }

    private void spawnFish(String filePath, double size, double speed, boolean moveLeft, int count) {
        for (int i = 0; i < count; i++) {
            enemyFishList.add(new EnemyFish(filePath, size, speed, moveLeft));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }public void start(Stage primaryStage) throws Exception {
        
        Stage stage = new Stage();
        int totalPoint = 0;
        int numFish = 0;
    
        
        Pane root = new Pane();
        Scene scene = new Scene(root,800,450,Color.SKYBLUE);
        
        Image icon = new Image("file:C:/Users/weiyi/Documents/2444557-1/24004557/icon.png");
        stage.getIcons().add(icon);
//        stage.setTitle("The FISH and FISH game");
//        stage.setWidth(800);
//        stage.setHeight(600);
        stage.setResizable(false);
//        stage.setFullScreen(true);
        Text textPoint = new Text();
        textPoint.setText("Point : " +totalPoint); 
        textPoint.setX(50);
        textPoint.setY(50);
//        Scene scene2 = new Scene(root,800,600,Color.SKYBLUE);
        Text textFish = new Text();
        textFish.setText("Number of Fish :" + numFish);
        textFish.setX(50);
        textFish.setY(70);

        Image imageBG = new Image("file:C:\\Users\\weiyi\\Downloads\\Telegram Desktop\\background.png");
        ImageView imageViewBG = new ImageView(imageBG);
        imageViewBG.setFitWidth(800);
        imageViewBG.setFitHeight(450);
        imageViewBG.setPreserveRatio(true);
        imageViewBG.setX(0);
        imageViewBG.setY(0);
//         Scene scene2 = new Scene(root2);
      
       
        
        
        
        
        
        canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);

        enemyFishList = new ArrayList<>();

        // Initialize enemy fish for Level 1
        spawnFish("C:\\small_fish.png", 160, 2, true, 5); // Initial small fish

        // Initialize player fish
        playerFish = new PlayerFish("C:\\player_fish.png", 200, 0); // Player's fish

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Draw and move player fish
            playerFish.draw(gc);

            // Draw and move enemy fish
            for (int i = 0; i < enemyFishList.size(); i++) {
                EnemyFish fish = enemyFishList.get(i);
                fish.move();
                fish.draw(gc);

                // Check for collision with player fish
                if (checkCollision(playerFish, fish)) {
                    if (playerFish.size >= fish.size) {
                        enemyFishList.remove(fish);
                        score++;
                        playerFish.grow();
                        break;
                    } else {
                        System.out.println("Game Over!");
                        gameLoop.stop();
                        return;
                    }
                }
            }

            // Adjust fish population based on score and level
            if (score >= 5 && level == 1) {
                spawnFish("C:\\medium_fish.png", 300, 3, false, 3); // Medium fish, more fish spawn
                level++;
            } else if (score >= 15 && level == 2) {
                spawnFish("C:\\big_fish.png", 400, 4, true, 2); // Big fish, more fish spawn
                level++;
            } else if (score >= 30 && level == 3) {
                spawnFish("C:\\giant_fish.png", 500, 5, true, 1); // Giant fish, more fish spawn
                level++;
            }
        }));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();

        root.getChildren().add(imageViewBG);
        root.getChildren().add(textPoint);
        root.getChildren().add(textFish);
        
        
        stage.setScene(scene);
        stage.setTitle("Fish Eat Fish Game");
        stage.show();
    }


}
