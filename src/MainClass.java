/*
 * author : Natig Babayev
 * December, 2015
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class MainClass extends GraphicsProgram implements ActionListener {

	public GOval food;

	public static GRect[] rects;

	private int snakeX, snakeY, snakeWidth, snakeHeight;

	boolean left = false;
	// because first direction when game starts have to be right for moving
	// snake to the right
	boolean right = true;
	boolean up = false;
	boolean down = false;

	public Timer timer = new Timer(200, this);

	private boolean isPlaying, isGameOver;
	private int score, previousScore;
	private GLabel scoreLabel;

	public void run() {
		// we have to set our score and previousScore variables to 0
		score = 0;
		previousScore = 0;
		// game hasn't started yet, so, gameOver and isPlaying must be false
		isGameOver = false;
		isPlaying = false;
		// first we have to add our black background
		addBackground();
		// then we have to add random food to our screen
		randomFood();
		// now it's time to explain the rules
		// we put 2 GLabel and set parameters accordingly
		GLabel startLabel = new GLabel("Click anywhere in this window for starting game.", getWidth() / 2,
				(getHeight() / 2));
		startLabel.move(-startLabel.getWidth() / 2, -startLabel.getHeight());
		startLabel.setColor(Color.WHITE);
		add(startLabel);
		GLabel rulesLabel = new GLabel("Press [<-] for moving left and press [->] for moving right.", getWidth() / 2,
				(getHeight() / 2) + 75);
		rulesLabel.move(-rulesLabel.getWidth() / 2, -rulesLabel.getHeight());
		rulesLabel.setColor(Color.WHITE);
		add(rulesLabel);

		// we have to put score label to our screen
		scoreLabel = new GLabel("Score : " + score, getWidth() - 200, 30);
		scoreLabel.move(-scoreLabel.getWidth() / 2, -scoreLabel.getHeight());
		scoreLabel.setColor(Color.RED);
		add(scoreLabel);

		// we have to wait for click for starting game
		waitForClick();
		// now user clicked, so, we can set our variables accordingly,
		// and remove game rules
		isPlaying = true;
		remove(startLabel);
		remove(rulesLabel);
		// timer started, so, snake'll move
		timer.start();
		// we draw our snake
		drawSnake();
		// we'll listen to keyboard for direction, too
		addKeyListeners();

	}

	/*
	 * following part is only for drawing black background with GRect
	 */
	public GRect background;

	private void addBackground() {
		background = new GRect(0, 0, getWidth(), getHeight());
		background.setFilled(true);
		background.setColor(Color.BLACK);
		add(background);
	}
	/*
	 * end
	 */

	private void randomFood() {
		Random random = new Random();
		int maxX = getWidth() - 15;
		int maxY = getHeight() - 15;
		int min = 0;
		// we generate two random integers for pointing our food
		int pointX = random.nextInt((maxX - min) + 1) + min;
		int pointY = random.nextInt((maxY - min) + 1) + min;
		// we add new oval as a food
		// we colorize it
		// yellow is fine
		food = new GOval(pointX, pointY, 15, 15);
		food.setFilled(true);
		food.setColor(Color.YELLOW);
		// we add it to our screen
		add(food);
	}

	/*
	 * Following method are for handling key events we will change our boolean
	 * variables which are called up, down, left, right according do keyboard
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			// moving up
			up = true;
			down = false;
			left = false;
			right = false;
			break;

		case KeyEvent.VK_DOWN:
			// moving down
			up = false;
			down = true;
			left = false;
			right = false;
			break;

		case KeyEvent.VK_LEFT:
			// moving left
			up = false;
			down = false;
			left = true;
			right = false;
			break;

		case KeyEvent.VK_RIGHT:
			// moving right
			up = false;
			down = false;
			left = false;
			right = true;
			break;

		}
	}

	public void drawSnake() {
		// first size of snake will be 10 rectangles
		rects = new GRect[10];
		// Y coordinate'll be 0, it's OK
		snakeY = 0;
		// setting snake width and height
		snakeWidth = 15;
		snakeHeight = 15;
		// and calculating first part of snake's X coordinate
		snakeX = rects.length * snakeWidth;
		// we add 10 rectangle to our rects array
		for (int i = 0; i < rects.length; i++) {
			rects[i] = new GRect(snakeX, snakeY, snakeWidth, snakeHeight);
			rects[i].setFilled(true);
			rects[i].setColor(Color.BLUE);
			// we have to decrease the X coordinate of next rectangle
			// Otherwise, everything will be "ust-uste"
			snakeX -= snakeWidth;
		}
		// now we add all elements of rects array to our screen
		for (int i = 0; i < rects.length; i++) {
			add(rects[i]);
		}
	}

	public void redrawSnake() {
		// in this method, we redraw our snake
		// if there is any changes we'll call this method and start redrawing
		// mainly, I'll use it for movenments of snake according to movement
		// algorithm of snake game
		for (int i = rects.length - 1; i > 0; i--) {
			rects[i].setLocation(rects[i - 1].getX(), rects[i - 1].getY());
		}
		for (int i = 0; i < rects.length; i++) {
			add(rects[i]);
		}
	}

	public void growSnake() {
		// for growing snake we need to increase the size of rects array
		// For that purpose we have to create arraylist
		// then we should add all rects elements to that list
		ArrayList<GRect> arr = new ArrayList<GRect>();
		for (int i = 0; i < rects.length; i++) {
			arr.add(rects[i]);
		}
		// then we have to create new part of our snake and give color
		// so, we add this rectangle to our arraylist
		GRect rect = new GRect(rects[rects.length - 1].getX() - snakeWidth,
				rects[rects.length - 1].getY() - snakeHeight, snakeWidth, snakeHeight);
		rect.setFilled(true);
		rect.setColor(Color.BLUE);
		arr.add(rect);
		// then for updating our rects array, we have to use
		// arraylist.toArray(...) function, so we increase size of rects array,
		// and elements of rects array
		rects = arr.toArray(new GRect[arr.size()]);
		redrawSnake();
	}

	public void moveUp() {
		// update snake
		redrawSnake();
		// if head part of snake(rects[0]) is not out of screen, change location
		// of rects[0] (because other parts will move accordingly
		// if out of screen, isGameOver must be true
		if ((rects[0].getX() + snakeWidth >= 0 && rects[0].getX() + snakeWidth <= getWidth())
				&& rects[0].getY() + snakeWidth >= 0 && rects[0].getY() + snakeWidth <= getHeight()) {
			rects[0].setLocation(rects[0].getX(), rects[0].getY() - snakeWidth);
		} else {
			isGameOver = true;
		}
	}

	public void moveDown() {
		// update snake
		redrawSnake();
		// if head part of snake(rects[0]) is not out of screen, change location
		// of rects[0] (because other parts will move accordingly
		// if out of screen, isGameOver must be true
		if ((rects[0].getX() + snakeWidth >= 0 && rects[0].getX() + snakeWidth <= getWidth())
				&& rects[0].getY() + snakeWidth >= 0 && rects[0].getY() + snakeWidth <= getHeight()) {
			rects[0].setLocation(rects[0].getX(), rects[0].getY() + snakeWidth);
		} else {
			isGameOver = true;
		}
	}

	public void moveLeft() {
		// update snake
		redrawSnake();
		// if head part of snake(rects[0]) is not out of screen, change location
		// of rects[0] (because other parts will move accordingly
		// if out of screen, isGameOver must be true
		if ((rects[0].getX() + snakeWidth >= 0 && rects[0].getX() + snakeWidth <= getWidth())
				&& rects[0].getY() + snakeWidth >= 0 && rects[0].getY() + snakeWidth <= getHeight()) {
			rects[0].setLocation(rects[0].getX() - snakeWidth, rects[0].getY());
		} else {
			isGameOver = true;
		}
	}

	public void moveRight() {
		// update snake
		redrawSnake();
		// if head part of snake(rects[0]) is not out of screen, change location
		// of rects[0] (because other parts will move accordingly
		// if out of screen, isGameOver must be true
		if ((rects[0].getX() + snakeWidth >= 0 && rects[0].getX() + snakeWidth <= getWidth())
				&& rects[0].getY() + snakeWidth >= 0 && rects[0].getY() + snakeWidth <= getHeight()) {
			rects[0].setLocation(rects[0].getX() + snakeWidth, rects[0].getY());
		} else {
			isGameOver = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// each timer tick, I check if game is over or not
		if (!isGameOver) {
			// if game is not over, I update my score label which is called
			// scoreLabel
			scoreLabel.setLabel("Score : " + score);
			// if down is true, call moveDown() method
			if (down) {
				moveDown();
			}
			// if up is true, call moveUp() method
			if (up) {
				moveUp();
			}
			// if right is true, call moveRight() method
			if (right) {
				moveRight();
			}
			// if left is true, call moveLeft() method
			if (left) {
				moveLeft();
			}
			// each time update previous score
			previousScore = score;
			// if there is intersection with food
			// increase score by 10
			// call growSnake() method
			// remove previous food
			// call randomFood method and create new food
			if (intersectsWithFood()) {
				score += 10;
				growSnake();
				remove(food);
				randomFood();
			}
			// if there's intersection with snake itself gameOver must be true;
			if (intersectsWithSnake() && score - previousScore == 0) {
				isGameOver = true;
			}
		} else {
			// if game over, stop timer
			timer.stop();
			// create game over Label, and set parameters
			GLabel gameOverLabel = new GLabel("Game Over!", getWidth() / 2, (getHeight() / 2));
			gameOverLabel.move(-gameOverLabel.getWidth() / 2, -gameOverLabel.getHeight());
			gameOverLabel.setColor(Color.WHITE);
			// add it to screen
			add(gameOverLabel);
		}
	}

	private boolean intersectsWithFood() {
		// for increasing score, I have to check if first part of body parts (or
		// we can simply say head part) intersects with our food or not
		// for this I use simple function which is provided by acm library
		// object.getBounds().intersects(otherObject)
		// if head collides with food oval, return true
		// otherwise return false
		if (food.getBounds().intersects(rects[0].getBounds())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean intersectsWithSnake() {
		GRect head = null;
		// in following codes I put small rectangle in front of my snake's head
		// in that way I will detect if the snake head collides with itself or
		// not
		if ((!left && !down && !up && !right) || right) {
			head = new GRect(rects[0].getX() + 15, rects[0].getY() + 5, 5, 5);
		} else if (left) {
			head = new GRect(rects[0].getX() - 5, rects[0].getY() + 5, 5, 5);
		} else if (down) {
			head = new GRect(rects[0].getX() + 5, rects[0].getY() + 15, 5, 5);
		} else if (up) {
			head = new GRect(rects[0].getX() + 5, rects[0].getY() - 5, 5, 5);
		}
		// after putting small rectangle which is called head
		// I have to check for intersections
		// for this, I have to check if head rectangle collides with any body
		// part of snake
		// So, I create for loop and start checking each body part intersects
		// with head rectangle or not
		// if yes, return true, and exit from loop and function
		for (int i = 1; i < rects.length; i++) {
			if (head.getBounds().intersects(rects[i].getBounds())) {
				return true;
			}
		}
		// if none of body parts of snake collides with head, return false
		return false;
	}

	public static void main(String[] args) {
		new MainClass().start();
	}
}