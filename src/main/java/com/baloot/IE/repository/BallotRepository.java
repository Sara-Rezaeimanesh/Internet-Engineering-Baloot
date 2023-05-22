package com.baloot.IE.repository;


import com.baloot.IE.domain.User.UserRepository;

public class BallotRepository {
    private static BallotRepository instance;

    private BallotRepository() {
        createAllTables();
    }

    public static BallotRepository getInstance() {
        if (instance == null)
            instance = new BallotRepository();
        return instance;
    }

    private void createAllTables() {
        try {
            UserRepository userRepository = UserRepository.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
