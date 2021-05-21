package evhh.prefabs;

import evhh.components.MarkComponent;
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
public class MarkPrefab extends ObjectPrefab
{

    public MarkPrefab(BufferedImage texture, String textureRef,  int id)
    {
        super(texture, textureRef, false, id);
    }

    @Override
    public GameObject getInstance(Grid grid, int x, int y)
    {
        GameObject instance =  super.getInstance(grid, x, y);
        instance.addComponent(new MarkComponent(instance));
        instance.setCreator(this);
        return instance;
    }
}
