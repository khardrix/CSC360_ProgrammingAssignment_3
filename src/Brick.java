/*********************************************************************************************************************
 *********************************************************************************************************************
 *****  Class: CSC-360-001-2019-040    Semester: Summer 2019    Professor: Richard Fox    Student: Ryan Huffman  *****
 *****-----------------------------------------------------------------------------------------------------------*****
 *****                                       Programming Assignment #3                                           *****
 *****___________________________________________________________________________________________________________*****
 *****                             Brick class for use with the game of Breakout!                                *****
 *********************************************************************************************************************
 *********************************************************************************************************************/

// IMPORTS of needed tools and plug-ins
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Brick {

    // CLASS VARIABLE(s) declaration(s)
    private double x;
    private double y;
    private Color color;
    private boolean isVisible;
    private int value;


    // 4-arg Constructor where the Brick is instantiated with isVisible set to true as default
    public Brick(double x, double y, Color color, int value){
        this.x = x;
        this.y = y;
        this.color = color;
        this.value = value;
        this.isVisible = true;
    }


    // 5-arg Constructor that allows you to set whether the Brick is initially visible or not
    public Brick(double x, double y, Color color, int value, boolean isVisible){
        this.x = x;
        this.y = y;
        this.color = color;
        this.value = value;
        this.isVisible = isVisible;
    }


    // collides method that receives the ball’s location (bx, by) and returns true if the ball has hit this Brick and
    // false otherwise
    public boolean collides(double bx, double by){
        if(isVisible){
            if(((by - 10) >= y) && ((by - 10) <= (y + 10))){
                if(((bx + 10) >= x) && ((bx - 10) <= (x + 28))){
                    return true;
                }
            }
        }
        return false;
    }


    // A draw method which receives the Pane object from the start method.
    // If the Brick is visible, then do these steps.
    // Create a Rectangle with the x,y coordinate of this Brick, set its fill Color to the Color of this Brick.
    // Draw the Rectangle on the Pane.  Obviously if the Brick is not visible, you would skip this step
    public void draw(Pane pane){
        if(this.isVisible){
            Rectangle breakoutBrick = new Rectangle(this.x, this.y, 28, 10);
            breakoutBrick.setFill(this.color);
            breakoutBrick.setStroke(Color.BLACK);
            pane.getChildren().add(breakoutBrick);
        }
    }


    // A remove method that, if invoked from your Timeline’s action handler, causes this Brick to “disappear”.
    // This method receives the Pane. This method sets visible to false, creates a new Rectangle that is white and
    // draws this Rectangle on the Pane. What this does is “replace” the Brick with a white one and
    // thus renders it invisible. In my implementation, this method returned the value of the Brick that was hit
    // to be added to the user’s score. You can do this in other ways although
    // removing a Brick by using pane.getChildren().remove(...); is harder because
    // a Brick is not the same as the Rectangle object added to the pane.
    public int remove(Pane pane){
        this.isVisible = false;
        Rectangle newBreakoutBrick = new Rectangle(this.x, this.y, 28, 10);
        newBreakoutBrick.setFill(Color.WHITE);
        pane.getChildren().add(newBreakoutBrick);

        return this.value;
    }


    // Accessor method to return the value of value
    public int getValue() {
        return this.value;
    }


    // Accessor method to return the value of isVisible
    public boolean getIsVisible() {
        return this.isVisible;
    }
}
