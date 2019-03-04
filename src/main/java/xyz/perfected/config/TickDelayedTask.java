package xyz.perfected.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickDelayedTask
{
    private int counter;
    private Runnable run;
    
    public static void schedule(final Runnable run, final int ticks) {
        new TickDelayedTask(run, ticks);
    }
    
    public static void schedule(final Runnable run) {
        schedule(run, 1);
    }
    
    public TickDelayedTask(final Runnable run, final int ticks) {
        counter = ticks;
        this.run = run;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (counter <= 0) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            run.run();
        }
        --counter;
    }
}
