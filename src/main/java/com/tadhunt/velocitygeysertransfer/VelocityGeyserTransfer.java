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
import com.velocitypowered.api.command.CommandSource;

import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@DESCRIPTION@", authors = {"tadhunt"}, dependencies = { @Dependency(id = "geyser", optional = false) })
public class VelocityGeyserTransfer {
	private final Logger logger;

	private String addr;
	private int port;

	@Inject()
	public VelocityGeyserTransfer(CommandManager commandManager, Logger logger) {
		this.logger = logger;
		this.addr = null;
		this.port = 0;

		CommandMeta bedrockCommand = commandManager.metaBuilder("bedrock").build();
		commandManager.register(bedrockCommand, new CommandBedrock(this));
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
		logger.info(String.format("uuid: %s", uuid.toString()));

		GeyserConnection connection = findConnection(uuid);
		if(connection == null) {
			logger.info(String.format("uuid %s: not a bedrock player (ignore)", uuid.toString()));
			return;
		}

		String name = connection.bedrockUsername();
		if(name == null) {
			logger.info(String.format("uuid %s: no bedrock username (ignore)", uuid.toString()));
			return;
		}

		String xuid = connection.xuid();
		if(xuid == null) {
			logger.info(String.format("uuid %s: no bedrock xuid (ignore)", uuid.toString()));
			return;
		}

		if(this.addr == null || this.port == 0)  {
			logger.info(String.format("Player(%s, %s): addr not set (ignore)", name, uuid.toString()));
			return;
		}

		logger.info(String.format("Player(%s, %s): transfer to [%s]:%d", name, uuid.toString(), this.addr, this.port));

		connection.transfer(this.addr, this.port);
		chooseServerEvent.setInitialServer(null);
	}

	private GeyserConnection findConnection(UUID uuid) {
		GeyserApi api = GeyserApi.api();

		if(api == null) {
			logger.info("GeyserApi not yet initialized");
			return null;
		}

		List<? extends GeyserConnection> connections = GeyserApi.api().onlineConnections();
		GeyserConnection connection = null;
        for (GeyserConnection c : connections) {
            logger.info(String.format("java %s bedrock %s javauuid %s xuid %s", c.javaUsername(), c.bedrockUsername(), c.javaUuid().toString(), c.xuid()));

			UUID cUuid = c.javaUuid();
			if(uuid.equals(cUuid)) {
				connection = c;
			}
        }

		if(connection == null) {
			return null;
		}
		
		GeyserConnection c = api.connectionByUuid(uuid);
		if(c == null) {
			logger.info(String.format("bug: found by uuid compare, but not via connectionByUuid", uuid.toString()));
		}

		return connection;
	}

	public void setServer(String addr, int port) {
		this.addr = addr;
		this.port = port;
	}

	public void clearServer() {
		addr = null;
		port = 0;
	}

	public String getAddr() {
		return addr;
	}

	public int getPort() {
		return port;
	}
}
