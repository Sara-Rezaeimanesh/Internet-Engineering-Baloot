package all;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;

import java.io.IOException;

public class BalootApplocation {
    public static void main(String[] args) throws IOException {
        Amazon amazon = new Amazon();
        CommandHandler cm = new CommandHandler(amazon);
        cm.run();
    }
}

