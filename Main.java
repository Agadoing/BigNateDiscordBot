import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException{

        String token = "token";
        JDABuilder.createDefault(token).addEventListeners(new Main()).setActivity(Activity.playing("Enter +help for help!")).build();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) throws StringIndexOutOfBoundsException {

        Message msg = event.getMessage();
        String message = msg.getContentRaw();
        String[] arrayOfWords = breakUpMessage(message);

        if ((message.charAt(0)) == '+')
        {
            String command = arrayOfWords[0];

            if (command.equals("+comic")){
                getComicFromDate(arrayOfWords, event);
            } else if (command.equals("+random")){
                //TODO: add a random command

            }
        }
    }

    public static String[] breakUpMessage(String message){

        return message.split(" ");
    }

    public static void getComicFromDate(String[] arrayOfWords, MessageReceivedEvent event){

        MessageChannel channel = event.getChannel();
        try {
            channel.sendMessage(getImageLink(arrayOfWords)).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getImageLink(String[] arrayOfWords) throws IOException {    //date is in the format (yyyy/mm/dd)

        String URL = "https://www.gocomics.com/" + arrayOfWords[1] + "/" + arrayOfWords[2];
        System.out.println(URL);
        Document doc = Jsoup.connect(URL).get();

        Elements metaTags = doc.getElementsByTag("meta");

        for (Element metaTag : metaTags) {
            String content = metaTag.attr("content");
            String property = metaTag.attr("property");

            if(property.equals("og:image")) {
                return content;

            }

        }

        return "You pantload! That's not the right input!";
    }

//    public static LocalDate convertToDate (String date){    //The argument date must be in the format yyyy/mm/dd
//
//
//    }
}
