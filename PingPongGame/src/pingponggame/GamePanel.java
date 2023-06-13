/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pingponggame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALL_DIAMETER = 50;
	static final int PADDLE_WIDTH = 80;
	static final int PADDLE_HEIGHT = 130;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;

	boolean gameOver; // New variable to track game over state
	String winner; // New variable to store the winner
        private Image backgroundImage;
        
	GamePanel(){
                
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH,GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
                
		gameThread = new Thread(this);
		gameThread.start();
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                backgroundImage = toolkit.getImage("assets/Background.png");//intended to add bg but hasn't finished
                
                
	}

	public void newBall() {
		random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
	}
	public void newPaddles() {
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
	}
        
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
                draw(graphics);
		g.drawImage(image,0,0,this);
               
                
	}
        
        private boolean showPauseMessage = true;
	public void draw(Graphics g) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
                if (showPauseMessage){
                    g.setColor(Color.white);
                    g.setFont(new Font("Consolas", Font.PLAIN, 20));
                    g.drawString("Press P to pause", GAME_WIDTH - 300, 30);
                }
		if (gameOver) {
			g.setColor(Color.white);
			g.setFont(new Font("Consolas",Font.PLAIN,60));
			FontMetrics fontMetrics = g.getFontMetrics();
			int textWidth = fontMetrics.stringWidth(winner);
			int x = (GAME_WIDTH - textWidth) / 2;
			int y = GAME_HEIGHT / 2;
			g.drawString(winner, x, y);
		}
		Toolkit.getDefaultToolkit().sync();
	}
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}
        
	public void checkCollision() {

		//bounce ball off top & bottom window edges
		if(ball.y <=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
                
		//bounce ball off paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		//stops paddles at window edges
		if(paddle1.y<=0)
			paddle1.y=0;
		if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		if(paddle2.y<=0)
			paddle2.y=0;
		if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		//give a player 1 point and creates new paddles & ball
		if (ball.x <= 0) {
                    if (score.player2 >= 5) {
                        gameOver = true;
                        winner = "Player 2 Wins!";
                        togglePause();
                    } else {
                        score.player2++;
                        newPaddles();
                        newBall();
                        System.out.println("Player 2: " + score.player2);
                    }
                }
                if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
                    if (score.player1 >= 5) {
                        gameOver = true;
                        winner = "Player 1 Wins!";
                        togglePause();
                    } else {
                        score.player1++;
                        newPaddles();
                        newBall();
                        System.out.println("Player 1: " + score.player1);
                    }
                }
	}
        
        
        
        private boolean isPaused = false;
        
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
                             if (!isPaused) { // Only move and update the game if not paused
                                move();
                                checkCollision();
                            }
                            repaint();
                            delta--;
			}
		}
	}
        public void togglePause() {
            isPaused = !isPaused;
        }
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_P) {
                        togglePause(); // Pause/resume the game when "P" key is pressed
                    } else {
                        paddle1.keyPressed(e);
                        paddle2.keyPressed(e);
                    }
		}
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}
}

