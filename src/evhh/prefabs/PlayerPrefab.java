package evhh.prefabs;

import evhh.components.PlayerComponent;
import evhh.controller.InputManager.UserInputManager;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;

import java.awt.image.BufferedImage;

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
        instance.addComponent(new PlayerComponent(instance, uIM,upKeyCode,downKeyCode,rightKeyCode,leftKeyCode));
        instance.setCreator(this);
        return instance;
    }
}
