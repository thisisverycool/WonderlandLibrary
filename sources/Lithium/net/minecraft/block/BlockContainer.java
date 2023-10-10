package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPosition;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider
{
    protected BlockContainer(Material materialIn)
    {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    protected BlockContainer(Material p_i46402_1_, MapColor p_i46402_2_)
    {
        super(p_i46402_1_, p_i46402_2_);
        this.isBlockContainer = true;
    }

    protected boolean isInvalidNeighbor(World p_181086_1_, BlockPosition p_181086_2_, EnumFacing p_181086_3_)
    {
        return p_181086_1_.getBlockState(p_181086_2_.offset(p_181086_3_)).getBlock().getMaterial() == Material.cactus;
    }

    protected boolean hasInvalidNeighbor(World p_181087_1_, BlockPosition p_181087_2_)
    {
        return this.isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.NORTH) || this.isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.SOUTH) || this.isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.WEST) || this.isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.EAST);
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType()
    {
        return -1;
    }

    public void breakBlock(World worldIn, BlockPosition pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPosition pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }
}
