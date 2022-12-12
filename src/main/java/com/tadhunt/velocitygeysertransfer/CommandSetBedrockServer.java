package com.tadhunt.velocitygeysertransfer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public class CommandSetBedrockServer implements SimpleCommand {
	private VelocityGeyserTransfer plugin;

	public CommandSetBedrockServer(VelocityGeyserTransfer plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(final Invocation invocation) {
		CommandSource source = invocation.source();
		String[] args = invocation.arguments();

		if(args.length != 2) {
			source.sendMessage(Component.text("bad args").color(NamedTextColor.RED));
			return;
		}

        String addr = args[0];
        int    port = Integer.parseInt(args[1]);

        this.plugin.setServer(addr, port);
		
		source.sendMessage(Component.text(String.format("Bedrock players will be transferred to [%s]:%d", addr, port)));
	}
}
