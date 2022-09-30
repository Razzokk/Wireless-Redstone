package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blocks.P2PReceiverBlock;
import rzk.wirelessredstone.registries.ModBlockEntities;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2PTransmitterBlockEntity extends BlockEntity
{
    private static final String LINKED_RECEIVERS_FIELD = "linked_receivers";
    private Set<BlockPos> linkedReceivers = new HashSet<>();

    public P2PTransmitterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.SIMPLE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        if (!nbt.contains(LINKED_RECEIVERS_FIELD)) return;

        ListTag links = nbt.getList(LINKED_RECEIVERS_FIELD, Tag.TAG_LIST);
        links.forEach(tag -> linkedReceivers.add(NbtUtils.readBlockPos((CompoundTag) tag)));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        if (linkedReceivers.isEmpty()) return;

        ListTag links = new ListTag();
        linkedReceivers.forEach(tag -> links.add(NbtUtils.writeBlockPos(tag)));
        nbt.put(LINKED_RECEIVERS_FIELD, links);
    }

    private boolean isPowered()
    {
        return getBlockState().getValue(POWERED);
    }

    public void link(BlockPos pos)
    {
        linkedReceivers.add(pos);
        setChanged();
        updateP2PReceiver(pos, isPowered());
    }

    public void unlink(BlockPos pos)
    {
        linkedReceivers.remove(pos);
        setChanged();
        updateP2PReceiver(pos, false);
    }

    public void updateP2PReceiver(BlockPos pos, boolean powered)
    {
        BlockState receiver = level.getBlockState(pos);
        if (!(receiver.getBlock() instanceof P2PReceiverBlock)) linkedReceivers.remove(pos);
        else if (level.isLoaded(pos)) level.setBlock(pos, receiver.setValue(POWERED, powered), 3);
    }

    public void updateAllP2PReceivers(boolean powered)
    {
        linkedReceivers.forEach(pos -> updateP2PReceiver(pos, powered));
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel)
        {
            //serverLevel.getChunkSource().getPendingTasksCount()
            WirelessRedstone.LOGGER.debug("onLoad: (level: {}, entity: {})", serverLevel, this);
        }
    }
}
