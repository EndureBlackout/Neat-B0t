package me.endureblackout.discord.DiscordBot;

import java.util.List;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getMessage().getContentRaw().equalsIgnoreCase("!ping")) {
			e.getChannel().sendMessage("pong!").queue();
		}

		if (e.getMessage().getContentRaw().startsWith("/ban")) {
			e.getMessage().delete().queue();

			List<User> users = e.getMessage().getMentionedUsers();
			Guild guild = e.getGuild();
			Member author = e.getGuild().getMember(e.getAuthor());

			if (author.hasPermission(Permission.BAN_MEMBERS)) {
				for (User u : users) {
					Member ban = e.getGuild().getMember(u);

					banMember(ban, author, guild, e.getChannel().getId());
				}
			} else {
				e.getChannel().sendMessage("[Permission Error]:" + author.getEffectiveName()
						+ " does not have the correct permissions to ban.").queue();
			}
		}

		if (e.getMessage().getContentRaw().startsWith("/kick")) {
			e.getMessage().delete().queue();

			List<User> users = e.getMessage().getMentionedUsers();
			Guild guild = e.getGuild();
			Member author = guild.getMember(e.getAuthor());

			if (author.hasPermission(Permission.KICK_MEMBERS)) {
				for (User u : users) {
					Member kick = e.getGuild().getMember(u);

					kickMember(kick, author, guild, e.getChannel().getId());
				}
			} else {
				e.getChannel().sendMessage("[Permission Error]:" + author.getEffectiveName()
						+ " does not have the correct permissions to kick.").queue();
			}
		}

		if (e.getMessage().getContentRaw().startsWith("/gethyp")) {
			String[] args = e.getMessage().getContentRaw().trim().split("/gethyp");

			double side1 = 0;
			double side2 = 0;
			double side3 = 0;

			if (args.length >= 3) {
				e.getChannel().sendMessage("Too many arguments. This command takes 2 arguments.");
			} else {
				for (int i = 0; i < args.length; i++) {
					if (i == 0) {
						side1 = Integer.parseInt(args[i]);
					} else if (i == 1) {
						side2 = Integer.parseInt(args[i]);
					}
				}

				side3 = Math.hypot(side1, side2);

				e.getChannel().sendMessage("The hypotenuse is: " + side3).queue();
			}
		}
	}

	public void onEmoteAdd(EmoteAddedEvent e) {
		Emote emote = e.getEmote();

		TextChannel announcements = (TextChannel) e.getGuild().getTextChannelsByName("announcements", true);

		announcements.sendMessage(emote.getId() + " was added to the guild!").queue();
	}

	public void onJoin(GuildJoinEvent e) {
		Guild guild = e.getGuild();
		List<Member> membs = guild.getMembers();

		Server server = new Server(guild, membs);
		server.saveToDatabase();

		joinMessage(guild);
	}

	public void kickMember(Member m, Member b, Guild guild, String channel) {
		guild.getController()
				.kick(m).queue(
						success -> guild.getTextChannelById(channel)
								.sendMessage(m.getEffectiveName() + " has been kicked from the server.").queue(),
						error -> {
							if (error instanceof PermissionException) {
								guild.getTextChannelById(channel).sendMessage("Error: " + error.getStackTrace())
										.queue();
							} else {
								guild.getTextChannelById(channel).sendMessage("Error: " + error.getStackTrace())
										.queue();
							}
						});
	}

	public void joinMessage(Guild guild) {
		guild.getDefaultChannel()
				.sendMessage(
						"Hello! My name is Neat b0t. I am an administrative bot that can help you moderate your server!")
				.queue();
	}

	public void banMember(Member m, Member b, Guild guild, String channel) {
		guild.getController().ban(m, 6)
				.queue(success -> guild.getTextChannelById(channel)
						.sendMessage(m.getEffectiveName() + " has been banned by " + b.getEffectiveName()).queue(),
						error -> {
							if (error instanceof PermissionException) {
								guild.getTextChannelById(channel).sendMessage("Error: " + error.getStackTrace())
										.queue();
							} else {
								guild.getTextChannelById(channel).sendMessage("Error: " + error.getStackTrace())
										.queue();
							}
						});
	}
}
