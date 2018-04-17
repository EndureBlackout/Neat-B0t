package me.endureblackout.discord.DiscordBot;

import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class Server {
	Guild guild;
	List<Member> members;
	
	public Server(Guild guild, List<Member> membs) {
		this.guild = guild;
		this.members = membs;
	}
	
	public void saveToDatabase() {
		
	}
	
	public Guild getGuild() {
		return guild;
	}
	
}
