package net.wither.er.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ErOverlay {
    private static final ResourceLocation STAMINA_SPRITE = new ResourceLocation("er:gui/sprites/hud/stamina_full");
    private static final ResourceLocation STAMINA_EMPTY_SPRITE = new ResourceLocation("er:hud/stamina_empty");
    private static final ResourceLocation skillL = new ResourceLocation("er:textures/screens/elemental_skill_l.png");
    private static final ResourceLocation skillR = new ResourceLocation("er:textures/screens/elemental_skill_r.png");
    private static final ResourceLocation energy = new ResourceLocation("er:textures/screens/energy_filling.png");
    private static final ResourceLocation burstL = new ResourceLocation("er:textures/screens/elemental_burst_l.png");
    private static final ResourceLocation burstR = new ResourceLocation("er:textures/screens/elemental_burst_r.png");
    private static final ResourceLocation HEALTH_BAR_BASE = new ResourceLocation("er:hud/health/player_health_bar_base");
    private static final ResourceLocation HEALTH_BAR_FILLING = new ResourceLocation("er:hud/health/player_health_bar_filling");
    private static final ResourceLocation HEALTH_BAR_BLINKING = new ResourceLocation("er:hud/health/player_health_bar_blinking");
    private static final ResourceLocation HEALTH_BAR_ABSORBING = new ResourceLocation("er:hud/health/player_health_bar_absorbing");
	private static final ResourceLocation ARMOR_FULL_SPRITE = new ResourceLocation("minecraft:hud/armor_full");

	private static int lastDamage = 0 ;
	private static int damageColor = 0 ;
	private static long lastDamageTick = 0 ;

	private static float lastHealth = 0 ;

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Post event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Player player = Minecraft.getInstance().player;
		//maybeRenderPlayerStamina(player, event.getGuiGraphics(), w, h);
		maybeRenderStellaFortunaIcon(player, event.getGuiGraphics(), w, h);
	}

	@SubscribeEvent
	public static void ClientTick(TickEvent.ClientTickEvent event){
		Player player = Minecraft.getInstance().player;
		if(player == null)
			return;
		if(lastHealth > player.getHealth()){
			lastHealth = (float) Math.max(player.getHealth() , lastHealth - player.getMaxHealth() * 0.01);
			lastHealth = Math.min(lastHealth , player.getMaxHealth());
		}
		else if(lastHealth < player.getHealth()){
			lastHealth = player.getHealth() ;
		}
	}

	private static void maybeRenderStellaFortunaIcon(Player player, GuiGraphics graphics, int w, int h) {
		player.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(
			(var) -> {
				if (var.Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
					ErCombatVariables.PlayerVariables vars = player.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
					renderElementalSkillIcon(player, graphics, w, h, vars);
					renderElementalBrustIcon(player, graphics, w, h, vars, fortuna);
				}
			}
		);

	}

	private static void renderElementalSkillIcon(Player player, GuiGraphics graphics, int w, int h, ErCombatVariables.PlayerVariables vars) {
		float chargingPercent = vars.skillCooldown / vars.stackedMaxSkillCooldown;
		if (chargingPercent > 0.5) {
			graphics.blit(skillL, w - 110, h - 70, 0, 0, 32, (int) (32 * Math.sin(chargingPercent * Math.PI)), 32, 32);
		} else if (chargingPercent > 0) {
			graphics.blit(skillL, w - 110, h - 70, 0, 0, 32, 32, 32, 32);
			graphics.blit(skillR, w - 110, h - 70 + (int) (32 * Math.sin(chargingPercent * Math.PI)), 0, (int) (32 * Math.sin(chargingPercent * Math.PI)), 32, (int) (33 - 32 * Math.sin(chargingPercent * Math.PI)), 32, -32);
		} else {
			graphics.blit(skillL, w - 110, h - 70, 0, 0, 32, 32, 32, 32);
			graphics.blit(skillR, w - 110, h - 70, 0, 32, 32, 32, 32, -32);
		}
	}

	private static void renderElementalBrustIcon(Player player, GuiGraphics graphics, int w, int h, ErCombatVariables.PlayerVariables vars, StellaFortunas fortuna) {
		int percent = (int) (48 * vars.energyAmount / fortuna.getEnergyCost(player));
		RenderSystem.enableBlend();
		if (fortuna.elementType() == 1)
			RenderSystem.setShaderColor(0, 1, 0.6f, 0.3f);
		else
			RenderSystem.setShaderColor(1, 1, 1, 0.3f);
		graphics.blit(energy, w - 75, h - 20 - percent, 0, 48 - percent, 48, percent, 48, 48);

		if (fortuna.elementType() == 1)
			RenderSystem.setShaderColor(0, 0.7f, 0.42f, 1);
		else
			RenderSystem.setShaderColor(0.7f, 0.7f, 0.7f, 1);
		float chargingPercent = vars.burstCooldown / vars.stackedMaxBurstCooldown;
		if (chargingPercent > 0.5) {
			graphics.blit(burstL, w - 75, h - 68, 0, 0, 48, (int) (48 * Math.sin(chargingPercent * Math.PI)), 48, 48);
		} else if (chargingPercent > 0) {
			graphics.blit(burstL, w - 75, h - 68, 0, 0, 48, 48, 48, 48);
			graphics.blit(burstR, w - 75, h - 68 + (int) (48 * Math.sin(chargingPercent * Math.PI)), 0, (int) (48 * Math.sin(chargingPercent * Math.PI)), 48, (int) (49 - 48 * Math.sin(chargingPercent * Math.PI)), 48, -48);
		} else {
			graphics.blit(burstL, w - 75, h - 68, 0, 0, 48, 48, 48, 48);
			graphics.blit(burstR, w - 75, h - 68, 0, 48, 48, 48, 48, -48);
		}
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.disableBlend();
	}

	private static void maybeRenderPlayerStamina(Player player, GuiGraphics graphics, int width, int height) {
		Minecraft minecraft = Minecraft.getInstance();
		Gui gui = minecraft.gui;
		if (minecraft.gameMode.canHurtPlayer()) {
			if (player != null) {
				int i1 = graphics.guiWidth() / 2 + 91;
				minecraft.getProfiler().push("stamina");
				double maxStamina = player.getAttribute(ErModAttributes.MAX_STAMINA.get()).getValue();
				ErCombatVariables.PlayerVariables vars = player.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseThrow(Error::new);
				int i3 = 20;
				int j3 = (int) (vars.stamina / maxStamina * 20);
				int rightHeight = height - 69;
				if (j3 < i3) {
					int j2 = graphics.guiHeight() - rightHeight;
					int l3 = Mth.ceil((double) (j3 - 2) * 10.0 / (double) i3);
					int i4 = Mth.ceil((double) j3 * 10.0 / (double) i3) - l3;
					RenderSystem.disableDepthTest();
					RenderSystem.depthMask(false);
					RenderSystem.enableBlend();
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					RenderSystem.setShaderColor(1, 1, 1, 1);
					for (int j4 = 0; j4 < l3 + i4; j4++) {
						graphics.blit(STAMINA_SPRITE, i1 - j4 * 8 - 9, j2, 9, 9, 8, 8);
					}
					RenderSystem.disableBlend();
					RenderSystem.depthMask(true);
					RenderSystem.defaultBlendFunc();
					RenderSystem.enableDepthTest();
					RenderSystem.setShaderColor(1, 1, 1, 1);
					rightHeight += 10;
				}
				minecraft.getProfiler().pop();
				//gui.rightHeight = rightHeight;
			}
		}
	}

	private static void renderPlayerHealthBar(Player player, GuiGraphics graphics) {
		/*
		Minecraft minecraft = Minecraft.getInstance();
		Gui gui = minecraft.gui;
		if (minecraft.gameMode.canHurtPlayer()) {
			if (player != null) {
				int l = graphics.guiWidth() / 2 - 91;
				minecraft.getProfiler().push("er_health");
				float health = player.getHealth() ;
				float max_health = player.getMaxHealth();
				ErCombatVariables.PlayerVariables vars = player.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseThrow(Error::new);
				int leftHeight = gui.leftHeight;
				int j2 = graphics.guiHeight() - leftHeight;
				RenderSystem.enableBlend();

				graphics.blitSprite(HEALTH_BAR_BASE, l , j2, 81, 9);
				graphics.blitSprite(HEALTH_BAR_BLINKING, 81 , 9 , 0 , 0,  l , j2, calculateHealth(lastHealth, max_health), 9);
				graphics.blitSprite(HEALTH_BAR_FILLING, 81 , 9 , 0 , 0,  l , j2, calculateHealth(health, max_health), 9);
				if(player.getAbsorptionAmount() > 0)
					graphics.blitSprite(HEALTH_BAR_ABSORBING, 81 , 9 , 0 , 0,  l , j2, calculateHealth(player.getAbsorptionAmount(), max_health), 9);

				Font font = minecraft.font ;
				String s = (int)(health + player.getAbsorptionAmount()) + "/" + (int)max_health ;
				int j = l - font.width(s) / 2 + 41;
				graphics.drawString(font, s, j, j2 + 1, 0xffffff, false);

				if(lastDamageTick + 20 >= Minecraft.getInstance().level.getGameTime()) {
					j = l + font.width(s) / 2 + 45;
					graphics.drawString(font, "-" + lastDamage, j + 1, j2 + 1, 0, false);
					graphics.drawString(font, "-" + lastDamage, j, j2 + 2, 0, false);
					graphics.drawString(font, "-" + lastDamage, j - 1, j2 + 1, 0, false);
					graphics.drawString(font, "-" + lastDamage, j , j2, 0, false);
					graphics.drawString(font, "-" + lastDamage, j, j2 + 1, (Minecraft.getInstance().level.getGameTime() % 8 >= 4 ? damageColor : 0xffffffff), false);
				}

				RenderSystem.disableBlend();
				leftHeight += 10;
				minecraft.getProfiler().pop();
				gui.leftHeight = leftHeight;
			}
		}

		 */
	}

	private static void RenderPlayerArmor(Player player, GuiGraphics graphics) {
		/*
		Minecraft minecraft = Minecraft.getInstance();
		Gui gui = minecraft.gui;
		if (player != null && minecraft.gameMode.canHurtPlayer()) {
			float armor = player.getArmorValue();
			if(armor > 0) {
				int l = graphics.guiWidth() / 2 - 91;
				int leftHeight = gui.leftHeight;
				int j = graphics.guiHeight() - leftHeight;
				minecraft.getProfiler().push("er_armor");
				graphics.blitSprite(ARMOR_FULL_SPRITE, l, j, 9, 9);

				Font font = minecraft.font;
				String s = String.valueOf(armor);
				int j1 = l + 11;
				graphics.drawString(font, s, j1, j + 1, 0xffffff, false);

				minecraft.getProfiler().pop();
				if (player.getArmorValue() > 0) {
					gui.leftHeight += 10;
				}
			}
		}

		 */
	}

	private static int calculateHealth(float health, float max_health){
		return Mth.ceil(Math.min(health,max_health) / max_health * 80) ;
	}

	public static void updateDamage(int damage, int color){
		lastDamage = damage;
		damageColor = color ;
		lastDamageTick = Minecraft.getInstance().level.getGameTime() ;
	}
}