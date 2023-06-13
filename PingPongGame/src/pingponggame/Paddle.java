/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pingponggame;
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle{
	int id;
	int yVelocity;
	int speed = 10;
        Image paddleImage;
        Image paddleImage2;
	Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		this.id=id;
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                paddleImage = toolkit.getImage("assets/paddle1-image.png");
                paddleImage2 = toolkit.getImage("assets/paddle2-image.png");
	}
	public void keyPressed(KeyEvent e){
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(speed);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(speed);
			}
			break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(0);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(0);
			}
			break;
		}
	}
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void move() {
		y= y + yVelocity;
	}
	public void draw(Graphics g) {
		if(id==1){
                    g.drawImage(paddleImage, x, y, width, height, null);
                }
                    
			//g.setColor(Color.blue);
                else if(id==2){
                    g.drawImage(paddleImage2, x, y, width, height, null);
                }
                    
			//g.setColor(Color.red);
                        
		//g.fillRect(x, y, width, height);
                //g.drawImage(paddleImage, x, y, width, height, null);
                
	}
}
