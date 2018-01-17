package fr.taeron;

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid = "UnfocusedCPUMod", version = "0.1")
public class UnfocusedCPUMod {

	private boolean focused;
	
	@EventHandler 
	public void init(FMLInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(this);
		this.focused = true;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e){
		if(Minecraft.getMinecraft().theWorld == null){
			return;
		}
		if(!Display.isActive() && this.focused){
			this.focused = false;
			Thread th = new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						Thread.sleep(500);
						Minecraft.getMinecraft().gameSettings.limitFramerate = 1;
						Display.setTitle("[Unfocused] " + Display.getTitle());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			th.run();
			
		} else if (Display.isActive() && Minecraft.getMinecraft().gameSettings.limitFramerate == 1){
			this.focused = true;
			Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
			Display.setTitle(Display.getTitle().replace("[Unfocused] ", ""));
		}
	}
}
