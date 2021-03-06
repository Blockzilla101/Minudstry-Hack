package mindustry.world.consumers;

import arc.func.Boolf;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.util.ArcAnnotate.NonNull;
import mindustry.entities.type.TileEntity;
import mindustry.type.Item;
import mindustry.ui.Cicon;
import mindustry.ui.ItemImage;
import mindustry.ui.MultiReqImage;
import mindustry.ui.ReqImage;
import mindustry.world.Tile;
import mindustry.world.meta.BlockStat;
import mindustry.world.meta.BlockStats;
import mindustry.world.meta.values.ItemFilterValue;

import static mindustry.Vars.*;

public class ConsumeItemFilter extends Consume{
    public final @NonNull
    Boolf<Item> filter;

    public ConsumeItemFilter(Boolf<Item> item){
        this.filter = item;
    }

    @Override
    public void applyItemFilter(Bits arr){
        content.items().each(filter, item -> arr.set(item.id));
    }

    @Override
    public ConsumeType type(){
        return ConsumeType.item;
    }

    @Override
    public void build(Tile tile, Table table){
        MultiReqImage image = new MultiReqImage();
        content.items().each(i -> filter.get(i) && (!world.isZone() || data.isUnlocked(i)), item -> image.add(new ReqImage(new ItemImage(item.icon(Cicon.medium), 1), () -> tile.entity != null && tile.entity.items != null && tile.entity.items.has(item))));

        table.add(image).size(8 * 4);
    }

    @Override
    public String getIcon(){
        return "icon-item";
    }

    @Override
    public void update(TileEntity entity){

    }

    @Override
    public void trigger(TileEntity entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items != null && entity.items.has(item) && this.filter.get(item)){
                entity.items.remove(item, 1);
                break;
            }
        }
    }

    @Override
    public boolean valid(TileEntity entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items != null && entity.items.has(item) && this.filter.get(item)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void display(BlockStats stats){
        stats.add(booster ? BlockStat.booster : BlockStat.input, new ItemFilterValue(filter));
    }
}
