package evhh;

import evhh.common.imagemanipulation.ImageTiler;
import evhh.components.CrateComponent;
import evhh.components.MarkComponent;
import evhh.controller.InputManager.UserInputManager;
import evhh.model.EventTrigger;
import evhh.model.GameInstance;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;
import evhh.model.mapeditor.MapEditor;
import evhh.prefabs.CratePrefab;
import evhh.prefabs.MarkPrefab;
import evhh.prefabs.PlayerPrefab;
import evhh.prefabs.WallPrefab;
import evhh.view.audio.AudioListener;
import evhh.view.renderers.FrameRenderer;
import evhh.view.renderers.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/***********************************************************************************************************************
 * @project: AOOP_Project_Sokoban
 * @package: evhh
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-17
 * @time: 13:22
 **********************************************************************************************************************/
public class Main
{
    public static final int DEFAULT_GRID_WIDTH = 16;
    public static final int DEFAULT_GRID_HEIGHT = 16;
    public static final int DEFAULT_CELL_SIZE = 32;
    public static final String GRID_SAVE_PATH = System.getProperty("user.dir") + "/SavedData/SavedGrids/";
    public static final String ASSET_PATH = System.getProperty("user.dir") + "/Assets";
    public static final int TIMER_DELAY = 1000 / 60;

    private static GameInstance game1;
    private static Grid grid;
    private static FrameRenderer frameRenderer;
    private static GameFrame gameFrame;
    private static UserInputManager userInputManager;

    private static MapEditor mapEditor;
    private static CratePrefab cratePrefab;
    private static WallPrefab wallPrefab;
    private static PlayerPrefab playerPrefab;
    private static PlayerPrefab playerPrefab2;
    private static MarkPrefab markPrefab;
    private static EventTrigger levelSwitchingEvent;
    private static boolean runWithMapEditor;




    public static void main(String[] args)
    {

        createGame();
        createPrefabs();
        int reply = JOptionPane.showConfirmDialog(null, "Do you want to run with map editor?", "Choose startup mode", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Starting game and map editor");
            runWithMapEditor = true;
            runWithMapEditor();
        } else if(reply == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Creating new Grid");
            runWithMapEditor = false;
            addMaps();
        } else{
            JOptionPane.showMessageDialog(null, "Shutting down");
            System.exit(0);
        }

        game1.start();

    }

    private static void runWithMapEditor()
    {
        Thread thread = new Thread(() -> buildScene());
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {

                synchronized (game1)
                {
                    game1.refreshSpritesInRenderer();
                    game1.refreshMappedUserInput();

                    if (mapEditor != null)
                        if (mapEditor.getWorkingGrid() != game1.getMainGrid())
                        {
                            game1.setMainGrid(mapEditor.getWorkingGrid());
                        }
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,100);
        thread.start();
    }
    private static void createGame()
    {
        game1 = new GameInstance("Game1");
        grid = new Grid(DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
        gameFrame = new GameFrame(DEFAULT_GRID_WIDTH * DEFAULT_CELL_SIZE, DEFAULT_GRID_HEIGHT * DEFAULT_CELL_SIZE, "Game1");
        frameRenderer = new FrameRenderer(gameFrame, DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT, DEFAULT_CELL_SIZE);
        userInputManager = new UserInputManager(game1);
        game1.setMainGrid(grid);
        game1.setFrameRenderer(frameRenderer);
        game1.setUserInputManager(userInputManager);
        game1.addRendererTimer(TIMER_DELAY);
        game1.setUpdateTimer(TIMER_DELAY);

        game1.loadTextureAssets(ASSET_PATH + "/Textures");
        game1.getFrameRenderer().setGridBackgroundImage(ImageTiler.tileImage(game1.getTexture("blank"),DEFAULT_GRID_WIDTH,DEFAULT_GRID_HEIGHT));
        game1.getFrameRenderer().setAudioListener(new AudioListener());


        BufferedImage tiled =  ImageTiler.tileImage(game1.getTexture("blank"), DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
        try
        {
            ImageIO.write(tiled, "png", new File(ASSET_PATH + "/tiled.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private static void addMaps()
    {
        game1.addSavedGridPath(GRID_SAVE_PATH + "map1.ser");
        game1.addSavedGridPath(GRID_SAVE_PATH + "map2.ser");
        game1.addSavedGridPath(GRID_SAVE_PATH + "map3.ser");
        game1.addSavedGridPath(GRID_SAVE_PATH + "map4.ser");
        game1.addSavedGridPath(GRID_SAVE_PATH + "map5.ser");
        game1.switchGrid(0);
        createEvents();
        game1.startPeriodicEventChecking();
    }
    private static void createPrefabs()
    {
        cratePrefab = new CratePrefab(game1.getTexture("crate"),"crate",310,game1.getTexture("cratemarked"),"cratemarked");
        wallPrefab = new WallPrefab(game1.getTexture("wall"),"wall",320);
        playerPrefab = new PlayerPrefab(
                game1.getTexture("player"),
                "player",
                330,
                game1.getUserInputManager(),
                KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_D,
                KeyEvent.VK_A);
        File[] audioFilesPlayer = new File[1];
        audioFilesPlayer[0] = new File(System.getProperty("user.dir") + "/Assets/AudioAssets/Step.wav");
        playerPrefab.addStepSound(game1.getFrameRenderer().getAudioListener(),audioFilesPlayer);

        playerPrefab2 = new PlayerPrefab(
                game1.getTexture("player"),
                "player",
                340,
                game1.getUserInputManager(),
                KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_LEFT);
        markPrefab = new MarkPrefab(game1.getTexture("blankmarked"),"blankmarked",350);




    }
    private static void buildScene()
    {

        mapEditor = new MapEditor(grid,DEFAULT_CELL_SIZE,new ObjectPrefab[]{cratePrefab, wallPrefab, playerPrefab, markPrefab,playerPrefab2});
    }
    private static void createEvents()
    {
        levelSwitchingEvent = new EventTrigger()
        {
            @Override
            public boolean checkTrigger()
            {
                    return Arrays.
                            stream(game1.getGameObjects(CrateComponent.class)).
                            map(g -> (CrateComponent) g.getComponent(CrateComponent.class)).
                            allMatch(CrateComponent::isMarked);
            }

            @Override
            public void runEvent()
            {
                if(game1.getCurrentGridIndex()+1<game1.numSavedGrids())
                    game1.switchGrid(game1.getCurrentGridIndex()+1);
                if(game1.getCurrentGridIndex()+1==game1.numSavedGrids())
                    game1.switchGrid(0);//game1.reloadGrid();
            }
        };
        game1.addEvent(levelSwitchingEvent);
    }

}
