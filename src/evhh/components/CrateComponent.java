package evhh.components;

import evhh.model.GameComponent;
import evhh.model.GameObject;
import evhh.model.gamecomponents.Sprite;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Project_Sokoban
 * @package: evhh.components
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-17
 * @time: 13:25
 **********************************************************************************************************************/
public class CrateComponent extends GameComponent
{
    private boolean isMarked;
    private boolean wasMarked = false;
    private transient BufferedImage unmarkedCrateTexture,  markedCrateTexture;
    private String markedCrateTextureRef,unmarkedCrateTextureRef;
    private MarkComponent markComponent;
    public CrateComponent(GameObject parent, boolean isMarked,BufferedImage unmarkedCrateTexture,String unmarkedCrateTextureRef, BufferedImage markedCrateTexture,String markedCrateTextureRef)
    {
        super(parent);
        this.isMarked = isMarked;
        this.unmarkedCrateTexture = unmarkedCrateTexture;
        this.markedCrateTexture = markedCrateTexture;
        this.markedCrateTextureRef = markedCrateTextureRef;
        this.unmarkedCrateTextureRef = unmarkedCrateTextureRef;
        if(isMarked)
            ((Sprite)parent.getComponent(Sprite.class)).switchImage(markedCrateTexture,markedCrateTextureRef);
        else
            ((Sprite)parent.getComponent(Sprite.class)).switchImage(unmarkedCrateTexture,unmarkedCrateTextureRef);

    }

    public boolean push(GameObject pusher)
    {
        int newX = 2*getX()-pusher.getX();
        int newY = 2*getY()-pusher.getY();
        if(parent.getGrid().isValidCoordinates(newX,newY))
        {
            if(parent.getGrid().isEmpty(newX,newY))
            {

                parent.setPosition(newX,newY);
                if(isMarked)
                {
                    parent.getGrid().addGameObject(markComponent.getGameObject(),markComponent.getX(),markComponent.getY());
                    ((Sprite)parent.getComponent(Sprite.class)).switchImage(unmarkedCrateTexture,unmarkedCrateTextureRef);
                    isMarked = false;
                }
            }
            else if(parent.getGrid().get(newX,newY).hasComponent(MarkComponent.class))
            {
                MarkComponent prevMarked = null;
                if(isMarked)
                {
                    wasMarked = true;
                    prevMarked = markComponent;
                }
                else
                    ((Sprite)parent.getComponent(Sprite.class)).switchImage(markedCrateTexture,markedCrateTextureRef);

                markComponent = ((MarkComponent)parent.getGrid().get(newX,newY).getComponent(MarkComponent.class));
                parent.getGrid().removeGameObject(newX,newY);
                parent.setPosition(newX,newY);
                isMarked = true;
                if(wasMarked)
                {
                    parent.getGrid().addGameObject(prevMarked.getGameObject(), prevMarked.getX(), prevMarked.getY());
                    wasMarked = false;
                }
            }
            else {return false;}
            return true;
        }
        return false;
    }
    @Override
    public void onStart()
    {
        if (unmarkedCrateTexture == null && unmarkedCrateTextureRef != null)
            unmarkedCrateTexture = parent.getGrid().getGameInstance().getTexture(unmarkedCrateTextureRef);
        if (markedCrateTexture == null && markedCrateTextureRef != null)
            markedCrateTexture = parent.getGrid().getGameInstance().getTexture(markedCrateTextureRef);
    }

    @Override
    public void update()
    {

    }

    @Override
    public void onExit()
    {

    }

    public boolean isMarked()
    {
        return isMarked;
    }
}
