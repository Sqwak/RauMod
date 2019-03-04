/*
 * Taken from Kbz's old RAU Mod.
 */

package pw.nora.rau;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.*;

public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "raumod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/raumod";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("rau");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Core.config.openGui();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        final List<String> list = new ArrayList<String>(Arrays.asList(
                "config"
        ));
        return ((args.length == 1) ? getListOfStringsMatchingLastWord(args, (String[])list.toArray(new String[0])) : null);
    }
}
