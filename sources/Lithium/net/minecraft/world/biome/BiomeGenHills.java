package net.minecraft.world.biome;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class BiomeGenHills extends BiomeGenBase
{
    private WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
    private WorldGenTaiga2 field_150634_aD = new WorldGenTaiga2(false);
    private int field_150635_aE = 0;
    private int field_150636_aF = 1;
    private int field_150637_aG = 2;
    private int field_150638_aH;

    protected BiomeGenHills(int id, boolean p_i45373_2_)
    {
        super(id);
        this.field_150638_aH = this.field_150635_aE;

        if (p_i45373_2_)
        {
            this.theBiomeDecorator.treesPerChunk = 3;
            this.field_150638_aH = this.field_150636_aF;
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return (WorldGenAbstractTree)(rand.nextInt(3) > 0 ? this.field_150634_aD : super.genBigTreeChance(rand));
    }

    public void decorate(World worldIn, Random rand, BlockPosition pos)
    {
        super.decorate(worldIn, rand, pos);
        int i = 3 + rand.nextInt(6);

        for (int j = 0; j < i; ++j)
        {
            int k = rand.nextInt(16);
            int l = rand.nextInt(28) + 4;
            int i1 = rand.nextInt(16);
            BlockPosition blockpos = pos.add(k, l, i1);

            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.stone)
            {
                worldIn.setBlockState(blockpos, Blocks.emerald_ore.getDefaultState(), 2);
            }
        }

        for (i = 0; i < 7; ++i)
        {
            int j1 = rand.nextInt(16);
            int k1 = rand.nextInt(64);
            int l1 = rand.nextInt(16);
            this.theWorldGenerator.generate(worldIn, rand, pos.add(j1, k1, l1));
        }
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();

        if ((noiseVal < -1.0D || noiseVal > 2.0D) && this.field_150638_aH == this.field_150637_aG)
        {
            this.topBlock = Blocks.gravel.getDefaultState();
            this.fillerBlock = Blocks.gravel.getDefaultState();
        }
        else if (noiseVal > 1.0D && this.field_150638_aH != this.field_150636_aF)
        {
            this.topBlock = Blocks.stone.getDefaultState();
            this.fillerBlock = Blocks.stone.getDefaultState();
        }

        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    /**
     * this creates a mutation specific to Hills biomes
     */
    private BiomeGenHills mutateHills(BiomeGenBase p_150633_1_)
    {
        this.field_150638_aH = this.field_150637_aG;
        this.func_150557_a(p_150633_1_.color, true);
        this.setBiomeName(p_150633_1_.biomeName + " M");
        this.setHeight(new BiomeGenBase.Height(p_150633_1_.minHeight, p_150633_1_.maxHeight));
        this.setTemperatureRainfall(p_150633_1_.temperature, p_150633_1_.rainfall);
        return this;
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        return (new BiomeGenHills(p_180277_1_, false)).mutateHills(this);
    }
}
