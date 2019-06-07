import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;


public class Bot extends ListenerAdapter {

     private static ProfanityFilter filter;

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(Ref.getToken()).buildBlocking();
        jda.addEventListener(new Bot());
        filter = new ProfanityFilter();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {

        User user = evt.getAuthor();
        MessageChannel channel = evt.getChannel();
        Message message = evt.getMessage();

        String response;

        if(filter.badWordsFound(message.getContentRaw())) {
            response = filter.respondToCurse(message.getContentRaw());
            if (!response.equalsIgnoreCase("") || !response.isEmpty()) {
                channel.sendMessage(user.getAsMention() + response).queue();
            }
        }

        //commands
        if (message.getContentRaw().equalsIgnoreCase(Ref.getPrefix()+ "ping")) {
            channel.sendMessage(user.getAsMention() + " Pong!").queue();
        }
        if (message.getContentRaw().equalsIgnoreCase( "patty")) {
            channel.sendMessage(user.getAsMention() + ", sup widdit?").queue();
        }

       /* //commands
        if (message.getContentRaw().equalsIgnoreCase(Ref.getPrefix()+ "makeHumanBean")) {
            Guild guild = evt.getGuild();
            Member member = evt.getMember();
            GuildController controller = new GuildController(guild);

            System.out.println(member.getUser().getName());

            try {
               AuditableRestAction rest = controller.addRolesToMember(member, guild.getRolesByName("Human Bean", false));
               rest.complete();
                channel.sendMessage(user.getAsMention() + "! Welcome to the server!").queue();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent evt) {
        Guild guild = evt.getGuild();
        Member member = evt.getMember();
        GuildController controller = new GuildController(guild);

        try {
            AuditableRestAction rest = controller.addRolesToMember(member, guild.getRolesByName("Human Bean", false));
            rest.complete();
            guild.getTextChannelsByName("general", false).get(0).sendMessage(member.getUser().getAsMention() + "! Welcome to the server!").queue();
        } catch (Exception e) {
            e.printStackTrace();
        }





    }

}
