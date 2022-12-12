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

import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

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
		GeyserApi api = GeyserApi.api();

		if(api == null) {
			logger.info("VelocityGeyserTransfer: GeyserApi not yet initialized (ignore)");
			return;
		}
		GeyserConnection connection = GeyserApi.api().connectionByUuid(uuid);

		if(connection == null) {
			logger.info(String.format("VelocityGeyserTransfer: uuid %s: not a bedrock player (ignore)", uuid.toString()));
			return;
		}

		if(this.addr == null || this.port == 0)  {
			logger.info(String.format("VelocityGeyserTransfer: uuid %s: addr not set (ignore)", uuid.toString()));
			return;
		}
		logger.info(String.format("VelocityGeyserTransfer: uuid %s: transfer to [%s]:%d", uuid.toString(), this.addr, this.port));
	}

	public void setServer(String addr, int port) {
		this.addr = addr;
		this.port = port;
	}
}
