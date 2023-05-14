package dejt.kaku.flasher.lamp;

import dejt.kaku.flasher.LightFlasher;
import dejt.kaku.flasher.math.MathCalc;
import javafx.scene.effect.Light;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * NOTE: the onStateChange method only works properly if the powered rail is powered from a powered block. I do not know how to
 * solve the other issue so I leave it like that for now.
 */
@Mod.EventBusSubscriber(modid = LightFlasher.MODID)
public class LampState {

    //state of lamp inferred from powered rail because I am lazy.

    @SubscribeEvent
    public static void onStateChange(BlockEvent.NeighborNotifyEvent ev){
        IBlockState state = ev.getState();
        Block block = state.getBlock();
        World world = ev.getWorld();
        BlockPos position;
        try{
            position = world.playerEntities.get(0).getPosition(); //hardcoded to main player.
        } catch (Exception e) {
            position = BlockPos.ORIGIN;
        }
        if(block instanceof BlockRailPowered && getStateRange(position, LightFlasher.configuration.mechanics.checking_radius, BlockRailPowered.class, world, BlockRailPowered.POWERED)){
            if (LightFlasher.finalStatus) {
                System.out.println("The light will flash");
                LightFlasher.board.doAction();
            } else {
                LightFlasher.board.end();
                System.out.println("Rail is powered");
            }
        }
    }

    private static boolean getStateRange(BlockPos start, int radius, Class<? extends Block> typeBlock, World world, IProperty<Boolean> property){
        start = MathCalc.clamp(start, -3, 3, world);
        List<BlockPos> positionList = MathCalc.checkWithinRadius(start, radius, 3);
        for(BlockPos pos : positionList){
            if(LightFlasher.configuration.mechanics.view) world.setBlockState(pos.up(30), Blocks.STONE.getDefaultState());
            Block block = world.getBlockState(pos).getBlock();
                if(block.getClass() == typeBlock && world.getBlockState(pos).getValue(property)){
                    return true;
                }
        }
        return false;
    }


}
