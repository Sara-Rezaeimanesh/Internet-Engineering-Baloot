package all;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;

public class BalootApplication {
    public static void main(String[] args) throws Exception {
        Amazon amazon = new Amazon();
        CommandHandler cm = new CommandHandler(amazon);
        cm.run(8080);
    }
}

