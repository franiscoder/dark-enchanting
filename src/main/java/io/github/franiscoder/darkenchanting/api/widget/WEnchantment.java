package io.github.franiscoder.darkenchanting.api.widget;


import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.franiscoder.darkenchanting.DarkEnchanting;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class WEnchantment extends WLB implements Supplier<WEnchantment>{
    @Nullable
    private Text enchantmentName;
    @Nullable
    private WEnchantment.LabelUpdater labelUpdater = null;
    private Enchantment enchantment;
    private BlockContext ctx;


    public WEnchantment(Enchantment enchantment, BlockContext ctx) {
        super(0, enchantment.getMaximumLevel());
        this.enchantmentName = new TranslatableText(enchantment.getTranslationKey());
        this.enchantment = enchantment;
        this.ctx = ctx;

    }

    public static WEnchantment set(Enchantment enchantment, int value, BlockContext ctx) {
        WEnchantment w = new WEnchantment(enchantment, ctx);
        w.enchantment = enchantment;
        w.enchantmentName = new TranslatableText(enchantment.getTranslationKey());
        w.setLabel(w.enchantmentName);
        w.setValue(value);
        return w;
    }

    @Override
    protected void onValueChanged(int value) {
        //I hope this works
        Inventory inv = CottonCraftingController.getBlockInventory(ctx);
        ItemStack stack = inv.getInvStack(0);
        super.onValueChanged(value);

        if (labelUpdater != null) {
            enchantmentName = labelUpdater.updateLabel(value);
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.put(enchantment, value);
        EnchantmentHelper.set(map, stack);
        ctx.run(
                (world, pos) -> {

                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeBlockPos(pos);
                    passedData.writeIdentifier(Registry.ENCHANTMENT.getId(enchantment));
                    passedData.writeInt(value);
                    System.out.println("Sending Packet...");
                    ClientSidePacketRegistry.INSTANCE.sendToServer(DarkEnchanting.ENCHANT_PACKET, passedData);
                    System.out.println("Packet Sent!");
                }
        );
    }

    @Override
    public WEnchantment get() {
        return this;
    }
}