package evhh.prefabs;

import evhh.components.CrateComponent;
import evhh.model.GameObject;
import evhh.model.Grid;
import evhh.model.ObjectPrefab;
import evhh.model.gamecomponents.Sprite;

import java.awt.image.BufferedImage;

/***********************************************************************************************************************
 * @project: AOOP_Project_Sokoban
 * @package: evhh.prefabs
 * ---------------------------------------------------------------------------------------------------------------------
 * @authors: Hamed Haghjo & Elias Vahlberg
 * @date: 2021-05-17
 * @time: 13:30
 **********************************************************************************************************************/
public class CratePrefab extends ObjectPrefab
{

    transient private BufferedImage markedCrateTexture;
    private String markedCrateTextureRef;

    public CratePrefab(BufferedImage texture, String textureRef, int id,BufferedImage markedCrateTexture,String markedCrateTextureRef)
    {
        super(texture, textureRef, false, id);
        this.markedCrateTexture = markedCrateTexture;
        this.markedCrateTextureRef = markedCrateTextureRef;
    }

    @Override
    public GameObject getInstance(Grid grid,int x, int y)
    {
        GameObject instance = super.getInstance(grid,x,y);
        instance.addComponent(new CrateComponent(instance,false,texture,textureRef,markedCrateTexture,markedCrateTextureRef));
        instance.setCreator(this);
        return instance;
    }

}
