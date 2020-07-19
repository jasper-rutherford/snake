
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Handler
{

    private boolean restart;
    private MouseInput mouse;
    private int mouseX;
    private int mouseY;
    private MouseWheel wheel;
    private JFrame myFrame;
    private int rot;
    private boolean leftHeld;
    private boolean rightHeld;

    private int gridSize;
    private boolean[][] grid;
    private ArrayList<int[]> snake;
    private int direction;
    private int tickCount;
    private int tileLength;
    private boolean lose;
    private int[] appleCoords;
    private boolean start;
    private int oldDirection;
    private boolean enterPressed;


    public Handler(JFrame frame)
    {
        restart = false;
        mouse = new MouseInput();
        wheel = new MouseWheel();
        wheel = new MouseWheel();
        myFrame = frame;
        mouseX = getMouseX();
        mouseY = getMouseY();
        rot = 0;
        leftHeld = false;
        rightHeld = false;

        gridSize = 10;
        grid = new boolean[gridSize][gridSize];
        direction = -1; // 0 - right, 1 - down, etc
        tickCount = 0;
        tileLength = 600 / gridSize;
        lose = false;
        appleCoords = new int[]{(int) (Math.random() * gridSize), (int) (Math.random() * gridSize)};
        start = false;
        oldDirection = -1;
        enterPressed = false;
    }

    public void tick()
    {
        doKeyInput();

        runLeftMouse();
        runRightMouse();

        doMouseWheel();

        mouseX = getMouseX();
        mouseY = getMouseY();

        tickCount++;

        if (tickCount >= 6 && !lose && start && direction != -1)
        {
            moveSnake();
            tickCount = 0;
        }
    }

    public void render(Graphics g)
    {
        g.setColor(new Color(1, 1, 1));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(Color.RED); // mouse crosshair
        g.drawLine(mouseX, 0, mouseX, Game.HEIGHT);
        g.drawLine(0, mouseY, Game.WIDTH, mouseY);

        g.setColor(new Color(43, 139, 149));
        g.fillRect(0, 0, gridSize * tileLength, gridSize * tileLength);
        g.setColor(new Color(52, 177, 47));
        for (int x = 0; x < gridSize; x++)
        {
            for (int y = 0; y < gridSize; y++)
            {
                if (grid[x][y])
                    g.fillRect(x * tileLength, y * tileLength, tileLength, tileLength);
            }
        }

        g.setColor(new Color(223, 33, 34));
        g.fillRect(appleCoords[0] * tileLength, appleCoords[1] * tileLength, tileLength, tileLength);
    }

    public boolean getRestart()
    {
        return restart;
    }


    public void doKeyInput()
    {
        // close game on pressing escape
        if (KeyInput.getKey(KeyEvent.VK_ESCAPE))
        {
            System.exit(0);
        }

        if (KeyInput.getKey(KeyEvent.VK_RIGHT) && oldDirection != 2)
        {
            direction = 0;
        }
        if (KeyInput.getKey(KeyEvent.VK_DOWN) && oldDirection != 3)
        {
            direction = 1;
        }
        if (KeyInput.getKey(KeyEvent.VK_LEFT) && oldDirection != 0)
        {
            direction = 2;
        }
        if (KeyInput.getKey(KeyEvent.VK_UP) && oldDirection != 1)
        {
            direction = 3;
        }

        //restart by pressing enter
        if (KeyInput.getKey(KeyEvent.VK_ENTER))
        {
            if (!enterPressed)
            {
                start = true;
                lose = false;
                grid = new boolean[gridSize][gridSize];
                snake = new ArrayList<>();
                int[] coords = {gridSize / 2, gridSize / 2};
                snake.add(coords);
                grid[coords[0]][coords[1]] = true;
                direction = -1;
                oldDirection = -1;
            }
            enterPressed = true;
        }
        else
            enterPressed = false;
    }

    public void moveSnake()
    {
        int newX = -1;
        int newY = -1;
        boolean eat = false;

        //move right
        if (direction == 0)
        {
            newX = snake.get(0)[0] + 1;
            newY = snake.get(0)[1];
            if (newX == gridSize || grid[newX][newY])
                lose = true;
        }
        else if (direction == 1) //move down
        {
            //create new snake part
            newX = snake.get(0)[0];
            newY = snake.get(0)[1] + 1;
            if (newY == gridSize || grid[newX][newY])
                lose = true;
        }
        else if (direction == 2) //move left
        {
            newX = snake.get(0)[0] - 1;
            newY = snake.get(0)[1];
            if (newX == -1 || grid[newX][newY])
                lose = true;
        }
        else if (direction == 3)//move up
        {
            newX = snake.get(0)[0];
            newY = snake.get(0)[1] - 1;
            if (newY == -1 || grid[newX][newY])
                lose = true;
        }

        if (!lose)
        {
            //add new snake part
            int[] newCoord = {newX, newY};
            snake.add(0, newCoord);
            grid[newX][newY] = true;
            oldDirection = direction;

            if (grid[appleCoords[0]][appleCoords[1]])
                eat = true;
        }

        if (!eat)
        {
            //remove last snake part
            int oldX = snake.get(snake.size() - 1)[0];
            int oldY = snake.get(snake.size() - 1)[1];
            snake.remove(snake.size() - 1);
            grid[oldX][oldY] = false;
        }
        else
        {
            appleCoords = new int[]{(int) (Math.random() * gridSize), (int) (Math.random() * gridSize), tileLength, tileLength};
        }
    }

    public void runLeftMouse()
    {
        boolean holdTest = leftHeld; //saves what leftHeld was
        leftHeld = mouse.leftIsHeld(); //updates leftHeld

        if (!holdTest && leftHeld) //if leftheld was changed to true
            runLeftMousePressed();

        if (leftHeld)
            runLeftMouseHeld();

        if (holdTest && !leftHeld) //if leftheld is changed to false
            runLeftMouseReleased();
    }

    public void runLeftMousePressed()
    {

    }

    public void runLeftMouseHeld()
    {

    }

    public void runLeftMouseReleased()
    {

    }

    public void runRightMouse()
    {
        boolean holdTest = rightHeld; //saves what rightHeld was
        rightHeld = mouse.rightIsHeld(); //updates rightHeld

        if (!holdTest && rightHeld) // if rightheld is changed to true
            runRightMousePressed();

        if (rightHeld)
            runRightMouseHeld();

        if (holdTest && !rightHeld) //if rightheld is changed to false
            runRightMouseReleased();
    }

    public void runRightMousePressed()
    {

    }

    public void runRightMouseHeld()
    {

    }

    public void runRightMouseReleased()
    {

    }

    public void doMouseWheel()
    {
        rot = (int) wheel.getRot();
    }


    public int getMouseX()
    {
        int screenX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int screenY = (int) MouseInfo.getPointerInfo().getLocation().getY();

        Point point = new Point(screenX, screenY);
        SwingUtilities.convertPointFromScreen(point, myFrame);

        return (int) point.getX();
    }

    public int getMouseY()
    {
        int screenX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int screenY = (int) MouseInfo.getPointerInfo().getLocation().getY();

        Point point = new Point(screenX, screenY);
        SwingUtilities.convertPointFromScreen(point, myFrame);

        return (int) point.getY();
    }

}