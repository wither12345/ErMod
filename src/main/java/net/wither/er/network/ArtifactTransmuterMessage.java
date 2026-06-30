package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.world.inventory.ArtifactTransmuterGuiMenu;
import org.jetbrains.annotations.NotNull;

public record ArtifactTransmuterMessage() implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<ArtifactTransmuterMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "artifact_transmuter"));

    public static final StreamCodec<ByteBuf, ArtifactTransmuterMessage> STREAM_CODEC = StreamCodec.unit(new ArtifactTransmuterMessage());

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ArtifactTransmuterMessage data, final IPayloadContext context) {
        Player player = context.player() ;
        if(player.containerMenu instanceof ArtifactTransmuterGuiMenu menu){
            menu.enhance();
            /*
            ItemStack slot1 = menu.getSlot(1).getItem() ;
            ItemStack slot0 = menu.getSlot(0).getItem() ;
            ArtifactData artifactData = slot1.getComponents().get(DataComponentsRegister.ARTIFACT.get());
            if(artifactData != null && slot0.getComponents().get(DataComponents.CUSTOM_DATA) != null){
                int experience = artifactData.level().experience() ;
                int total_experience = artifactData.level().total_experience() ;
                int mora = slot0.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getInt("moras") ;
                int lv = artifactData.level().level() ;
                int rarity = artifactData.rarity() ;
                for(int i = 2 ; i < 6 ; i ++){
                    if(lv >= ArtifactData.getMaxLevel(rarity))
                        break;

                    ItemStack item = menu.getSlot(i).getItem() ;
                    ArtifactData artifactData1 = item.getComponents().get(DataComponentsRegister.ARTIFACT.get());
                    int exp_per = getExp(artifactData1) ;
                    if(item.getItem() == ErModItems.SANCTIFYING_UNCTION.get())
                        exp_per = 2500 ;
                    if(item.getItem() == ErModItems.SANCTIFYING_ESSENCE.get())
                        exp_per = 10000 ;

                    if(exp_per > 0) {
                        while(exp_per <= mora && item.getCount() > 0 && lv < ArtifactData.getMaxLevel(rarity)) {
                            if(artifactData1 != null) {
                                experience += (int) (artifactData1.level().total_experience() * 0.8);
                                total_experience += (int) (artifactData1.level().total_experience() * 0.8);
                            }
                            experience += exp_per ;
                            total_experience += exp_per ;
                            mora -= exp_per ;
                            item.shrink(1);
                            while (experience >= ArtifactData.getMaxExp(lv, rarity) && lv < ArtifactData.getMaxLevel(rarity)){
                                experience -= ArtifactData.getMaxExp(lv, rarity) ;
                                lv ++ ;
                            }
                        }
                    }
                }

                final int exp = experience;
                final int total_exp = total_experience;
                int finalLv = lv;
                slot1.update(DataComponentsRegister.ARTIFACT.get(), artifactData, d -> d.setLevel(new ArtifactLevel(finalLv, exp, total_exp)));
                int finalMora = mora;
                CustomData.update(DataComponents.CUSTOM_DATA, slot0, tag -> tag.putInt("moras", finalMora));
            }
             */
        }
    }
}
