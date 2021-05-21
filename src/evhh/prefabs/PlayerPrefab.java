package evhh.prefabs;

import evhh.components.PlayerComponent;
import evhh.controller.InputManager.UserInputManager;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;
import evhh.model.gamecomponents.AudioComponent;
import evhh.view.audio.AudioListener;

import java.awt.image.BufferedImage;
import java.io.File;

/***********************************************************************************************************************
 * @project: AOOP_Project_Sokoban
 * @package: evhh.prefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-17
 * @time: 13:31
 **********************************************************************************************************************/
public class PlayerPrefab extends ObjectPrefab
{

    transient private UserInputManager uIM;
    private int upKeyCode
    , downKeyCode
    , rightKeyCode
    , leftKeyCode;
    AudioListener audioListener;
    File[] audioFiles;
    public PlayerPrefab(BufferedImage texture,
                        String textureRef,
                        int id,
                        UserInputManager uIM,
                        int upKeyCode,
                        int downKeyCode,
                        int rightKeyCode,
                        int leftKeyCode)
    {
        super(texture, textureRef, false, id);
        this.uIM = uIM;
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.rightKeyCode = rightKeyCode;
        this.leftKeyCode = leftKeyCode;
    }

    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance = super.getInstance(grid, x, y);
        PlayerComponent pc = new PlayerComponent(instance, uIM,upKeyCode,downKeyCode,rightKeyCode,leftKeyCode);
        if(audioListener !=null && audioFiles !=null)
        {
            AudioComponent ac = new AudioComponent(instance,audioListener,audioFiles);
            pc.setAudioComponent(ac);
            instance.addComponent(ac);
        }
        instance.addComponent(pc);
        instance.setCreator(this);

        return instance;
    }
    public void addStepSound(AudioListener audioListener, File[] audioFiles)
    {
        this.audioListener = audioListener;
        this.audioFiles = audioFiles;
    }
}
