package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.CodecoolerMenuOption;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.CodecoolerView;
import java.util.ArrayList;

public class CodecoolerController implements Bootable<Codecooler> {

    private CodecoolerView codecoolerView = new CodecoolerView();
    private ArtifactDAO artifactDAO = new ArtifactDAO();

    public boolean start(Codecooler codecooler) {

        codecoolerView.displayCodecoolerMenu();
        int userChoice = codecoolerView.getUserChoice();
        CodecoolerMenuOption codecoolerMenuOption = CodecoolerMenuOption.values()[userChoice];

        codecoolerView.clearTerminal();

        switch(codecoolerMenuOption) {

            case DISPLAY_WALLET:
                displayWallet(codecooler);
                break;
            case BUY_ARTIFACT:
                buyArtifact();
                break;
            case BUY_WITH_GROUP:
                buyWithGroup();
                break;
            case DISPLAY_LEVEL:
                displayLevel(codecooler);
                break;
            case EXIT:
                return false;
        }
        codecoolerView.enterToContinue();
        return true;
    }


    public void displayWallet(Codecooler codecooler) {
        ItemDAO itemDao = new ItemDAO();

        String codecoolerWalletID;
        codecoolerWalletID = codecooler.getWallet().getID();

        ArrayList<Item> userItems;
        userItems = itemDao.getUserItems(codecoolerWalletID);

        String walletData;
        walletData = codecooler.getWallet().toString();

        codecoolerView.output(walletData);
        codecoolerView.displayUserItems(userItems);
    }

    public void buyArtifact() {
        codecoolerView.displayArtifacts(artifactDAO);

        codecoolerView.getInput("Enter artifact name: ");
    }

    public void buyWithGroup(){
        // TODO: Demo:
        codecoolerView.output("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(Codecooler codecooler) {
        codecoolerView.output(codecooler.getLevel().toString());
    }

    public void updateLevel(Codecooler codecooler) {
        LevelDAO levelDao = new LevelDAO();
        ArrayList<Level> levelList = levelDao.readAll();
        Integer playerExperience = codecooler.getWallet().getEarnedCoins();

        for (Level level: levelList) {
            if (playerExperience >= level.getRequiredExperience()) {
                codecooler.setLevel(level);
            }
        }
    }
}