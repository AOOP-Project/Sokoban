package evhh.components;

import evhh.common.RunnableArg;
import evhh.controller.InputManager.KeyboardInput;
import evhh.controller.InputManager.UserInputManager;
import evhh.model.ControllableComponent;
import evhh.model.GameComponent;
import evhh.model.GameObject;
import evhh.model.gamecomponents.AudioComponent;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/***********************************************************************************************************************
 * @project: AOOP_Project_Sokoban
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-17
 * @time: 13:26
 **********************************************************************************************************************/
public class PlayerComponent extends ControllableComponent
{
    private  int upKeyCode,downKeyCode,rightKeyCode, leftKeyCode;
    private transient KeyboardInput keyboardInput;
    private  MarkComponent mark = null,newMark = null;
    private AudioComponent audioComponent;
    public PlayerComponent(GameObject parent,UserInputManager uIM,int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode)
    {
        super(parent,uIM);
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.rightKeyCode = rightKeyCode;
        this.leftKeyCode = leftKeyCode;
        RunnableArg<KeyEvent> keyInputEvent = keyEvent -> tryMove(keyEvent.getKeyCode());
        HashMap<Integer,Integer> map = new HashMap<>();

        map.put(upKeyCode,KeyEvent.KEY_PRESSED);
        map.put(downKeyCode,KeyEvent.KEY_PRESSED);
        map.put(rightKeyCode,KeyEvent.KEY_PRESSED);
        map.put(leftKeyCode,KeyEvent.KEY_PRESSED);
        keyboardInput = new KeyboardInput(keyInputEvent,map);
        if(uIM!=null)
            uIM.addKeyInput(keyboardInput);

    }
    public void tryMove(int keyCode)
    {

        int newX = 0;
        int newY = 0;
        if(keyCode==upKeyCode)
        {
            newX = getX();
            newY = getY()+1;
        }
        else if(keyCode==downKeyCode)
        {
            newX = getX();
            newY = getY()-1;
        }
        else if(keyCode==rightKeyCode)
        {
            newX = getX()+1;
            newY = getY();
        }
        else if(keyCode==leftKeyCode)
        {
            newX = getX()-1;
            newY = getY();
        }
        else{return;}
        if(!parent.getGrid().isValidCoordinates(newX,newY))
            return;
        if(parent.getGrid().isEmpty(newX,newY))
            move(newX,newY);
        else if(parent.getGrid().get(newX,newY).hasComponent(CrateComponent.class))
        {
            CrateComponent crateComponent = ((CrateComponent)parent.getGrid().get(newX,newY).getComponent(CrateComponent.class));
            if(crateComponent.push(parent))
            {
                if(!parent.getGrid().isEmpty(newX,newY)&&parent.getGrid().get(newX,newY).hasComponent(MarkComponent.class))
                {
                    newMark = ((MarkComponent)parent.getGrid().get(newX,newY).getComponent(MarkComponent.class));
                    parent.getGrid().removeGameObject(newX, newY);
                }
                move(newX,newY);
            }
        }
        else if(parent.getGrid().get(newX,newY).hasComponent(MarkComponent.class))
        {
            newMark = ((MarkComponent)parent.getGrid().get(newX,newY).getComponent(MarkComponent.class));
            parent.getGrid().removeGameObject(newX, newY);
            move(newX,newY);
        }
    }
    private void move(int x, int y)
    {
        if(audioComponent!=null)
            audioComponent.play(0);
        synchronized (this)
        {
            parent.setPosition(x, y);
            if (mark != null)
            {
                parent.getGrid().getGameInstance().addGameObject(mark.getGameObject(), mark.getX(), mark.getY());
                mark = null;
            }
            mark = newMark;
            newMark = null;
        }
    }
    @Override
    public void onUIMRefresh(UserInputManager userInputManager)
    {
        super.uIM = userInputManager;
        RunnableArg<KeyEvent> keyInputEvent = keyEvent -> tryMove(keyEvent.getKeyCode());
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(upKeyCode,KeyEvent.KEY_PRESSED);
        map.put(downKeyCode,KeyEvent.KEY_PRESSED);
        map.put(rightKeyCode,KeyEvent.KEY_PRESSED);
        map.put(leftKeyCode,KeyEvent.KEY_PRESSED);
        keyboardInput = new KeyboardInput(keyInputEvent,map);
        uIM.addKeyInput(keyboardInput);
    }

    public AudioComponent getAudioComponent()
    {
        return audioComponent;
    }

    public void setAudioComponent(AudioComponent audioComponent)
    {
        this.audioComponent = audioComponent;
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void onExit()
    {

    }
}
