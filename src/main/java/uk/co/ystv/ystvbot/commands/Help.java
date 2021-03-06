package uk.co.ystv.ystvbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputUser;

public class Help extends Command {

	@Override
	public void onGenericMessage(GenericMessageEvent<PircBotX> event) throws Exception {
		if (event.getMessage().equalsIgnoreCase("!help")) {
			OutputUser ircout = event.getUser().send();

			ircout.notice("YSTV Morrissey bot - Top_Cat 2014!");
			ircout.notice("Avaliable commands:");

			String[] helpTexts = null;
			for (Commands cmd : Commands.values()) {
				if ((helpTexts = cmd.getObj().helpText()) != null) {
					for (String helpText : helpTexts) {
						ircout.notice(helpText);
					}
				}
			}
		}
	}

	@Override
	String[] helpText() {
		return new String[] {
				"!help - such meta, so help"
		};
	}
}
