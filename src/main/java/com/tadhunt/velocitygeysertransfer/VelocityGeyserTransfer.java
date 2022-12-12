package com.tadhunt.velocitygeysertransfer;

import com.google.inject.Inject;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.UUID;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@DESCRIPTION@", authors = {"tadhunt"}, dependencies = { @Dependency(id = "geyser", optional = false) })
public class VelocityGeyserTransfer {
	private final Logger logger;

	@Getter
	private String addr;
	@Getter
	private int port;

	@Inject()
	public VelocityGeyserTransfer(CommandManager commandManager, Logger logger) {
		this.logger = logger;
		this.addr = null;
		this.port = 0;

		CommandMeta serverCmd = commandManager.metaBuilder("server").build();
		commandManager.register(serverCmd, new CommandSetBedrockServer(this));
		logger.info("VelocityGeyserTransfer ready");
	}

	/**
	* @param event Velocity init event
	*/
	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
	}

	@Subscribe
	public void onServerChooseEvent(PlayerChooseInitialServerEvent chooseServerEvent) {
		UUID uuid = chooseServerEvent.getPlayer().getUniqueId();
//		GeyserConnection connection = GeyserApi.api().connectionByUuid(uuid);

		logger.info(String.format("VelocityGeyserTransfer: uuid %s connected", uuid.toString()));
	}

	public void setServer(String addr, int port) {
		this.addr = addr;
		this.port = port;
	}
}
