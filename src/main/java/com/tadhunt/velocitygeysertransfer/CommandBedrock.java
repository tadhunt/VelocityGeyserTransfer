package com.tadhunt.velocitygeysertransfer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public class CommandBedrock implements SimpleCommand {
	private VelocityGeyserTransfer plugin;
	private static final String[] helptext = {
		"server set <addr:ipaddr> <port:0-65535> - Connect to this Bedrock server.",
		"clear - clears previously set server.",
		"show - shows which server bedrock players will be connected to.",
	};

	public CommandBedrock(VelocityGeyserTransfer plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(final Invocation invocation) {
		CommandSource source = invocation.source();
		String[] args = invocation.arguments();

		int i = 0;
		int n = args.length;

		if(n < 1) {
			source.sendMessage(Component.text("bad args").color(NamedTextColor.RED));
			return;
		}

		String cmd = args[i++];
		n--;

		switch(cmd) {
		case "server":
			if(n < 1) {
				help(source, "missing subcmd");
				return;
			}
			cmd = args[i++];
			n--;
			switch(cmd) {
			case "set":
				cmdSet(source, args, i, n);
				return;
			case "clear":
				cmdClear(source, args, i, n);
				return;
			case "show":
				cmdShow(source, args, i, n);
				return;
			}
		}

		help(source, "unknown command");
	}

	private void cmdSet(CommandSource source, String[] args, int i, int n) {
		if(n != 2) {
			help(source, "bad args");
			return;
		}

		String addr = args[i++];
		int port = Integer.parseInt(args[i++]);

		plugin.setServer(addr, port);
		source.sendMessage(Component.text(String.format("Bedrock players will be transferred to [%s]:%d", addr, port)));
	}

	private void cmdClear(CommandSource source, String[] args, int i, int n) {
		if(n != 0) {
			help(source, "unexpected args");
			return;
		}

		plugin.clearServer();
		source.sendMessage(Component.text("Bedrock server cleared"));
	}

	private void cmdShow(CommandSource source, String[] args, int i, int n) {
		if(n != 0) {
			help(source, "unexpected args");
			return;
		}

		String addr = this.plugin.getAddr();
		int port = this.plugin.getPort();

		if(addr == null || port == 0) {
			source.sendMessage(Component.text("Bedrock server is not set"));
		} else {
			source.sendMessage(Component.text(String.format("Bedrock players will be transferred to [%s]:%d", addr, port)));
		}
	}

	private void help(CommandSource source, String msg) {
		source.sendMessage(Component.text(String.format("Error: %s", msg)).color(NamedTextColor.RED));
		for(int i = 0; i < helptext.length; i++) {
			source.sendMessage(Component.text(helptext[i]).color(NamedTextColor.AQUA));
		}
	}
}
