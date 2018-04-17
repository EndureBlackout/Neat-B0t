package me.endureblackout.discord.DiscordBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class BotMain {

	public static JDA jda;
	public static final String BOT_TOKEN = "NDMxODAyMjM4Njk3MjA5ODU2.DakDfg.l90A-4MwO9duRBJAVFc2HeAotJk";

	public static void main(String[] args) {
		try {
			jda = new JDABuilder(AccountType.BOT).addEventListener(new BotListener()).setToken(BOT_TOKEN).buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
