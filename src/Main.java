/*********************************************************************************************************************
 *********************************************************************************************************************
 *****  Class: CSC-360-001-2019-040    Semester: Summer 2019    Professor: Richard Fox    Student: Ryan Huffman  *****
 *****-----------------------------------------------------------------------------------------------------------*****
 *****                                       Programming Assignment #3                                           *****
 *****___________________________________________________________________________________________________________*****
 *****                                                                                                           *****
 *****                                        The game of Breakout!                                              *****
 *********************************************************************************************************************
 *********************************************************************************************************************/

// IMPORTS of needed tools and plug-ins
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;


public class Main extends Application {

    // CLASS VARIABLE(s) declaration(s)
    private Circle ball;
    private double bx, by, bdx, bdy;
    private Rectangle paddle;
    private double px, pdx;
    private int score = 0, lives = 5, totalBricks = 80;
    private Text descriptionScore;
    private Text scoreValue;
    private Text descriptionLives;
    private Text livesValue;
    private Text pauseText;
    private Brick[][] bricks;
    private Pane pane2;
    private Timeline timer;
    private Random randomGenerator;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage){

        // Initialize the Random variable
        randomGenerator = new Random();

        // Create and Initialize the Pane, Set the Pane's Preferred Width and Height, Add the Text objects to the Pane
        Pane pane1 = new Pane();
        pane1.setPrefSize(600, 0);

        // Create and Initialize four Lines to outline the playing field
        Line leftLine = new Line(20, 50, 20, 580);
        Line rightLine = new Line(580, 50, 580, 580);
        Line topLine = new Line(20, 50, 580, 50);
        Line bottomLine = new Line(20, 580, 580, 580);

        // Initialize bx and by
        bx= 300;
        by = 250;

        // Initialize the Circle ball
        ball = new Circle(bx, by, 10);

        // Initialize px, pdx and the Rectangle paddle
        px = 275;
        pdx = 0;
        paddle = new Rectangle(px, 550, 55, 20);

        // Initialize the Pane
        pane2 = new Pane();

        // Method call to Set the value of the Strings of the Text objects and Add them to the Pane
        replaceText(pane1, pane2);

        // Add the ball, paddle and the four playing field outline Lines to the Pane
        pane2.getChildren().add(ball);
        pane2.getChildren().add(paddle);
        pane2.getChildren().add(leftLine);
        pane2.getChildren().add(rightLine);
        pane2.getChildren().add(topLine);
        pane2.getChildren().add(bottomLine);

        // Variables for use in creating Brick objects for the Brick 2D array
        Color color = null;
        int value = 0;
        int yCor = 0;
        int xCor = 0;
        // Initialize the Brick, bricks, 2D array
        bricks = new Brick[4][20];

        // 2D for loop to get and set all the values to create all the Brick objects for the bricks 2D array
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 20; j++){
                // X and Y-coordinates
                yCor = ((i * 10) + 50);
                xCor = ((j * 28) + 20);

                // Assign Colors based on which Row of Brick objects are in and set the value they are worth when hit
                // by the Circle ball
                if(i == 0){
                    color = Color.RED;
                    value = 50;
                } else if(i == 1){
                    color = Color.GREEN;
                    value = 30;
                } else if(i == 2){
                    color = Color.BLUE;
                    value = 20;
                } else if(i == 3){
                    color = Color.PURPLE;
                    value = 10;
                }

                // Create the Brick objects to fill up the 2D arrays
                bricks[i][j] = new Brick(xCor, yCor, color, value);
                // Draw the Brick objects in the Pane pane2
                bricks[i][j].draw(pane2);
            }
        }

        // Create and Initialize the VBox that holds both Panes pane1 and pane2
        VBox mainPane = new VBox();
        mainPane.getChildren().add(pane1);
        mainPane.getChildren().add(pane2);

        // Create and Initialize the Scene
        Scene scene = new Scene(mainPane, 600, 625);

        // Randomly get the initial horizontal direction and velocity of the Circle ball is left or right
        int direction = randomGenerator.nextInt(2);
        bdx = (randomGenerator.nextInt(3) + 1);
        if(direction == 1){
            bdx *= -1;
        }

        // Randomly get the initial vertical velocity of the Circle ball
        bdy = (randomGenerator.nextInt(3) + 1);

        // Animation EventHandler
        timer = new Timeline(new KeyFrame(Duration.millis(15), e-> {
            // remove the old ball and paddle
            pane2.getChildren().remove(ball);
            pane2.getChildren().remove(paddle);

            // This line checks if the ball hits the left or right side of the playing field and reverse direction if so
            bdx *= (bx >= 575) || (bx <= 30) ? -1 : 1;

            // This line checks if the ball hits the top side of the playing field and reverse direction if so
            bdy *= by <= 60 ? -1 : 1;

            // Move the ball at the associated current Velocity
            bx += bdx;
            by += bdy;

            if(((bx >= 20) && (bx <= 580)) && ((by <= 580) && (by >= 50))){
                // Create a new ball (Circle)
                ball = new Circle(bx, by, 10);
                // Randomly Set the Color of the Circle ball every time a new frame of animation occurs
                ball.setFill(Color.rgb(randomGenerator.nextInt(200), randomGenerator.nextInt(200),
                        randomGenerator.nextInt(200), 1.0));
                // Add the new ball (Circle) to the Pane
                pane2.getChildren().add(ball);
            }

            // if block to check if the paddle has almost hit either the left or right wall and
            // stop the paddle if it has
            if(px < 20){
                px = 20;
                pdx = 0;
            } else if(px > 525){
                px = 525;
                pdx = 0;
            }

            // Move the paddle at the associated current Velocity
            px += pdx;

            // Create a new paddle (Rectangle) and add it to the Pane
            paddle = new Rectangle(px, 550, 55, 10);
            pane2.getChildren().add(paddle);

            // 2D for loop to check if any of the Brick objects have been collided with. If so, "remove" that Brick,
            // add the value of that Brick to the score, remove the old score and display the new score,
            // subtract from the total number of Brick objects every time a ball and brick collision happens and
            // once the total number of Brick objects equals zero, Stop the animation and call the wonGame() method
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 20; j++){
                    if(bricks[i][j].collides(bx, by)){
                        bricks[i][j].remove(pane2);
                        score += bricks[i][j].getValue();
                        pane1.getChildren().remove(scoreValue);
                        scoreValue = new Text(75, 40, String.valueOf(score));
                        pane1.getChildren().add(scoreValue);
                        totalBricks--;

                        if(totalBricks == 0){
                            timer.stop();
                            wonGame(pane1, pane2);
                        }
                    }
                }
            }

            // If block to check to see if the ball collided with the paddle
            if(by == 540){
                // if block to see if the ball collided with the far left side of the top of the paddle or
                // up 5 pixels to the right of the far left side of the top of the paddle
                // This if blocks will take the value of bdx and subtract 3 from it up to a
                // minimum bdx value of -3
                if((bx >= px) && (bx < (px + 5))){
                    // if/else block to make sure the value of bdx doesn't drop below -3
                    if(bdx >= 0){
                        bdx -= 3;
                    } else {
                        bdx = -3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 5 pixels to the right of the far left side
                // of the top of the paddle or up to 14 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks will take the value of bdx and subtract 2 from it up to a minimum
                // bdx value of -3
                else if((bx >= (px + 5)) && (bx < (px + 15))){
                    // if/else block to make sure the value of bdx doesn't drop below -3
                    if(bdx >= -1){
                        bdx -= 2;
                    } else {
                        bdx = -3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 15 pixels to the right of the far left side
                // of the top of the paddle or up to 24 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks will take the value of bdx and subtract 1 from it up to a minimum
                // bdx value of -3
                else if((bx >= (px + 15)) && (bx < (px + 25))){
                    // if/else block to make sure the value of bdx doesn't drop below -3
                    if(bdx >= -2){
                        bdx -= 1;
                    } else {
                        bdx = -3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 25 pixels to the right of the far left side
                // of the top of the paddle or up to 29 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks does not change the value of bdx
                else if((bx >= (px + 25)) && (bx < (px + 30))){
                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 30 pixels to the right of the far left side
                // of the top of the paddle or up to 39 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks will take the value of bdx and add 1 to it up to a maximum
                // bdx value of 3
                else if((bx >= (px + 30)) && (bx < (px + 40))){
                    // if/else block to make sure the value of bdx doesn't rise above 3
                    if(bdx <= 2){
                        bdx += 1;
                    } else {
                        bdx = 3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 40 pixels to the right of the far left side
                // of the top of the paddle or up to 49 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks will take the value of bdx and add 2 to it up to a maximum
                // bdx value of 3
                else if((bx >= (px + 40)) && (bx < (px + 50))){
                    // if/else block to make sure the value of bdx doesn't rise above 3
                    if(bdx <= 1){
                        bdx += 2;
                    } else{
                        bdx = 3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
                // else if block to see if the ball collided with paddle at 50 pixels to the right of the far left side
                // of the top of the paddle or up to 55 pixels to the right of the far left side
                // of the top of the paddle
                // This else if blocks will take the value of bdx and add 3 to it up to a maximum
                // bdx value of 3
                else if((bx >= (px + 50)) && (bx <= (px + 55))){
                    // if/else block to make sure the value of bdx doesn't rise above 3
                    if(bdx <= 0){
                        bdx += 3;
                    } else {
                        bdx = 3;
                    }

                    // bounce the ball back up
                    bdy *= -1;
                }
            } else if(by > 580){
                launchBall(pane1, pane2);
            }
        }));

        // Set the CycleCount of the Animation to INDEFINITE (animation doesn't end) and Play the animation
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        // KeyPressed EventHandler for paddle
        scene.setOnKeyPressed(e -> {
            // if block to check that pdx is greater than -3 and the user pressed the left arrow key and
            // subtract from pdx if so
            if((e.getCode() == KeyCode.LEFT) && (pdx > -3)){
                pdx--;
            }
            // else if block to check that pdx is less than 3 and the user pressed the right arrow key and
            // add one to pdx if so
            else if((e.getCode() == KeyCode.RIGHT) && (pdx < 3)){
                pdx++;
            }
            // else if block to set pdx to 0 (stop the paddle) if the user presses the down arrow key
            else if(e.getCode() == KeyCode.DOWN){
                pdx = 0;
            }
            // else if block to pause/play the game if the user presses the [SPACE] bar,
            // depending on whether the game is currently running or already paused. If Paused,
            // show the Text object that says "P A U S E D" on the screen at the specified location and
            // Set the Color and Size of the Text object Font
            else if(e.getCode() == KeyCode.SPACE) {
                if (timer.getStatus() == Animation.Status.RUNNING) {
                    pauseText = new Text(200, 325, "P A U S E D");
                    pauseText.setFont(new Font(36));
                    pauseText.setFill(Color.RED);
                    pane2.getChildren().add(pauseText);
                    timer.pause();
                } else {
                    pane2.getChildren().remove(pauseText);
                    timer.play();
                }
            }
        });

        // Set the Title of the Stage, Set the Scene to the Stage and Show the Stage
        primaryStage.setTitle("Breakout! Ryan Huffman: Programming Assignment #3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // If the ball has gone passed the paddle in the y direction, the user loses that life, subtract 1 from lives, and
    // if lives is 0 stop the timer and display a “game over” type message,
    // otherwise remove the Text object from the Pane, create a new one with the new number of lives,
    // and add the new Text object to the Pane and start the ball anew
    // (I have a separate launchBall method to start the ball each time needed)
    public void launchBall(Pane pane1, Pane pane2){

        // Initialize bx and by
        bx= 300;
        by = 250;

        // Initialize a new Circle ball and add it to the Pane
        ball = new Circle(bx, by, 10);
        pane2.getChildren().add(ball);

        // Randomly get the initial horizontal direction and velocity of the Circle ball is left or right
        int direction = randomGenerator.nextInt(2);
        bdx = (randomGenerator.nextInt(3) + 1);
        if(direction == 1){
            bdx *= -1;
        }

        // Randomly get the initial vertical velocity of the Circle ball
        bdy = (randomGenerator.nextInt(3) + 1);

        // Method call to remove all the Text and then replace it with the new Text
        replaceText(pane1, pane2);

        // Initialize the String value of the Text object and Add it to the Pane
        if(lives > 1){
            lives--;
            livesValue = new Text(540, 40, String.valueOf(lives));
            pane1.getChildren().add(livesValue);
        } else {
            // Stop the Animation
            timer.stop();

            // Remove all the Text objects from the top Pane
            pane1.getChildren().removeAll(descriptionScore, scoreValue, descriptionLives, livesValue);

            // Display the game over message and final score to the user
            Text gameOverMessage = new Text(150, 20, "Game Over! You Lost! We hope you enjoyed Breakout!");
            descriptionScore = new Text(275, 40, "Score: ");
            scoreValue = new Text(310, 40, String.valueOf(score));
            pane1.getChildren().add(gameOverMessage);
            pane1.getChildren().add(descriptionScore);
            pane1.getChildren().add(scoreValue);
        }
    }


    // Method that is called if the user clears the playing field of all Brick objects
    public void wonGame(Pane pane1, Pane pane2){
        // Remove all the Text objects from the top Pane
        pane1.getChildren().removeAll(descriptionScore, scoreValue, descriptionLives, livesValue);

        // Display the game over message and final score to the user
        Text gameOverMessage = new Text(150, 20, "Stage Cleared! You Won! We hope you enjoyed Breakout!");
        descriptionScore = new Text(275, 40, "Score: ");
        scoreValue = new Text(310, 40, String.valueOf(score));
        pane1.getChildren().add(gameOverMessage);
        pane1.getChildren().add(descriptionScore);
        pane1.getChildren().add(scoreValue);
    }


    // Method to rewrite all the Text
    public void replaceText(Pane pane1, Pane pane2){
        // Remove all the Text objects from the top Pane
        pane1.getChildren().removeAll(descriptionScore, scoreValue, descriptionLives, livesValue);

        // Initialize the String value of the Text object and Add it to the Pane
        descriptionScore = new Text(40, 40, "Score: ");
        pane1.getChildren().add(descriptionScore);

        // Initialize the String value of the Text object and Add it to the Pane
        scoreValue = new Text(75, 40, String.valueOf(score));
        pane1.getChildren().add(scoreValue);

        // Initialize the String value of the Text object and Add it to the Pane
        descriptionLives = new Text(505, 40, "Lives: ");
        pane1.getChildren().add(descriptionLives);
    }
}