package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.P2PRedstoneReceiverBlock;
import rzk.wirelessredstone.block.P2PRedstoneTransmitterBlock;

import java.util.List;

public class LinkerItem extends Item
{
    public LinkerItem(Properties props)
    {
        super(props.stacksTo(1));
    }

    private static BlockPos getLinkedPos(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("link")) return null;
        return NbtUtils.readBlockPos(tag.getCompound("link"));
    }

    private static void setLinkedPos(ItemStack stack, BlockPos linkedPos)
    {
        CompoundTag linkTag = NbtUtils.writeBlockPos(linkedPos);
        stack.getOrCreateTag().put("link", linkTag);
    }

    private static void toggleLinkingMode(ItemStack stack)
    {
        stack.getOrCreateTag().putBoolean("mode", !getLinkingMode(stack));
    }

    private static boolean getLinkingMode(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("mode")) return false;
        return tag.getBoolean("mode");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCrouching()) return InteractionResultHolder.pass(stack);

        toggleLinkingMode(stack);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState state = level.getBlockState(clickedPos);

        WirelessRedstone.LOGGER.debug("onItemUseFirst (clientSide: {}, stack: {})", level.isClientSide, stack);

        if (!(state.getBlock() instanceof P2PRedstoneTransmitterBlock || state.getBlock() instanceof P2PRedstoneReceiverBlock))
            return InteractionResult.PASS;


        if (!level.isClientSide)
        {
            if (state.getBlock() instanceof P2PRedstoneReceiverBlock)
            {
                setLinkedPos(stack, clickedPos);
            }
            else if (state.getBlock() instanceof P2PRedstoneTransmitterBlock)
            {
                BlockPos linkedPos = getLinkedPos(stack);
                if (linkedPos == null) return InteractionResult.FAIL;
                if (!getLinkingMode(stack)) P2PRedstoneTransmitterBlock.linkReceiver(level, clickedPos, linkedPos);
                else P2PRedstoneTransmitterBlock.unlinkReceiver(level, clickedPos, linkedPos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag)
    {
        Component modeComponent = Component.translatable(getLinkingMode(stack) ? "unlink" : "link").withStyle(ChatFormatting.YELLOW);
        list.add(Component.translatable("Mode: %s", modeComponent).withStyle(ChatFormatting.GRAY));

        BlockPos linkedPos = getLinkedPos(stack);
        if (linkedPos == null) return;
        Component posComponent = Component.translatable("[x=%s, y=%s, z=%s]", linkedPos.getX(), linkedPos.getY(), linkedPos.getZ()).withStyle(ChatFormatting.AQUA);
        list.add(Component.translatable("Linked Position: %s", posComponent).withStyle(ChatFormatting.GRAY));
    }
}
