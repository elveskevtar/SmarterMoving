package net.smart.moving;

import org.apache.logging.log4j.Logger;

import api.player.client.ClientPlayerAPI;
import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBaseSorting;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBaseSorting;
import api.player.server.ServerPlayerAPI;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.smart.moving.capabilities.SmartCapabilities;
import net.smart.moving.model.SmartModelPlayerBase;
import net.smart.moving.network.SmartPacketHandler;
import net.smart.moving.player.SmartPlayer;
import net.smart.moving.player.SmartPlayerBase;
import net.smart.moving.player.SmartServerPlayerBase;
import net.smart.moving.render.SmartRenderPlayerBase;

@Mod(modid = SmartMoving.MODID, name = SmartMoving.NAME, version = SmartMoving.VERSION, useMetadata = true)
public class SmartMoving {
    public static final String MODID = "smartmoving";
    public static final String NAME = "Smart Moving";
    public static final String VERSION = "@VERSION@";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        
        if (event.getSide() == Side.CLIENT)
        	renderAPI_register();
        
        SmartCapabilities.registerCapabilities();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
    	SmartPacketHandler.registerPackets();
    	
    	if (event.getSide() == Side.CLIENT) {
    		playerAPI_register();
    		SmartMovingFactory.initialize();
    		SmartMovingContext.initialize();
    	}
    }
    
    @EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (event.getSide() == Side.SERVER)
			ServerPlayerAPI.register(NAME, SmartServerPlayerBase.class);
	}
    
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    	SmartCapabilities.attachCapabilities(event);
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	if (event.phase == Phase.START && event.player.world.isRemote)
    		SmartPlayer.onPlayerTick(event.player);
    }
    
    private void playerAPI_register() {
    	ClientPlayerAPI.register(NAME, SmartPlayerBase.class);
    	ServerPlayerAPI.register(NAME, SmartServerPlayerBase.class);
    }
    
    private void renderAPI_register() {
    	String[] inferiors = new String[] { NAME };

		RenderPlayerBaseSorting renderSorting = new RenderPlayerBaseSorting();
		renderSorting.setAfterLocalConstructingInferiors(inferiors);
		renderSorting.setOverrideDoRenderInferiors(inferiors);
		renderSorting.setOverrideRotateCorpseInferiors(inferiors);
		renderSorting.setOverrideRenderLivingAtInferiors(inferiors);
    	RenderPlayerAPI.register(NAME, SmartRenderPlayerBase.class, renderSorting);
    	
    	ModelPlayerBaseSorting modelSorting = new ModelPlayerBaseSorting();
		modelSorting.setAfterLocalConstructingInferiors(inferiors);
    	ModelPlayerAPI.register(NAME, SmartModelPlayerBase.class, modelSorting);
    }
}
