package pw.nora.rau.chat;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.nora.rau.RauConfig;
import pw.nora.rau.util.CC;
import pw.nora.rau.util.ChatColor;
import pw.nora.rau.util.m;

import java.time.LocalDateTime;
import java.util.HashMap;

public class RauChat {
    public static HashMap<String, LocalDateTime> usersOnCD = new HashMap<String, LocalDateTime>();

    @SubscribeEvent
    public void onPlayerChat(ClientChatReceivedEvent e) {
        String msgColour = e.message.getFormattedText();
        String msg = e.message.getUnformattedText();
        String[] msg2 = msg.split(" ");

        if (RauConfig.getConfig.prefixEnabled.get()) {
            if (msg2[0].contains("[MVP") && !msg.contains("joined the lobby") || msg2[0].contains("[VIP") || msg2[0].contains("[HELP") || msg2[0].contains("[MOD") || msg2[0].contains("[ADMIN")) {
                String a = ChatColor.removeFormatting(msg2[1].replace(":",""));
                String player = a.replace("&7","").replace("&f","");
                e.setCanceled(true);
                m.c.thePlayer.addChatMessage(CC.of(RauConfig.getConfig.chatPrefix.get().replace("&","\u00a7"))
                        .setHoverEvent(CC.of("Click to tell " + player + " to RAU.").build())
                        .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,RauConfig.getConfig.warnMsg.get().replace("%s",player))
                        .append(" ")
                        .append(msgColour)
                        .build());
            }
            if (msgColour.startsWith("\u00a7r\u00a77") && msg2[0].endsWith(":")) {
                String a = ChatColor.removeFormatting(msg2[0].replace(":",""));
                String player = a.replace("&7","").replace("&f","");
                e.setCanceled(true);
                m.c.thePlayer.addChatMessage(CC.of(RauConfig.getConfig.chatPrefix.get().replace("&","\u00a7"))
                        .setHoverEvent(CC.of("Click to tell " + player + " to RAU.").build())
                        .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,RauConfig.getConfig.warnMsg.get().replace("%s",player))
                        .append(msgColour)
                        .append(" ")
                        .build());
            }
            if (msg2[0].toLowerCase().contains("party") || msg2[0].toLowerCase().contains("guild") || msg2[0].toLowerCase().contains("officer") || msg2[0].toLowerCase().contains("[staff]")) {
                if (msg2[2].contains("[")) {
                    String a = ChatColor.removeFormatting(msg2[3].replace(":",""));//.replace("&7","").replace("&f","");
                    String player = a.replace("&7","").replace("&f","");
                    e.setCanceled(true);
                    m.c.thePlayer.addChatMessage(CC.of(RauConfig.getConfig.chatPrefix.get().replace("&","\u00a7"))
                            .setHoverEvent(CC.of("Click to tell " + player + " to RAU.").build())
                            .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,RauConfig.getConfig.warnMsg.get().replace("%s",player))
                            .append(msgColour)
                            .append(" ")
                            .build());
                    return;
                }
                else {
                    String player = ChatColor.removeFormatting(msg2[2].replace(":",""));
                    e.setCanceled(true);
                    m.c.thePlayer.addChatMessage(CC.of(RauConfig.getConfig.chatPrefix.get().replace("&","\u00a7"))
                            .setHoverEvent(CC.of("Click to tell " + player + " to RAU.").build())
                            .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND,RauConfig.getConfig.warnMsg.get().replace("%s",player))
                            .append(" ")
                            .append(msgColour)
                            .append(" ")
                            .build());
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onFriendJoinServer(ClientChatReceivedEvent e) throws InterruptedException {
        String msg = e.message.getUnformattedText();
        String msg2[] = msg.split(" ");

        if (RauConfig.getConfig.msgPlayersEnabled.get()) {
            if (msg2[1].endsWith("joined.")) {
                String player = msg2[0];
                e.setCanceled(true);
                m.c.thePlayer.addChatMessage(CC.of(player + " joined.")
                        .setColor(EnumChatFormatting.YELLOW)
                        .setHoverEvent(CC.of("Click to send a message to " + player).build())
                        .setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + player + " " + RauConfig.getConfig.welcomeMsg.get().replace("%s", player))
                        .build());
            }
        }
    }
}
