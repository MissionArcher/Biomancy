package com.github.elenterius.biomancy.block;

import com.github.elenterius.biomancy.client.util.TooltipUtil;
import com.github.elenterius.biomancy.init.ModBlocks;
import com.github.elenterius.biomancy.tileentity.DigesterTileEntity;
import com.github.elenterius.biomancy.tileentity.state.DigesterStateData;
import com.github.elenterius.biomancy.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class DigesterBlock extends OwnableContainerBlock {

	public static final BooleanProperty CRAFTING = ModBlocks.CRAFTING_PROPERTY;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final VoxelShape NORTH_SHAPE = createVoxelShape(Direction.NORTH);
	public static final VoxelShape SOUTH_SHAPE = createVoxelShape(Direction.SOUTH);
	public static final VoxelShape EAST_SHAPE = createVoxelShape(Direction.EAST);
	public static final VoxelShape WEST_SHAPE = createVoxelShape(Direction.WEST);

	public DigesterBlock(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(CRAFTING, false).with(FACING, Direction.NORTH));
	}

	private static VoxelShape createVoxelShape(Direction direction) {
		AxisAlignedBB aabb0 = VoxelShapeUtil.createUnitAABB(0, 0, 3, 16, 14, 16);
		AxisAlignedBB aabb1 = VoxelShapeUtil.createUnitAABB(4, 14, 4, 12, 16, 12);
		AxisAlignedBB aabb2 = VoxelShapeUtil.createUnitAABB(3, 1, 0, 13, 10, 3);
		return Stream.of(VoxelShapeUtil.createWithFacing(direction, aabb0), VoxelShapeUtil.createWithFacing(direction, aabb1), VoxelShapeUtil.createWithFacing(direction, aabb2)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT nbt = stack.getChildTag("BlockEntityTag");
		if (nbt != null) {
			tooltip.add(TooltipUtil.EMPTY_LINE_HACK());
			CompoundNBT fuelNbt = nbt.getCompound(DigesterStateData.NBT_KEY_FUEL);
			int mainFuel = (int) (MathHelper.clamp(fuelNbt.getInt("Amount") / (float) DigesterTileEntity.MAX_FUEL, 0f, 1f) * 100);
			tooltip.add(new TranslationTextComponent("fluid." + fuelNbt.getString("FluidName").replace(":", ".").replace("/", ".")).appendString(": " + mainFuel + "%"));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(CRAFTING, FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity instanceof DigesterTileEntity) {
				((DigesterTileEntity) tileEntity).dropAllInvContents(world, blockPos);
			}
			super.onReplaced(state, world, blockPos, newState, isMoving);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote()) return ActionResultType.SUCCESS;

		//TODO: verify that authorization works
		INamedContainerProvider containerProvider = getContainer(state, worldIn, pos);
		if (containerProvider != null && player instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			NetworkHooks.openGui(serverPlayerEntity, containerProvider, (packetBuffer) -> {});
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new DigesterTileEntity();
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction facing = state.get(FACING);
		switch (facing) {
			case NORTH:
				return NORTH_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			case EAST:
				return EAST_SHAPE;
		}
		return VoxelShapes.fullCube();
	}
}