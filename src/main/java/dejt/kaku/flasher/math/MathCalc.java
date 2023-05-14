package dejt.kaku.flasher.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public final class MathCalc {

    public static float getDistance(BlockPos from, BlockPos to)
    {
        float f = (float)(from.getX() - to.getX());
        float f1 = (float)(from.getY() - to.getY());
        float f2 = (float)(from.getZ() - to.getZ());
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public static List<BlockPos> checkWithinRadius(BlockPos start, int r, int y_step){
        boolean exceed = false;
        List<BlockPos> positions = new ArrayList<>();
        while(!exceed){
            for(int i = -r; i < r; i++){
                for(int j = -r; j < r; j++)
                    for(int k = -y_step; k < y_step; k++){
                        BlockPos pos = start.add(i, k, j);
                        exceed = getDistance(start, pos) > r;
                        if(!exceed){
                            positions.add(pos);
                        }
                    }
            }
        }
        return positions;
    }

    public static BlockPos clamp(BlockPos posIn, int lowesty, int highesty, World world){
        int y0 = MathHelper.clamp(posIn.getY() + lowesty, 0, world.getHeight());
        int y1 = MathHelper.clamp(posIn.getY() + highesty, 0, world.getHeight());
        return new BlockPos(posIn.getX(), MathHelper.clamp(posIn.getY(), y0, y1), posIn.getZ());
    }
}
